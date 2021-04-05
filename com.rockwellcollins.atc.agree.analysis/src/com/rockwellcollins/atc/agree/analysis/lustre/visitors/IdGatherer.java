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
package com.rockwellcollins.atc.agree.analysis.lustre.visitors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import jkind.lustre.ArrayAccessExpr;
import jkind.lustre.ArrayExpr;
import jkind.lustre.ArrayUpdateExpr;
import jkind.lustre.BinaryExpr;
import jkind.lustre.BoolExpr;
import jkind.lustre.CastExpr;
import jkind.lustre.CondactExpr;
import jkind.lustre.Expr;
import jkind.lustre.FunctionCallExpr;
import jkind.lustre.IdExpr;
import jkind.lustre.IfThenElseExpr;
import jkind.lustre.IntExpr;
import jkind.lustre.NodeCallExpr;
import jkind.lustre.RealExpr;
import jkind.lustre.RecordAccessExpr;
import jkind.lustre.RecordExpr;
import jkind.lustre.RecordUpdateExpr;
import jkind.lustre.TupleExpr;
import jkind.lustre.UnaryExpr;
import jkind.lustre.visitors.ExprVisitor;

public class IdGatherer implements ExprVisitor<Set<String>> {

	@Override
	public Set<String> visit(ArrayAccessExpr e) {
		Set<String> ids = new HashSet<>();
		ids.addAll(e.array.accept(this));
		ids.addAll(e.index.accept(this));
		return ids;
	}

	@Override
	public Set<String> visit(ArrayExpr e) {
		Set<String> ids = new HashSet<>();
		for (Expr sub : e.elements) {
			ids.addAll(sub.accept(this));
		}
		return ids;
	}

	@Override
	public Set<String> visit(ArrayUpdateExpr e) {
		Set<String> ids = new HashSet<>();
		ids.addAll(e.array.accept(this));
		ids.addAll(e.index.accept(this));
		ids.addAll(e.value.accept(this));
		return ids;
	}

	@Override
	public Set<String> visit(BinaryExpr e) {
		Set<String> ids = new HashSet<>();
		ids.addAll(e.left.accept(this));
		ids.addAll(e.right.accept(this));
		return ids;
	}

	@Override
	public Set<String> visit(BoolExpr e) {
		return Collections.emptySet();
	}

	@Override
	public Set<String> visit(CastExpr e) {
		Set<String> ids = new HashSet<>();
		ids.addAll(e.expr.accept(this));
		return ids;
	}

	@Override
	public Set<String> visit(CondactExpr e) {
		Set<String> ids = new HashSet<>();
		for (Expr expr : e.args) {
			ids.addAll(expr.accept(this));
		}
		ids.addAll(e.call.accept(this));
		ids.addAll(e.clock.accept(this));
		return ids;
	}

	@Override
	public Set<String> visit(IdExpr e) {
		return Collections.singleton(e.id);
	}

	@Override
	public Set<String> visit(IfThenElseExpr e) {
		Set<String> ids = new HashSet<>();
		ids.addAll(e.cond.accept(this));
		ids.addAll(e.thenExpr.accept(this));
		ids.addAll(e.elseExpr.accept(this));
		return ids;
	}

	@Override
	public Set<String> visit(IntExpr e) {
		return Collections.emptySet();
	}

	@Override
	public Set<String> visit(FunctionCallExpr e) {
		Set<String> ids = new HashSet<>();
		for (Expr expr : e.args) {
			ids.addAll(expr.accept(this));
		}
		return ids;
	}

	@Override
	public Set<String> visit(NodeCallExpr e) {
		Set<String> ids = new HashSet<>();
		for (Expr expr : e.args) {
			ids.addAll(expr.accept(this));
		}
		return ids;
	}

	@Override
	public Set<String> visit(RealExpr e) {
		return Collections.emptySet();
	}

	@Override
	public Set<String> visit(RecordAccessExpr e) {
		Set<String> ids = new HashSet<>();
		ids.addAll(e.record.accept(this));
		return ids;
	}

	@Override
	public Set<String> visit(RecordExpr e) {
		Set<String> ids = new HashSet<>();
		for (Entry<String, Expr> entry : e.fields.entrySet()) {
			ids.addAll(entry.getValue().accept(this));
		}
		return ids;
	}

	@Override
	public Set<String> visit(RecordUpdateExpr e) {
		Set<String> ids = new HashSet<>();
		ids.addAll(e.record.accept(this));
		ids.addAll(e.value.accept(this));
		return ids;
	}

	@Override
	public Set<String> visit(TupleExpr e) {
		Set<String> ids = new HashSet<>();
		for (Expr expr : e.elements) {
			ids.addAll(expr.accept(this));
		}
		return ids;
	}

	@Override
	public Set<String> visit(UnaryExpr e) {
		return e.expr.accept(this);
	}

}
