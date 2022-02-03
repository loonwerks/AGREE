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
package com.rockwellcollins.atc.agree.analysis.realtime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.rockwellcollins.atc.agree.analysis.ast.AgreeVar;

import jkind.lustre.BinaryExpr;
import jkind.lustre.BinaryOp;
import jkind.lustre.BoolExpr;
import jkind.lustre.Equation;
import jkind.lustre.Expr;
import jkind.lustre.IdExpr;
import jkind.lustre.IfThenElseExpr;
import jkind.lustre.NamedType;
import jkind.lustre.Node;
import jkind.lustre.NodeCallExpr;
import jkind.lustre.RealExpr;
import jkind.lustre.UnaryExpr;
import jkind.lustre.UnaryOp;
import jkind.lustre.VarDecl;
import jkind.lustre.builders.NodeBuilder;

public class AgreeRealtimeCalendarBuilder {

	private static final String MIN_POS_NODE_NAME = "__MinPos";
	public static final String RISE_NODE_NAME = "__Rise";
	public static final String FALL_NODE_NAME = "__Fall";

	public static List<Node> getRealTimeNodes() {
		List<Node> nodes = new ArrayList<>();
		nodes.add(getMinPosNode());
		nodes.add(getRiseNode());
		nodes.add(getFallNode());
		return nodes;
	}

	private static Node getRiseNode() {
		NodeBuilder builder = new NodeBuilder(RISE_NODE_NAME);
		builder.addInput(new VarDecl("input", NamedType.BOOL));
		builder.addOutput(new VarDecl("output", NamedType.BOOL));

		IdExpr inputId = new IdExpr("input");
		IdExpr outputId = new IdExpr("output");

		Expr outputExpr = new UnaryExpr(UnaryOp.NOT, inputId);
		outputExpr = new UnaryExpr(UnaryOp.PRE, outputExpr);
		outputExpr = new BinaryExpr(outputExpr, BinaryOp.AND, inputId);
		outputExpr = new BinaryExpr(inputId, BinaryOp.ARROW, outputExpr);

		builder.addEquation(new Equation(outputId, outputExpr));
		return builder.build();
	}

	private static Node getFallNode() {
		NodeBuilder builder = new NodeBuilder(FALL_NODE_NAME);
		builder.addInput(new VarDecl("input", NamedType.BOOL));
		builder.addOutput(new VarDecl("output", NamedType.BOOL));

		IdExpr inputId = new IdExpr("input");
		IdExpr outputId = new IdExpr("output");

		Expr outputExpr = new UnaryExpr(UnaryOp.PRE, inputId);
		Expr notInput = new UnaryExpr(UnaryOp.NOT, inputId);
		outputExpr = new BinaryExpr(outputExpr, BinaryOp.AND, notInput);
		outputExpr = new BinaryExpr(notInput, BinaryOp.ARROW, outputExpr);

		builder.addEquation(new Equation(outputId, outputExpr));
		return builder.build();
	}

	private static Node getMinPosNode() {
		NodeBuilder builder = new NodeBuilder(MIN_POS_NODE_NAME);
		IdExpr a = new IdExpr("a");
		IdExpr b = new IdExpr("b");
		IdExpr ret = new IdExpr("ret");
		builder.addInput(new VarDecl(a.id, NamedType.REAL));
		builder.addInput(new VarDecl(b.id, NamedType.REAL));
		builder.addOutput(new VarDecl(ret.id, NamedType.REAL));

		Expr aLessB = new BinaryExpr(a, BinaryOp.LESSEQUAL, b);
		Expr bNeg = new BinaryExpr(b, BinaryOp.LESSEQUAL, new RealExpr(BigDecimal.ZERO));
		Expr aNeg = new BinaryExpr(a, BinaryOp.LESSEQUAL, new RealExpr(BigDecimal.ZERO));
		Expr ifALessB = new IfThenElseExpr(aLessB, a, b);
		Expr ifBNeg = new IfThenElseExpr(bNeg, a, ifALessB);
		Expr ifANeg = new IfThenElseExpr(aNeg, b, ifBNeg);

		builder.addEquation(new Equation(ret, ifANeg));
		return builder.build();
	}

	public static Expr getTimeConstraint(Set<AgreeVar> events) {

		IdExpr timeId = AgreePatternTranslator.timeExpr;
		Expr preTime = new UnaryExpr(UnaryOp.PRE, timeId);

		Expr nodeCall = new BinaryExpr(timeId, BinaryOp.MINUS, preTime);
		for (AgreeVar eventVar : events) {
			Expr event = new IdExpr(eventVar.id);
			BinaryExpr timeChange = new BinaryExpr(event, BinaryOp.MINUS, timeId);
			Expr preTimeChange = new UnaryExpr(UnaryOp.PRE, timeChange);
			nodeCall = new NodeCallExpr(MIN_POS_NODE_NAME, preTimeChange, nodeCall);
		}

		nodeCall = new BinaryExpr(preTime, BinaryOp.PLUS, nodeCall);
		Expr timeExpr = new BinaryExpr(timeId, BinaryOp.EQUAL, nodeCall);
		timeExpr = new BinaryExpr(new BoolExpr(true), BinaryOp.ARROW, timeExpr);
		Expr timeGrtPreTime = new BinaryExpr(timeId, BinaryOp.GREATER, preTime);
		Expr timeInitZero = new BinaryExpr(timeId, BinaryOp.EQUAL, new RealExpr(BigDecimal.ZERO));
		timeInitZero = new BinaryExpr(timeInitZero, BinaryOp.ARROW, timeGrtPreTime);
		return new BinaryExpr(timeInitZero, BinaryOp.AND, timeExpr);
	}

}
