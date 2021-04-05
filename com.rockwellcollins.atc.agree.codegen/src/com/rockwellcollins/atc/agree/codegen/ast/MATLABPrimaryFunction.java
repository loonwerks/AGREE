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
package com.rockwellcollins.atc.agree.codegen.ast;

import java.util.ArrayList;
import java.util.List;

import com.rockwellcollins.atc.agree.codegen.ast.expr.MATLABIdExpr;
import com.rockwellcollins.atc.agree.codegen.visitors.MATLABAstVisitor;

import jkind.Assert;

public class MATLABPrimaryFunction extends MATLABFunction {

	public List<MATLABIdExpr> inputs = new ArrayList<>();
	public List<MATLABStatement> statements = new ArrayList<>();
	public List<MATLABFunction> functions = new ArrayList<>();
	public List<MATLABPersistentVarDecl> persistentVarDecl = new ArrayList<>();
	public List<MATLABPort> ports = new ArrayList<>();

	public MATLABPrimaryFunction(String name, List<MATLABIdExpr> inputs,
			List<MATLABPersistentVarDecl> persistentVarDecl, List<MATLABStatement> statements,
			List<MATLABFunction> functions, List<MATLABPort> ports) {
		Assert.isNotNull(name);
		Assert.isNotNull(inputs);
		Assert.isNotNull(persistentVarDecl);
		Assert.isNotNull(statements);
		Assert.isNotNull(functions);
		Assert.isNotNull(ports);
		this.name = name;
		this.inputs = inputs;
		this.persistentVarDecl = persistentVarDecl;
		this.statements = statements;
		this.functions = functions;
		this.ports = ports;
	}

	@Override
	public <T, S extends T> T accept(MATLABAstVisitor<T, S> visitor) {
		return visitor.visit(this);
	}

}
