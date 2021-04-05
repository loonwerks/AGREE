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
package com.rockwellcollins.atc.agree.parsing;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.linking.ILinkingService;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.osate.annexsupport.AnnexLinkingService;

import com.google.inject.Injector;
import com.rockwellcollins.atc.agree.linking.AgreeLinkingService;
import com.rockwellcollins.atc.agree.ui.internal.AgreeActivator;

// Based on EMV2AnnexLinkingService from Error Model annex
public class AgreeAnnexLinkingService implements AnnexLinkingService {
	final private Injector injector = AgreeActivator.getInstance()
			.getInjector(AgreeActivator.COM_ROCKWELLCOLLINS_ATC_AGREE_AGREE);

	private ILinkingService linkingService;

	private IQualifiedNameProvider nameProvider;

	protected ILinkingService getLinkingService() {
		if (linkingService == null) {
			linkingService = injector.getInstance(AgreeLinkingService.class);
		}
		return linkingService;
	}

	protected IQualifiedNameProvider getNameProvider() {
		if (nameProvider == null) {
			nameProvider = injector.getInstance(IQualifiedNameProvider.class);
		}
		return nameProvider;
	}

	@Override
	public List<EObject> resolveAnnexReference(String annexName, EObject context, EReference reference, INode node) {
		return getLinkingService().getLinkedObjects(context, reference, node);
	}

	@Override
	public QualifiedName getFullyQualifiedName(final EObject obj) {
		return getNameProvider().getFullyQualifiedName(obj);
	}
}
