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
package com.rockwellcollins.atc.agree.analysis.ast;

import org.eclipse.emf.ecore.EObject;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureInstance;

import com.rockwellcollins.atc.agree.analysis.ast.visitors.AgreeASTVisitor;

import jkind.lustre.Type;
import jkind.lustre.VarDecl;

public class AgreeVar extends VarDecl implements AgreeASTElement {

	public final EObject reference;
	public final ComponentInstance compInst;
	public final FeatureInstance featInst;

	public AgreeVar(String name, Type type, EObject reference, ComponentInstance compInst, FeatureInstance featInst) {
		super(name, type);
		this.reference = reference;
		this.compInst = compInst;
		this.featInst = featInst;
	}

	public AgreeVar(String name, Type type, EObject reference, ComponentInstance compInst) {
		this(name, type, reference, compInst, null);
	}

	public AgreeVar(String name, Type type, EObject reference) {
		this(name, type, reference, null, null);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String) {
			return id.equals(obj);
		}

		if (!(obj instanceof AgreeVar)) {
			return false;
		}
		AgreeVar otherVar = (AgreeVar) obj;
		return otherVar.compInst == compInst && otherVar.id.equals(id);
	}

	@Override
	public <T> T accept(AgreeASTVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		if (compInst != null) {
			return compInst.getInstanceObjectPath() + "." + id;
		}
		return id;
	}

}
