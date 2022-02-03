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
package com.rockwellcollins.atc.agree.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jkind.results.layout.Layout;

public class AgreeLayout implements Layout {
	private List<String> categories = new ArrayList<>();
	private Map<String, String> signalCategories = new HashMap<>();
	private Map<String, List<String>> compInputSignals = new HashMap<>();
	private Map<String, List<String>> compOutputSignals = new HashMap<>();

	public enum SigType {
		INPUT, OUTPUT
	}

	public void addCategory(String category) {
		categories.add(category);
	}

	public void addElement(String category, String signal, SigType type) {
		signalCategories.put(signal, category);
		Map<String, List<String>> compSignals;
		if (type == SigType.INPUT) {
			compSignals = compInputSignals;
		} else {
			assert (type == SigType.OUTPUT);
			compSignals = compOutputSignals;
		}

		List<String> sigList = compSignals.get(category);
		if (sigList == null) {
			sigList = new ArrayList<>();
			compSignals.put(category, sigList);
		}

		sigList.add(signal);
	}

	@Override
	public List<String> getCategories() {
		return categories;
	}

	@Override
	public String getCategory(String signal) {
		for (String cat : categories) {
			if (signal.startsWith(cat + ".")) {
				return cat;
			}
		}
		return "";

	}

	public List<String> getAllInputsFromCategory(String category) {
		return compInputSignals.get(category);
	}

	public List<String> getAllOutputsFromCategory(String category) {
		return compOutputSignals.get(category);
	}

}
