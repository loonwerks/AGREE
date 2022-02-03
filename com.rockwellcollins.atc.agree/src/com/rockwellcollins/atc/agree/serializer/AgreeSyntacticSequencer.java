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
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;

public class AgreeSyntacticSequencer extends AbstractAgreeSyntacticSequencer {
	/**
	 * AppliesToKeywords:
	 * 	'applies' 'to'
	 * ;
	 */
	@Override
	protected String getAppliesToKeywordsToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		if (node != null) {
			return super.getAppliesToKeywordsToken(semanticObject, ruleCall, node);
		}
		return "applies to";
	}

	/**
	 * InBindingKeywords:
	 * 	'in' 'binding'
	 * ;
	 */
	@Override
	protected String getInBindingKeywordsToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		if (node != null) {
			return super.getInBindingKeywordsToken(semanticObject, ruleCall, node);
		}
		return "in binding";
	}

	/**
	 * InModesKeywords:
	 * 	'in' 'modes'
	 * ;
	 */
	@Override
	protected String getInModesKeywordsToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		if (node != null) {
			return super.getInModesKeywordsToken(semanticObject, ruleCall, node);
		}
		return "in modes";
	}
}