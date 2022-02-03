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
package com.rockwellcollins.atc.agree.analysis.ast.visitors;

import static jkind.lustre.parsing.LustreParseUtil.equation;
import static jkind.lustre.parsing.LustreParseUtil.to;

import java.util.ArrayList;
import java.util.List;

import com.rockwellcollins.atc.agree.analysis.AgreeException;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTElement;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeConnection;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeEquation;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNode;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNodeBuilder;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeProgram;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeStatement;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeVar;

import jkind.lustre.Equation;
import jkind.lustre.Expr;
import jkind.lustre.IdExpr;
import jkind.lustre.visitors.ExprMapVisitor;

public class AgreeInlineLatchedConnections extends ExprMapVisitor implements AgreeASTVisitor<AgreeASTElement> {

	private final List<AgreeNode> nodes = new ArrayList<>();
	public static final String LATCHED_SUFFIX = "__LATCHED_";

	public static AgreeProgram translate(AgreeProgram program) {
		return (AgreeProgram) program.accept(new AgreeInlineLatchedConnections());
	}

	@Override
	public AgreeProgram visit(AgreeProgram program) {
		AgreeNode topNode = (AgreeNode) program.topNode.accept(this);
		return new AgreeProgram(nodes, program.globalLustreNodes, program.uninterpretedFunctions, program.globalTypes,
				topNode);
	}

	@Override
	public AgreeNode visit(AgreeNode node) {
		AgreeNodeBuilder builder = new AgreeNodeBuilder(node);
		builder.clearSubNodes();

		for (AgreeNode subNode : node.subNodes) {
			AgreeNode newSubNode = (AgreeNode) subNode.accept(this);
			builder.addSubNode(newSubNode);
		}

		if (node.timing == AgreeNode.TimingModel.LATCHED) {
			for (AgreeNode subNode : builder.build().subNodes) {
				addLatchedInputEqs(builder, subNode);
			}
		}

		AgreeNode finalNode = builder.build();
		nodes.add(finalNode);
		return finalNode;
	}

	private void addLatchedInputEqs(AgreeNodeBuilder builder, AgreeNode subNode) {
		for (AgreeVar var : subNode.inputs) {
			AgreeVar latchVar = new AgreeVar(subNode.id + "__" + var.id + LATCHED_SUFFIX, var.type, var.reference,
					var.compInst, var.featInst);
			builder.addLocal(latchVar);

			Expr clockExpr = new IdExpr(subNode.id + AgreeASTBuilder.clockIDSuffix);
			String sourceVarName = subNode.id + "__" + var.id;

			Equation latchEq = equation("latchVar = " + sourceVarName + " -> if (pre (not clockVar)) and clockVar then "
					+ sourceVarName + " else pre latchVar;", to("latchVar", latchVar), to("clockVar", clockExpr));

			builder.addLocalEquation(new AgreeEquation(latchEq, null));
		}
	}

	@Override
	public AgreeVar visit(AgreeVar var) {
		throw new AgreeException("Should not visit here");
	}

	@Override
	public AgreeEquation visit(AgreeEquation eq) {
		throw new AgreeException("Should not visit here");
	}

	@Override
	public AgreeConnection visit(AgreeConnection conn) {
		throw new AgreeException("Should not visit here");
	}

	@Override
	public AgreeASTElement visit(AgreeStatement e) {
		throw new AgreeException("Should not visit here");
	}

}
