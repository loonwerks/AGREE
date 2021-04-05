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
package com.rockwellcollins.atc.tcg.views;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import jkind.api.ui.results.AnalysisResultColumnViewer;

public class TcgResultTree extends AnalysisResultColumnViewer {
	private TreeViewer treeViewer;

	public TcgResultTree(Composite parent) {
		super(parent);
	}

	@Override
	protected ColumnViewer createViewer() {
		treeViewer = new TreeViewer(composite, SWT.FULL_SELECTION);
		treeViewer.getTree().setHeaderVisible(true);
		createColumns();
		return treeViewer;
	}

	private void createColumns() {
		TreeViewerColumn propertyColumn = new TreeViewerColumn(treeViewer, SWT.None);
		propertyColumn.getColumn().setText("Test Spec");
		propertyColumn.getColumn().setWidth(400);
//		propertyColumn.setLabelProvider(new AnalysisResultLabelProvider(Column.PROPERTY, treeViewer));
		propertyColumn.setLabelProvider(new TestCaseGeneratorResultLabelProvider(Column.PROPERTY, treeViewer));

		TreeViewerColumn resultColumn = new TreeViewerColumn(treeViewer, SWT.None);
		resultColumn.getColumn().setText("Result");
//		resultColumn.setLabelProvider(new AnalysisResultLabelProvider(Column.RESULT));
		resultColumn.setLabelProvider(new TestCaseGeneratorResultLabelProvider(Column.RESULT));

		TreeColumnLayout layout = new TreeColumnLayout();
		composite.setLayout(layout);
		layout.setColumnData(propertyColumn.getColumn(), new ColumnWeightData(2));
		layout.setColumnData(resultColumn.getColumn(), new ColumnWeightData(1));
	}

	@Override
	public TreeViewer getViewer() {
		return treeViewer;
	}
}
