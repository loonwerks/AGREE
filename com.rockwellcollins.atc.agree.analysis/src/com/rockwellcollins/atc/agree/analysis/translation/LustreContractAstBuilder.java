/*
 * Copyright (c) 2021, Collins Aerospace.
 * Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this data,
 * including any software or models in source or binary form, as well as any drawings, specifications,
 * and documentation (collectively "the Data"), to deal in the Data without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Data.
 *
 * THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
 */
package com.rockwellcollins.atc.agree.analysis.translation;

import java.util.ArrayList;
import java.util.List;

import com.rockwellcollins.atc.agree.agree.AssumeStatement;
import com.rockwellcollins.atc.agree.agree.LemmaStatement;
import com.rockwellcollins.atc.agree.analysis.AgreeException;
import com.rockwellcollins.atc.agree.analysis.AgreeUtils;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNode;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNode.TimingModel;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNodeBuilder;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeProgram;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeStatement;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeVar;
import com.rockwellcollins.atc.agree.analysis.lustre.visitors.IdRewriteVisitor;
import com.rockwellcollins.atc.agree.analysis.lustre.visitors.IdRewriter;

import jkind.lustre.BinaryExpr;
import jkind.lustre.BinaryOp;
import jkind.lustre.BoolExpr;
import jkind.lustre.CondactExpr;
import jkind.lustre.Contract;
import jkind.lustre.Equation;
import jkind.lustre.Expr;
import jkind.lustre.IdExpr;
import jkind.lustre.NamedType;
import jkind.lustre.Node;
import jkind.lustre.NodeCallExpr;
import jkind.lustre.Program;
import jkind.lustre.TupleExpr;
import jkind.lustre.TypeDef;
import jkind.lustre.UnaryExpr;
import jkind.lustre.UnaryOp;
import jkind.lustre.VarDecl;
import jkind.lustre.builders.NodeBuilder;
import jkind.lustre.builders.ProgramBuilder;

public class LustreContractAstBuilder extends LustreAstBuilder {

	public static Program getContractLustreProgram(AgreeProgram agreeProgram) {

		nodes = new ArrayList<>();
		List<TypeDef> types = AgreeUtils.getLustreTypes(agreeProgram);

		AgreeNode flatNode = flattenAgreeNodeKindContract(agreeProgram.topNode, "_TOP__");
		List<Expr> assertions = new ArrayList<>();
		List<VarDecl> locals = new ArrayList<>();
		List<VarDecl> inputs = new ArrayList<>();
		List<VarDecl> outputs = new ArrayList<>();
		List<Equation> equations = new ArrayList<>();
		List<String> properties = new ArrayList<>();
		List<Expr> requires = new ArrayList<>();
		List<Expr> ensures = new ArrayList<>();

		for (AgreeStatement assertion : flatNode.assertions) {
			assertions.add(assertion.expr);
		}

		for (AgreeStatement assumption : flatNode.assumptions) {
			requires.add(assumption.expr);
		}

		for (AgreeStatement guarantee : flatNode.lemmas) {
			ensures.add(guarantee.expr);
		}

		for (AgreeStatement guarantee : flatNode.guarantees) {
			ensures.add(guarantee.expr);
		}

		for (AgreeVar var : flatNode.inputs) {
			inputs.add(var);
		}

		for (AgreeVar var : flatNode.outputs) {
			outputs.add(var);
		}

		for (AgreeVar var : flatNode.outputs) {
			if (var.reference instanceof AssumeStatement || var.reference instanceof LemmaStatement) {
				throw new AgreeException("This shouldn't happen");
			}
		}

		Contract contract = new Contract(requires, ensures);

		NodeBuilder builder = new NodeBuilder("_TOP");
		builder.addInputs(inputs);
		builder.addOutputs(outputs);
		builder.addLocals(locals);
		builder.addEquations(equations);
		builder.addProperties(properties);
		builder.addAssertions(assertions);
		builder.setContract(contract);

		Node main = builder.build();

		nodes.addAll(agreeProgram.globalLustreNodes);
		nodes.add(main);

		Program program = new ProgramBuilder().addTypes(types).addNodes(nodes).setMain(main.id).build();

		return program;

	}

