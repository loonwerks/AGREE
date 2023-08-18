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
package com.rockwellcollins.atc.agree.validation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.rockwellcollins.atc.agree.agree.Abstraction;
import com.rockwellcollins.atc.agree.agree.BinaryExpr;
import com.rockwellcollins.atc.agree.agree.CallExpr;
import com.rockwellcollins.atc.agree.agree.DoubleDotRef;
import com.rockwellcollins.atc.agree.agree.Expr;
import com.rockwellcollins.atc.agree.agree.FnDef;
import com.rockwellcollins.atc.agree.agree.IfThenElseExpr;
import com.rockwellcollins.atc.agree.agree.NodeBodyExpr;
import com.rockwellcollins.atc.agree.agree.NodeDef;
import com.rockwellcollins.atc.agree.agree.NodeEq;
import com.rockwellcollins.atc.agree.agree.NodeLemma;
import com.rockwellcollins.atc.agree.agree.NodeStmt;
import com.rockwellcollins.atc.agree.agree.PreExpr;
import com.rockwellcollins.atc.agree.agree.PrevExpr;
import com.rockwellcollins.atc.agree.agree.UnaryExpr;
import com.rockwellcollins.atc.agree.agree.util.AgreeSwitch;


public class CallRecursionHelper extends AgreeSwitch<Expr> {

	public LinkedList<Abstraction> visited;
	public List<LinkedList<Abstraction>> loops;

	public void doSwitchPreserveVisited(Expr expr) {
		LinkedList<Abstraction> copyOfVisited = new LinkedList<>(visited);
		doSwitch(expr);
		visited = copyOfVisited;
	}

	public CallRecursionHelper() {
		visited = new LinkedList<>();
		loops = new ArrayList<>();
	}

	@Override
	public Expr caseBinaryExpr(BinaryExpr object) {
		doSwitchPreserveVisited(object.getLeft());
		doSwitchPreserveVisited(object.getRight());
		return null;
	}

	@Override
	public Expr caseUnaryExpr(UnaryExpr object) {
		doSwitchPreserveVisited(object.getExpr());
		return null;
	}

	@Override
	public Expr caseIfThenElseExpr(IfThenElseExpr object) {
		doSwitchPreserveVisited(object.getC());
		doSwitchPreserveVisited(object.getB());
		doSwitchPreserveVisited(object.getA());
		return null;
	}

	@Override
	public Expr casePrevExpr(PrevExpr object) {
		doSwitchPreserveVisited(object.getDelay());
		doSwitchPreserveVisited(object.getInit());
		return null;
	}

	@Override
	public Expr casePreExpr(PreExpr object) {
		doSwitchPreserveVisited(object.getExpr());
		return null;
	}

	@Override
	public Expr caseNodeBodyExpr(NodeBodyExpr object) {

		for (NodeStmt stmt : object.getStmts()) {
			doSwitch(stmt);
		}
		return null;
	}

	@Override
	public Expr caseNodeEq(NodeEq object) {
		doSwitchPreserveVisited(object.getExpr());
		return null;
	}

	@Override
	public Expr caseNodeLemma(NodeLemma object) {
		doSwitchPreserveVisited(object.getExpr());
		return null;
	}

	@Override
	public Expr caseNodeDef(NodeDef object) {

		if (visited.contains(object)) {
			LinkedList<Abstraction> loop = new LinkedList<>(visited);
			loop.push(object);
			loops.add(loop);
		} else {
			visited.push(object);
			doSwitch(object.getNodeBody());
		}

		return null;
	}

	@Override
	public Expr caseFnDef(FnDef object) {

		if (visited.contains(object)) {
			LinkedList<Abstraction> loop = new LinkedList<>(visited);
			loop.push(object);
			loops.add(loop);
		} else {
			visited.push(object);
			doSwitchPreserveVisited(object.getExpr());
		}
		return null;
	}

	@Override
	public Expr caseCallExpr(CallExpr object) {
		DoubleDotRef id = object.getRef();

		Abstraction callDef = null;

		try {
			callDef = (Abstraction) id.getElm();
		} catch (ClassCastException e) {
			return null;
		}

		doSwitch(callDef);
		return null;
	}

}
