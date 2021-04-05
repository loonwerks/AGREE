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
package com.rockwellcollins.atc.agree.serializer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.scoping.IScope;
import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.Namespace;
import org.osate.aadl2.PropertySet;
import org.osate.aadl2.modelsupport.util.AadlUtil;
import org.osate.xtext.aadl2.serializer.Aadl2CrossReferenceSerializer;

import com.rockwellcollins.atc.agree.agree.DoubleDotRef;
import com.rockwellcollins.atc.agree.agree.NamedElmExpr;
import com.rockwellcollins.atc.agree.agree.NodeEq;

public class AgreeCrossReferenceSerializer extends Aadl2CrossReferenceSerializer {

	private String qualifiedName(Namespace namespace, NamedElement base) {

		String defName = base.getName();

		String prefix = "";

		Namespace defNamespace = AadlUtil.getContainingTopLevelNamespace(base);
		String pkgName = defNamespace.getElementRoot().getName();

		PropertySet propSet = AadlUtil.findImportedPropertySet(pkgName, namespace);
		AadlPackage aadlPackage = AadlUtil.findImportedPackage(pkgName, namespace);

		if (propSet != null || aadlPackage != null) {
			prefix = pkgName + "::";
		}

		return prefix + defName;
	}

	@Override
	@SuppressWarnings("restriction")
	protected String getCrossReferenceNameFromScope(EObject semanticObject, CrossReference crossref, EObject target,
			final IScope scope, org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor errors) {

		Namespace namespace = AadlUtil.getContainingTopLevelNamespace(semanticObject);

		if (semanticObject instanceof NamedElmExpr) {
			NamedElement ne = ((NamedElmExpr) semanticObject).getElm();
			return qualifiedName(namespace, ne);
		} else if (semanticObject instanceof DoubleDotRef) {
			NamedElement ne = ((DoubleDotRef) semanticObject).getElm();
			return qualifiedName(namespace, ne);
		} else if (semanticObject instanceof NodeEq) {
			NodeEq nodeEq = (NodeEq) semanticObject;
			assert (nodeEq.getLhs().size() == 1);
			return nodeEq.getLhs().get(0).getName();
		} else {
			return super.getCrossReferenceNameFromScope(semanticObject, crossref, target, scope, errors);
		}
	}
}
