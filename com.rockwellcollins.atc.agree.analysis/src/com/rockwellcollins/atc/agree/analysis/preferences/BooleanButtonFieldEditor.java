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
package com.rockwellcollins.atc.agree.analysis.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * Based on Eclipse's StringButtonFieldEditor
 */
public class BooleanButtonFieldEditor extends BooleanFieldEditor {
	protected Button button;
	protected String buttonText;
	protected Runnable buttonPressed;

	public BooleanButtonFieldEditor(String name, String labelText, String buttonText, Runnable buttonPressed,
			Composite parent) {
		init(name, labelText);
		this.buttonText = buttonText;
		this.buttonPressed = buttonPressed;
		createControl(parent);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		super.adjustForNumColumns(numColumns - 1);
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		super.doFillIntoGrid(parent, numColumns - 1);
		Button button = getButton(parent);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.END;
		button.setLayoutData(gd);
	}

	protected Button getButton(Composite parent) {
		if (button == null) {
			button = new Button(parent, SWT.PUSH);
			button.setText(buttonText);
			button.setFont(parent.getFont());
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					buttonPressed.run();
				}
			});
		} else {
			checkParent(button, parent);
		}
		return button;
	}

	@Override
	public int getNumberOfControls() {
		return 3;
	}

	protected Shell getShell() {
		if (button == null) {
			return null;
		}
		return button.getShell();
	}

	@Override
	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		if (button != null) {
			button.setEnabled(enabled);
		}
	}
}