	protected static AgreeNode flattenAgreeNodeKindContract(AgreeNode agreeNode, String nodePrefix) {

		List<AgreeVar> inputs = new ArrayList<>();
		List<AgreeVar> outputs = new ArrayList<>();
		List<AgreeVar> locals = new ArrayList<>();
		List<AgreeStatement> assertions = new ArrayList<>();

		Expr someoneTicks = null;
		for (AgreeNode subAgreeNode : agreeNode.subNodes) {
			String prefix = subAgreeNode.id + AgreeASTBuilder.dotChar;
			Expr clockExpr = getClockExpr(agreeNode, subAgreeNode);

			if (someoneTicks == null) {
				someoneTicks = clockExpr;
			} else {
				someoneTicks = new BinaryExpr(someoneTicks, BinaryOp.OR, clockExpr);
			}

			AgreeNode flatNode = flattenAgreeNodeKindContract(subAgreeNode,
					nodePrefix + subAgreeNode.id + AgreeASTBuilder.dotChar);

			Node lustreNode = addSubNodeLustre(agreeNode, nodePrefix, flatNode);

			addInputsAndOutputs(inputs, outputs, flatNode, lustreNode, prefix);

			addCondactCall(agreeNode, nodePrefix, inputs, assertions, flatNode, prefix, clockExpr, lustreNode);

			// addClockHolds(agreeNode, assertions, flatNode, clockExpr, prefix,
			// lustreNode);

			addInitConstraint(agreeNode, outputs, assertions, flatNode, prefix, clockExpr, lustreNode);

		}

		if (agreeNode.timing == TimingModel.ASYNC) {
			if (someoneTicks == null) {
				throw new AgreeException("Somehow we generated a clock constraint without any clocks");
			}
			assertions.add(new AgreeStatement("someone ticks", someoneTicks, null));
		}

		addConnectionConstraints(agreeNode, assertions);

		// add any clock constraints
		assertions.addAll(agreeNode.assertions);
		assertions.add(new AgreeStatement("", agreeNode.clockConstraint, null));
		inputs.addAll(agreeNode.inputs);
		outputs.addAll(agreeNode.outputs);
		locals.addAll(agreeNode.locals);

		AgreeNodeBuilder builder = new AgreeNodeBuilder(agreeNode.id);
		builder.addInput(inputs);
		builder.addOutput(outputs);
		builder.addLocal(locals);
		builder.addLocalEquation(agreeNode.localEquations);
		builder.addSubNode(agreeNode.subNodes);
		builder.addAssertion(assertions);
		builder.addAssumption(agreeNode.assumptions);
		builder.addGuarantee(agreeNode.guarantees);
		builder.addLemma(agreeNode.lemmas);
		builder.addPatternProp(agreeNode.patternProps);
		builder.setClockConstraint(new BoolExpr(true));
		builder.setInitialConstraint(agreeNode.initialConstraint);
		builder.setClockVar(agreeNode.clockVar);
		builder.setReference(agreeNode.reference);
		builder.setTiming(null);
		builder.addEventTime(agreeNode.eventTimes);
		builder.setCompInst(agreeNode.compInst);

		return builder.build();

	}

	protected static void addInitConstraint(AgreeNode agreeNode, List<AgreeVar> outputs,
			List<AgreeStatement> assertions, AgreeNode subAgreeNode, String prefix, Expr clockExpr, Node lustreNode) {
		if (agreeNode.timing != TimingModel.SYNC) {
			String tickedName = subAgreeNode.id + "___TICKED";
			outputs.add(new AgreeVar(tickedName, NamedType.BOOL, null, agreeNode.compInst, null));
			Expr tickedId = new IdExpr(tickedName);
			Expr preTicked = new UnaryExpr(UnaryOp.PRE, tickedId);
			Expr tickedOrPre = new BinaryExpr(clockExpr, BinaryOp.OR, preTicked);
			Expr initOrTicked = new BinaryExpr(clockExpr, BinaryOp.ARROW, tickedOrPre);
			Expr tickedEq = new BinaryExpr(tickedId, BinaryOp.EQUAL, initOrTicked);

			assertions.add(new AgreeStatement("", tickedEq, null));

			// we have two re-write the ids in the initial expressions
			IdRewriter rewriter = id -> new IdExpr(prefix + id.id);
			Expr newInit = subAgreeNode.initialConstraint.accept(new IdRewriteVisitor(rewriter));

			Expr initConstr = new BinaryExpr(new UnaryExpr(UnaryOp.NOT, tickedId), BinaryOp.IMPLIES, newInit);
			assertions.add(new AgreeStatement("", initConstr, null));

			// we also need to add hold expressions for the assumptions and
			// lemmas
			Expr assumeLemmaTrue = new BoolExpr(true);
			for (VarDecl lustreVar : lustreNode.inputs) {
				AgreeVar var = (AgreeVar) lustreVar;
				if (var.reference instanceof AssumeStatement || var.reference instanceof LemmaStatement) {
					assumeLemmaTrue = new BinaryExpr(assumeLemmaTrue, BinaryOp.AND, new IdExpr(prefix + var.id));
				}
			}
			assumeLemmaTrue = new BinaryExpr(new UnaryExpr(UnaryOp.NOT, tickedId), BinaryOp.IMPLIES, assumeLemmaTrue);
			assertions.add(new AgreeStatement("", assumeLemmaTrue, null));

		}
	}

