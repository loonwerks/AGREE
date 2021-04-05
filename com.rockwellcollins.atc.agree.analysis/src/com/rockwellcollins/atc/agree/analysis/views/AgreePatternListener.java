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
package com.rockwellcollins.atc.agree.analysis.views;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListener;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

public class AgreePatternListener implements IPatternMatchListener {
	private TextConsole console;
	private final Map<String, EObject> refMap;

	public AgreePatternListener(Map<String, EObject> refMap) {
		this.refMap = refMap;
	}

	@Override
	public void connect(TextConsole console) {
		this.console = console;
	}

	@Override
	public void matchFound(PatternMatchEvent event) {
		// remove the brackets
		int offset = event.getOffset() + 1;
		int length = event.getLength() - 2;
		try {
			String name = console.getDocument().get(offset, length);
			EObject ref = findBestReference(name);

			if (ref != null) {
				IHyperlink hyperlink = new AgreeConsoleHyperLink(ref);
				console.addHyperlink(hyperlink, offset, length);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private EObject findBestReference(String refStr) {

		EObject ref = null;
		while (ref == null && refStr != null && !refStr.equals("")) {
			ref = refMap.get(refStr);
			int index = refStr.lastIndexOf(".");
			if (index == -1) {
				break;
			}
			refStr = refStr.substring(0, index);
		}

		return ref;
	}

	@Override
	public String getPattern() {
		return "\\{.*\\}";
	}

	@Override
	public void disconnect() {
	}

	@Override
	public int getCompilerFlags() {
		return 0;
	}

	@Override
	public String getLineQualifier() {
		return null;
	}
}
