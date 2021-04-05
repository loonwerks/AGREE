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
package com.rockwellcollins.atc.agree.analysis.extentions;

import org.eclipse.core.runtime.IConfigurationElement;

import com.rockwellcollins.atc.agree.analysis.AgreeLayout;
import com.rockwellcollins.atc.agree.analysis.AgreeRenaming;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeProgram;

import jkind.api.results.AnalysisResult;

public class AgreeAutomaterProxy extends ExtensionProxy implements AgreeAutomater {

	private AgreeAutomater extractor;

	protected AgreeAutomaterProxy(IConfigurationElement configElem) {
		super(configElem);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AgreeProgram transform(AgreeProgram program) {
		AgreeAutomater extractor = getAgreeAutomater();

		if (extractor != null) {
			return extractor.transform(program);
		}
		return null;
	}

	private AgreeAutomater getAgreeAutomater() {
		if (extractor != null) {
			return extractor;
		}
		try {
			extractor = (AgreeAutomater) configElem.createExecutableExtension(ATT_CLASS);
		} catch (Exception e) {
			System.err.println("error instantiating agree automater in plugin "
					+ configElem.getDeclaringExtension().getContributor().getName());
		}
		return extractor;
	}

	@Override
	public AgreeRenaming rename(AgreeRenaming renaming) {
		AgreeAutomater extractor = getAgreeAutomater();

		if (extractor != null) {
			return extractor.rename(renaming);
		}
		return null;
	}

	@Override
	public AnalysisResult transformResult(AnalysisResult res) {
		AgreeAutomater extractor = getAgreeAutomater();

		if (extractor != null) {
			return extractor.transformResult(res);
		}
		return null;
	}

	@Override
	public AgreeLayout transformLayout(AgreeLayout layout) {
		AgreeAutomater extractor = getAgreeAutomater();

		if (extractor != null) {
			return extractor.transformLayout(layout);
		}
		return null;
	}

}
