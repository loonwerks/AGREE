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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.AnnexLibrary;
import org.osate.aadl2.AnnexSubclause;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.DefaultAnnexLibrary;
import org.osate.aadl2.DefaultAnnexSubclause;
import org.osate.aadl2.EventDataPort;
import org.osate.aadl2.NamedElement;
import org.osate.annexsupport.AnnexContentAssist;
import org.osate.annexsupport.AnnexUtil;
import org.osate.xtext.aadl2.properties.ui.contentassist.PropertiesProposalProvider;

import com.google.inject.Injector;
import com.rockwellcollins.atc.agree.agree.AgreeContract;
import com.rockwellcollins.atc.agree.agree.AgreeContractLibrary;
import com.rockwellcollins.atc.agree.agree.AgreeLibrary;
import com.rockwellcollins.atc.agree.agree.AgreePackage;
import com.rockwellcollins.atc.agree.agree.Arg;
import com.rockwellcollins.atc.agree.agree.ConstStatement;
import com.rockwellcollins.atc.agree.agree.DoubleDotRef;
import com.rockwellcollins.atc.agree.agree.NamedElmExpr;
import com.rockwellcollins.atc.agree.agree.RecordDef;
import com.rockwellcollins.atc.agree.agree.SelectionExpr;
import com.rockwellcollins.atc.agree.agree.SpecStatement;
import com.rockwellcollins.atc.agree.agree.Type;
import com.rockwellcollins.atc.agree.ui.contentassist.AgreeProposalProvider;
import com.rockwellcollins.atc.agree.ui.internal.AgreeActivator;

public class AgreeAnnexContentAssist implements AnnexContentAssist {
	final private Injector injector = AgreeActivator.getInstance()
			.getInjector(AgreeActivator.COM_ROCKWELLCOLLINS_ATC_AGREE_AGREE);

	private PropertiesProposalProvider propPropProv;
	private EObjectAtOffsetHelper offsetHelper;

	protected PropertiesProposalProvider getLinkingService() {
		if (propPropProv == null) {
			propPropProv = injector.getInstance(AgreeProposalProvider.class);
		}
		return propPropProv;
	}

	protected EObjectAtOffsetHelper getOffsetHelper() {
		if (offsetHelper == null) {
			offsetHelper = injector.getInstance(EObjectAtOffsetHelper.class);
		}
		return offsetHelper;
	}

	@Override
	public List<String> annexCompletionSuggestions(EObject defaultAnnex, int offset) {

		offset = (offset <= 0) ? 0 : offset - 1; // get one character back
		EObjectAtOffsetHelper helper = getOffsetHelper();
		EObject grammerObject = null;
		// EObjectAtOffsetHelper
		if (defaultAnnex instanceof DefaultAnnexLibrary) {
			AnnexLibrary annexLib = ((DefaultAnnexLibrary) defaultAnnex).getParsedAnnexLibrary();
			XtextResource resource = (XtextResource) annexLib.eResource();
			grammerObject = helper.resolveContainedElementAt(resource, offset);
		} else if (defaultAnnex instanceof DefaultAnnexSubclause) {
			AnnexSubclause annexSub = ((DefaultAnnexSubclause) defaultAnnex).getParsedAnnexSubclause();
			XtextResource resource = (XtextResource) annexSub.eResource();
			grammerObject = helper.resolveContainedElementAt(resource, offset);
		}

		List<String> results = new ArrayList<>();
		if (grammerObject instanceof SelectionExpr) {
			results.addAll(getNestedDotIDCandidates((SelectionExpr) grammerObject));
		}

		return results;
	}

	private List<String> getNestedDotIDCandidates(AadlPackage aadlPackage) {

		AgreeContract contract = null;
		List<String> results = new ArrayList<>();
		for (AnnexLibrary annex : AnnexUtil.getAllActualAnnexLibraries(aadlPackage,
				AgreePackage.eINSTANCE.getAgreeContractLibrary())) {
			if (annex instanceof AgreeLibrary) {
				contract = (AgreeContract) ((AgreeContractLibrary) annex).getContract();
			}
		}

		if (contract != null) {
			for (SpecStatement spec : contract.getSpecs()) {
				if (spec instanceof ConstStatement) {
					results.add(((ConstStatement) spec).getName());
				}
			}

		}

		return results;
	}

	private List<String> getNestedDotIDCandidates(NamedElement namedEl) {
		List<String> results = new ArrayList<>();

		List<NamedElement> namedEls = new ArrayList<>();
		if (namedEl instanceof ComponentImplementation) {
			namedEls.addAll(((ComponentImplementation) namedEl).getAllSubcomponents());
		} else if (namedEl instanceof RecordDef) {
			namedEls.addAll(((RecordDef) namedEl).getArgs());
		}
		for (NamedElement el : namedEls) {
			results.add(el.getName());
		}
		return results;
	}

	private List<String> getNestedDotIDCandidates(SelectionExpr id) {

		NamedElement base = ((NamedElmExpr) id).getElm();
		NamedElement namedEl = null;

		if (base instanceof Arg) {
			Type type = ((Arg) base).getType();

			DoubleDotRef elID = ((DoubleDotRef) type);
			namedEl = elID.getElm();
//=======
//			DoubleDotRef elID = ((RecordType) type).getRecord();
//			namedEl = elID.getElm();
//>>>>>>> origin/develop
		} else if (base instanceof DataPort) {
			namedEl = ((DataPort) base).getDataFeatureClassifier();
		} else if (base instanceof EventDataPort) {
			namedEl = ((EventDataPort) base).getDataFeatureClassifier();
		} else if (base instanceof AadlPackage) {
			return getNestedDotIDCandidates((AadlPackage) base);
		} else {
			return new ArrayList<>();
		}

		return getNestedDotIDCandidates(namedEl);
	}

}
