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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IConfigurationElement;

public class AgreeAutomaterRegistry extends ExtensionRegistry {

	public AgreeAutomaterRegistry() {
		initialize(AGREE_AUTOMATER_EXT_ID);
	}

	public List<AgreeAutomater> getAgreeAutomaters() {

		List<AgreeAutomater> cexExtractors = new ArrayList<>();
		for (Entry<String, ExtensionProxy> entry : extensions.entrySet()) {
			if (entry.getValue() instanceof AgreeAutomater) {
				cexExtractors.add((AgreeAutomater) entry.getValue());
			}
		}
		return cexExtractors;
	}

	@Override
	protected ExtensionProxy createProxy(IConfigurationElement configElem) {
		return new AgreeAutomaterProxy(configElem);
	}

}
