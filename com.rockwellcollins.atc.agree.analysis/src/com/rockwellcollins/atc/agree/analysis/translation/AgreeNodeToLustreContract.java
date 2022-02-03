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

import com.rockwellcollins.atc.agree.analysis.AgreeException;
import com.rockwellcollins.atc.agree.analysis.AgreeUtils;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNode;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeProgram;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeStatement;

import jkind.lustre.BinaryExpr;
import jkind.lustre.BinaryOp;
import jkind.lustre.Equation;
import jkind.lustre.Expr;
import jkind.lustre.IdExpr;
import jkind.lustre.NamedType;
import jkind.lustre.Node;
import jkind.lustre.Program;
import jkind.lustre.RecordType;
import jkind.lustre.TupleExpr;
import jkind.lustre.Type;
import jkind.lustre.TypeDef;
import jkind.lustre.VarDecl;
import jkind.lustre.builders.NodeBuilder;
import jkind.lustre.builders.ProgramBuilder;
import jkind.translation.DistributePres;
import jkind.translation.FlattenPres;
import jkind.translation.InlineNodeCalls;
import jkind.translation.OrderEquations;

public class AgreeNodeToLustreContract {

	public static Node translate(AgreeNode agreeNode, AgreeProgram agreeProgram) {

		List<Node> nodes = new ArrayList<>();

		nodes.addAll(agreeProgram.globalLustreNodes);
		// this node needs to be the last in the list
		// so that it is set as the main node in the program
		nodes.add(translateNode(agreeNode));

		List<TypeDef> types = new ArrayList<>();
		for (Type type : agreeProgram.globalTypes) {
			RecordType recType = (RecordType) type;
			types.add(new TypeDef(recType.id, type));
		}

		Program program = new ProgramBuilder().addTypes(types).addNodes(nodes).build();

		program = InlineNodeCalls.program(program);
		program = FlattenPres.program(program);

		Node main = DistributePres.node(program.getMainNode());
		main = OrderEquations.node(main);

		return main;
	}

	private static Node translateNode(AgreeNode agreeNode) {

		List<VarDecl> inputs = new ArrayList<>();
		List<VarDecl> locals = new ArrayList<>();
		List<Equation> eqs = new ArrayList<>();
		List<Expr> assertions = new ArrayList<>();
		List<String> properties = new ArrayList<>();

		inputs.addAll(agreeNode.inputs);
		inputs.addAll(agreeNode.outputs);
		inputs.addAll(agreeNode.locals);

		eqs.addAll(nodeAssertsToEqs(agreeNode));

		// right now the AGREE AST just has assertions over input variables
		// this step allows us to inline some of these as local variables
		// to the node
		List<IdExpr> ids = gatherIds(eqs);
		List<VarDecl> inlinedVars = new ArrayList<>();
		for (VarDecl var : inputs) {
			for (IdExpr id : ids) {
				if (id.id.equals(var.id)) {
					inlinedVars.add(var);
				}
			}
		}

		inputs.removeAll(inlinedVars);
		locals.addAll(inlinedVars);

		for (AgreeStatement statement : agreeNode.assumptions) {
			assertions.add(statement.expr);
		}

		int i = 0;
		for (AgreeStatement statement : agreeNode.guarantees) {
			String guarName = "__GUARANTEE" + i++;
			properties.add(guarName);
			locals.add(new VarDecl(guarName, NamedType.BOOL));
			eqs.add(new Equation(new IdExpr(guarName), statement.expr));
		}

		NodeBuilder builder = new NodeBuilder(agreeNode.id);
		builder.addInputs(inputs);
		builder.addLocals(locals);
		builder.addEquations(eqs);
		builder.addProperties(properties);
		builder.addAssertions(assertions);

		return builder.build();

	}

	private static List<Equation> nodeAssertsToEqs(AgreeNode agreeNode) {

		List<Equation> eqs = new ArrayList<>();
		for (AgreeStatement statement : agreeNode.assertions) {
			if (AgreeUtils.referenceIsInContract(statement.reference, agreeNode.compInst)) {
				BinaryExpr expr = (BinaryExpr) statement.expr;

				if ((expr.op != BinaryOp.EQUAL)) {
					throw new AgreeException("Unexpected expression type in AGREE AST asssertion");
				}

				Expr leftExpr = expr.left;
				Expr rightExpr = expr.right;
				List<IdExpr> ids = new ArrayList<>();
				if (leftExpr instanceof TupleExpr) {
					TupleExpr tuple = (TupleExpr) leftExpr;
					for (Expr element : tuple.elements) {
						ids.add((IdExpr) element);
					}
				} else {
					ids.add((IdExpr) leftExpr);
				}

				eqs.add(new Equation(ids, rightExpr));
			}
		}

		return eqs;
	}

	private static List<IdExpr> gatherIds(List<Equation> eqs) {
		List<IdExpr> ids = new ArrayList<>();
		for (Equation eq : eqs) {
			ids.addAll(eq.lhs);
		}
		return ids;
	}

}
