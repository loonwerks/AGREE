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

import java.util.Collections;
import java.util.Map;
import java.util.Stack;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.ui.editor.GlobalURIEditorOpener;

import com.rockwellcollins.atc.agree.analysis.AgreeUtils;

import jkind.api.ui.counterexample.SignalGroup;
import jkind.results.Counterexample;
import jkind.results.layout.Layout;

public class AgreeCounterexampleView extends ViewPart {
	public static final String ID = "com.rockwellcollins.atc.agree.analysis.views.agreeCounterexampleView";
	private static final GlobalURIEditorOpener globalURIEditorOpener = AgreeUtils.getGlobalURIEditorOpener();

	private AgreeCounterexampleTreeViewer tree;
	private Map<String, EObject> refMap;

	@Override
	public void createPartControl(Composite parent) {
		tree = new AgreeCounterexampleTreeViewer(parent);
	}

	@Override
	public void setFocus() {
		tree.setFocus();
	}

	public void setInput(Counterexample cex, Layout layout, Map<String, EObject> refMap) {
		tree.setInput(cex, layout, refMap);
		this.refMap = refMap;
		tree.getTreeViewer().addDoubleClickListener(event -> {
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (!selection.isEmpty() && selection.getFirstElement() instanceof SignalGroup) {
					open((SignalGroup) selection.getFirstElement());
				}
			}
		});
	}

	/**
	 * @since 2.8
	 */
	public void setInput(Counterexample cex, Layout layout) {
		setInput(cex, layout, Collections.<String, EObject> emptyMap());
	}

	private void open(SignalGroup group) {
		if (!group.isSingleton()) {
			return;
		}

		EObject e = findRelevantObject(group);
		if (e != null) {
			globalURIEditorOpener.open(EcoreUtil.getURI(e), true);
		}
	}

	private EObject findRelevantObject(SignalGroup curr) {
		Stack<String> names = new Stack<>();
		while (curr != null) {
			names.add(curr.getName());
			curr = curr.getParent();
		}

		EObject result = null;
		String name = "";
		while (!names.isEmpty()) {
			String next = names.pop();
			name += divider(name, next) + next;
			if (refMap.containsKey(name)) {
				result = refMap.get(name);
			}
		}
		return result;
	}

	private String divider(String front, String back) {
		if (front.isEmpty() || back.isEmpty() || back.startsWith("[")) {
			return "";
		}
		return ".";
	}
}
