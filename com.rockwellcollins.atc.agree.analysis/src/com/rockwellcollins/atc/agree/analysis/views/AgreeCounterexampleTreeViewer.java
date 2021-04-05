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

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jkind.api.ui.counterexample.CounterexampleContentProvider;
import jkind.api.ui.counterexample.CounterexampleNameLabelProvider;
import jkind.results.Counterexample;
import jkind.results.layout.Layout;

public class AgreeCounterexampleTreeViewer {

	protected TreeViewer treeViewer;
	protected final Composite composite;

	public AgreeCounterexampleTreeViewer(Composite parent) {
		composite = new Composite(parent, SWT.None);
	}

	public void setFocus() {
		if (treeViewer != null) {
			treeViewer.getTree().setFocus();
		}
	}

	public void setInput(Counterexample cex, Layout layout) {
		initializeTreeViewer(layout);
		createColumns(cex);
		treeViewer.setInput(cex);
		composite.layout(true);
	}

	private void initializeTreeViewer(Layout layout) {
		if (treeViewer != null) {
			treeViewer.getControl().dispose();
		}
		treeViewer = new TreeViewer(composite, SWT.MULTI | SWT.FULL_SELECTION);

		treeViewer.setContentProvider(new CounterexampleContentProvider(layout));
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
	}

	private void createColumns(Counterexample cex) {
		TreeColumnLayout layout = new TreeColumnLayout();
		composite.setLayout(layout);
		createNameColumn(layout);
		for (int i = 0; i < cex.getLength(); i++) {
			createStepColumn(i, layout);
		}
	}

	private void createNameColumn(TreeColumnLayout layout) {
		TreeViewerColumn nameCol = new TreeViewerColumn(treeViewer, SWT.None);
		nameCol.getColumn().setText("Name");
		nameCol.setLabelProvider(new CounterexampleNameLabelProvider());
		layout.setColumnData(nameCol.getColumn(), new ColumnWeightData(10, 200));
	}

	private void createStepColumn(int i, TreeColumnLayout layout) {
		TreeViewerColumn stepCol = new TreeViewerColumn(treeViewer, SWT.None);
		stepCol.getColumn().setText("Step " + (i + 1));
		ColumnViewerToolTipSupport.enableFor(stepCol.getViewer());
		stepCol.setLabelProvider(new AgreeCounterexampleStepLabelProvider(i));
		layout.setColumnData(stepCol.getColumn(), new ColumnWeightData(1, 50));
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}
}
