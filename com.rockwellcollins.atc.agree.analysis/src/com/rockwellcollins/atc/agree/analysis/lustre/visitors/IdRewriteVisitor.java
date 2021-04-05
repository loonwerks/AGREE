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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rockwellcollins.atc.agree.analysis.AgreeException;

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

public class IdRewriteVisitor implements ExprVisitor<Expr> {

	private final IdRewriter rewriter;

	public IdRewriteVisitor(IdRewriter rewriter) {
		this.rewriter = rewriter;
	}

	@Override
	public Expr visit(ArrayAccessExpr e) {
		throw new AgreeException("We do not support array expressions");
	}

	@Override
	public Expr visit(ArrayExpr e) {
		throw new AgreeException("We do not support array expressions");
	}

	@Override
	public Expr visit(ArrayUpdateExpr e) {
		throw new AgreeException("We do not support array expressions");
	}

	@Override
	public Expr visit(BinaryExpr e) {
		return new BinaryExpr(e.left.accept(this), e.op, e.right.accept(this));
	}

	@Override
	public Expr visit(BoolExpr e) {
		return new BoolExpr(e.value);
	}

	@Override
	public Expr visit(CastExpr e) {
		return new CastExpr(e.type, e.expr.accept(this));
	}

	@Override
	public Expr visit(CondactExpr e) {
		return new CondactExpr(e.clock.accept(this), (NodeCallExpr) e.call.accept(this), acceptList(e.args));
	}

	@Override
	public Expr visit(IdExpr e) {
		return rewriter.rewrite(e);
	}

	@Override
	public Expr visit(IfThenElseExpr e) {
		return new IfThenElseExpr(e.cond.accept(this), e.thenExpr.accept(this), e.elseExpr.accept(this));
	}

	@Override
	public Expr visit(IntExpr e) {
		return new IntExpr(e.value);
	}

	@Override
	public Expr visit(FunctionCallExpr e) {
		return new FunctionCallExpr(e.function, acceptList(e.args));
	}

	@Override
	public Expr visit(NodeCallExpr e) {
		return new NodeCallExpr(e.node, acceptList(e.args));
	}

	@Override
	public Expr visit(RealExpr e) {
		return new RealExpr(e.value);
	}

	@Override
	public Expr visit(RecordAccessExpr e) {
		return new RecordAccessExpr(e.record.accept(this), e.field);
	}

	@Override
	public Expr visit(RecordExpr e) {
		Map<String, Expr> newFields = new HashMap<>();
		for (Entry<String, Expr> entry : e.fields.entrySet()) {
			newFields.put(entry.getKey(), entry.getValue().accept(this));
		}
		return new RecordExpr(e.id, newFields);
	}

	@Override
	public Expr visit(RecordUpdateExpr e) {
		return new RecordUpdateExpr(e.record.accept(this), e.field, e.value.accept(this));
	}

	@Override
	public Expr visit(TupleExpr e) {
		throw new AgreeException("We do not currently support typles");
	}

	@Override
	public Expr visit(UnaryExpr e) {
		return new UnaryExpr(e.op, e.expr.accept(this));
	}

	private List<Expr> acceptList(List<Expr> exprs) {
		List<Expr> result = new ArrayList<>();

		for (Expr expr : exprs) {
			result.add(expr.accept(this));
		}

		return result;
	}

}