	protected static Node addSubNodeLustre(AgreeNode agreeNode, String nodePrefix, AgreeNode flatNode) {

		Node lustreNode = getLustreNode(flatNode, nodePrefix);
		addToNodes(lustreNode);
		return lustreNode;
	}

	protected static Node getLustreNode(AgreeNode agreeNode, String nodePrefix) {

		List<VarDecl> inputs = new ArrayList<>();
		List<VarDecl> outputs = new ArrayList<>();
		List<VarDecl> locals = new ArrayList<>();
		List<Equation> equations = new ArrayList<>();
		List<Expr> assertions = new ArrayList<>();
		List<Expr> requires = new ArrayList<>();
		List<Expr> ensures = new ArrayList<>();

		for (AgreeStatement statement : agreeNode.assumptions) {
			requires.add(statement.expr);
		}

		for (AgreeStatement statement : agreeNode.lemmas) {
			ensures.add(statement.expr);
		}

		for (AgreeStatement statement : agreeNode.guarantees) {
			ensures.add(statement.expr);
		}

		for (AgreeStatement statement : agreeNode.assertions) {
			assertions.add(statement.expr);
			if (AgreeUtils.referenceIsInContract(statement.reference, agreeNode.compInst)) {
				ensures.add(statement.expr);
			}
		}

		// gather the remaining inputs
		for (AgreeVar var : agreeNode.inputs) {
			inputs.add(var);
		}
		for (AgreeVar var : agreeNode.outputs) {
			outputs.add(var);
		}
		// Contract contract = new Contract(nodePrefix + agreeNode.id, requires, ensures);
		Contract contract = new Contract(requires, ensures);

		NodeBuilder builder = new NodeBuilder(nodePrefix + agreeNode.id);
		builder.addInputs(inputs);
		builder.addOutputs(outputs);
		builder.addLocals(locals);
		builder.addEquations(equations);
		builder.addAssertions(assertions);
		builder.setContract(contract);

		return builder.build();
	}

	protected static void addInputsAndOutputs(List<AgreeVar> inputs, List<AgreeVar> outputs, AgreeNode subAgreeNode,
			Node lustreNode, String prefix) {
		for (AgreeVar var : subAgreeNode.inputs) {
			AgreeVar input = new AgreeVar(prefix + var.id, var.type, var.reference, var.compInst, var.featInst);
			inputs.add(input);
		}

		for (AgreeVar var : subAgreeNode.outputs) {
			AgreeVar output = new AgreeVar(prefix + var.id, var.type, var.reference, var.compInst, var.featInst);
			outputs.add(output);
		}

		// right now we do not support local variables in our translation
//        for (AgreeVar var : subAgreeNode.locals) {
//            throw new AgreeException("What is an example of this?");
//            // varCount++;
//            // AgreeVar local = new AgreeVar(prefix+var.id, var.type,
//            // var.reference, var.compInst);
//            // outputs.add(local);
//        }
		if (!subAgreeNode.locals.isEmpty()) {
			throw new AgreeException("What is an example of this?");
		}

		inputs.add(subAgreeNode.clockVar);

	}

	protected static void addCondactCall(AgreeNode agreeNode, String nodePrefix, List<AgreeVar> inputs,
			List<AgreeStatement> assertions, AgreeNode subAgreeNode, String prefix, Expr clockExpr, Node lustreNode) {
		List<Expr> inputIds = new ArrayList<>();
		List<Expr> initOutputsVals = new ArrayList<>();
		List<IdExpr> nodeOutputIds = new ArrayList<>();
		for (VarDecl var : lustreNode.inputs) {
			inputIds.add(new IdExpr(prefix + var.id));
		}

		for (VarDecl var : lustreNode.outputs) {
			AgreeVar outputVar = (AgreeVar) var;
			String dummyName = prefix + var.id + "__DUMMY";
			AgreeVar dummyVar = new AgreeVar(dummyName, outputVar.type, outputVar.reference, outputVar.compInst,
					outputVar.featInst);

			if (!inputs.contains(dummyVar)) {
				inputs.add(dummyVar);
			}

			initOutputsVals.add(new IdExpr(dummyName));
			nodeOutputIds.add(new IdExpr(prefix + var.id));
		}

		if (agreeNode.timing == TimingModel.LATCHED) {
			throw new AgreeException(
					"check how we do this in the generic lustre translation now" + " to make sure that it is correct");
		}

		Expr condactExpr = new CondactExpr(clockExpr, new NodeCallExpr(lustreNode.id, inputIds), initOutputsVals);

		Expr condactOutput;
		if (nodeOutputIds.size() > 1) {
			condactOutput = new TupleExpr(nodeOutputIds);
		} else {
			condactOutput = nodeOutputIds.get(0);
		}

		Expr condactCall = new BinaryExpr(condactOutput, BinaryOp.EQUAL, condactExpr);
		assertions.add(new AgreeStatement("", condactCall, null));
	}

}
