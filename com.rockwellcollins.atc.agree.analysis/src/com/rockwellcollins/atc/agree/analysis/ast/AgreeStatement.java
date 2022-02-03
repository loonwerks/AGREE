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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import com.rockwellcollins.atc.agree.analysis.AgreeException;

//An AgreeStatement is used as a wrapper for assumptions, guarantees, and other things
//It implements EObject so it can be passed around as if it was part of the Ecore AST

import com.rockwellcollins.atc.agree.analysis.ast.visitors.AgreeASTVisitor;
import com.rockwellcollins.atc.agree.analysis.realtime.AgreePattern;

import jkind.lustre.Expr;

public class AgreeStatement implements AgreeASTElement, EObject {
	public final String string;
	public Expr expr;
	public final EObject reference;

	public AgreeStatement(String string, Expr expr, EObject reference) {
		this.string = string;
		this.expr = expr;
		this.reference = reference;
		if (expr == null && !(this instanceof AgreePattern)) {
			throw new AgreeException("AgreeStatement created with null expression");
		}
	}

	@Override
	public <T> T accept(AgreeASTVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public EList<Adapter> eAdapters() {
		return reference.eAdapters();
	}

	@Override
	public boolean eDeliver() {
		return reference.eDeliver();
	}

	@Override
	public void eSetDeliver(boolean deliver) {
		reference.eSetDeliver(deliver);
	}

	@Override
	public void eNotify(Notification notification) {
		reference.eNotify(notification);
	}

	@Override
	public EClass eClass() {
		return reference.eClass();
	}

	@Override
	public Resource eResource() {
		return reference.eResource();
	}

	@Override
	public EObject eContainer() {
		if (reference != null) {
			return reference.eContainer();
		} else {
			return null;
		}
	}

	@Override
	public EStructuralFeature eContainingFeature() {
		return reference.eContainingFeature();
	}

	@Override
	public EReference eContainmentFeature() {
		return reference.eContainmentFeature();
	}

	@Override
	public EList<EObject> eContents() {
		return reference.eContents();
	}

	@Override
	public TreeIterator<EObject> eAllContents() {
		return reference.eAllContents();
	}

	@Override
	public boolean eIsProxy() {
		return reference.eIsProxy();
	}

	@Override
	public EList<EObject> eCrossReferences() {
		return reference.eCrossReferences();
	}

	@Override
	public Object eGet(EStructuralFeature feature) {
		return reference.eGet(feature);
	}

	@Override
	public Object eGet(EStructuralFeature feature, boolean resolve) {
		return reference.eGet(feature, resolve);
	}

	@Override
	public void eSet(EStructuralFeature feature, Object newValue) {
		reference.eSet(feature, newValue);
	}

	@Override
	public boolean eIsSet(EStructuralFeature feature) {
		return reference.eIsSet(feature);
	}

	@Override
	public void eUnset(EStructuralFeature feature) {
		reference.eUnset(feature);
	}

	@Override
	public Object eInvoke(EOperation operation, EList<?> arguments) throws InvocationTargetException {
		return reference.eInvoke(operation, arguments);
	}

	@Override
	public String toString() {
		return string;
	}

}
