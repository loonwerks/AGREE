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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import com.rockwellcollins.atc.agree.analysis.AgreeRenaming;

import jkind.api.results.JKindResult;
import jkind.api.results.PropertyResult;
import jkind.results.ValidProperty;

public class AgreeTraceabilityMatrixView extends ViewPart {

	public static final String ID = "com.rockwellcollins.atc.agree.analysis.views.AgreeTraceabilityMatrixView";

	protected Composite composite;
	protected Table table;
	protected boolean showUnused = true;

	@Override
	public void createPartControl(Composite parent) {
		composite = new Composite(parent, SWT.None);
		composite.setLayout(new FillLayout());
	}

	@Override
	public void setFocus() {
		if (table != null) {
			table.setFocus();
		}
	}

	public void setShowUnused(boolean b) {
		showUnused = b;
	}

	public List<String> usedCandidates(JKindResult result) {
		Set<String> reqs = new HashSet<>();
		for (PropertyResult pr : result.getPropertyResults()) {
			if (pr.getProperty() instanceof ValidProperty) {
				ValidProperty vp = (ValidProperty) pr.getProperty();
				Set<String> ivc = vp.getIvc();
				if (ivc != null && !ivc.isEmpty()) {
					reqs.addAll(ivc);
				}
			}
		}
		return reqs.stream().collect(Collectors.toList());
	}

	public List<String> candidates(JKindResult result, AgreeRenaming renaming) {
		if (showUnused) {
			return renaming.getSupportRefStrings().keySet().stream().collect(Collectors.toList());
		} else {
			return usedCandidates(result);
		}
	}

	public String constructFullName(String raw, AgreeRenaming renaming) {
		String name = "";
		if (raw.contains(".")) {
			name = raw.substring(0, raw.indexOf('.') + 1);
		}
		name += renaming.getSupportRefString(raw);
		return name;
	}

	private int index = 0;

	public String constructShortName(String raw, AgreeRenaming renaming) {
		String name = "";
		if (raw.contains(".")) {
			name = raw.substring(0, raw.indexOf('.') + 1);
		}
		name += "R" + index;
		index++;
		return name;
	}

	public void setInput(JKindResult result, AgreeRenaming renaming) {
		for (Control control : composite.getChildren()) {
			control.dispose();
		}
		table = new Table(composite, SWT.FULL_SELECTION);

		List<String> raw = candidates(result, renaming);

		createColumns(raw, renaming);
		createContent(result, raw);
		packColumns();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		composite.layout(true);
	}

	private void createColumns(List<String> raw, AgreeRenaming renaming) {
		TableColumn tc = new TableColumn(table, SWT.CENTER);
		tc.setText("");
		tc.setWidth(10);

		for (String req : raw) {
			tc = new TableColumn(table, SWT.CENTER);
			tc.setWidth(10);
			tc.setText(constructShortName(req, renaming));
			tc.setToolTipText(constructFullName(req, renaming));
		}
	}

	private void createContent(JKindResult result, List<String> raw) {
		for (PropertyResult pr : result.getPropertyResults()) {
			if (pr.getProperty() instanceof ValidProperty) {
				ValidProperty vp = (ValidProperty) pr.getProperty();
				Set<String> ivc = vp.getIvc();
				if (ivc != null && !ivc.isEmpty()) {
					createRow(vp.getName(), ivc, raw);
				}
			}
		}
	}

	private void createRow(String name, Set<String> ivc, List<String> raw) {
		List<String> row = new ArrayList<>();
		row.add(name);
		for (String req : raw) {
			if (ivc.contains(req)) {
				row.add("X");
			} else {
				row.add(" ");
			}
		}

		TableItem ti = new TableItem(table, SWT.None);
		ti.setText(row.toArray(new String[row.size()]));
	}

	private void packColumns() {
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}
	}
}
