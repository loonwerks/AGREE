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

import com.rockwellcollins.atc.agree.analysis.Activator;
import com.rockwellcollins.atc.agree.analysis.preferences.PreferenceConstants;

import jkind.api.ui.counterexample.CounterexampleStepLabelProvider;
import jkind.api.ui.counterexample.SignalGroup;
import jkind.lustre.values.RealValue;
import jkind.lustre.values.Value;

public class AgreeCounterexampleStepLabelProvider extends CounterexampleStepLabelProvider {

	private final int step;

	private final boolean displayAsDecimal;

	public AgreeCounterexampleStepLabelProvider(int step) {
		super(step);
		this.step = step;
		displayAsDecimal = Activator.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.PREF_DISPLAY_DECIMAL_CEX);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof SignalGroup) {
			SignalGroup group = (SignalGroup) element;
			if (group.isSingleton()) {
				Value value = group.getSignals().get(0).getValue(step);
				if (value == null) {
					return "";
				} else if (value instanceof RealValue && displayAsDecimal) {
					return ((RealValue) value).value.toTruncatedDecimal(12, "...");
				} else {
					return value.toString();
				}
			}
		}

		return "";
	}

	@Override
	public String getToolTipText(Object element) {
		if (element instanceof SignalGroup) {
			SignalGroup group = (SignalGroup) element;
			if (group.isSingleton()) {
				Value value = group.getSignals().get(0).getValue(step);
				if (value == null) {
					return null;
				} else if (value instanceof RealValue && displayAsDecimal) {
					RealValue rv = (RealValue) value;
					return rv.toString();
				}
			}
		}

		return null;
	}

	@Override
	public int getToolTipDisplayDelayTime(Object object) {
		return 500;
	}

	@Override
	public int getToolTipTimeDisplayed(Object object) {
		return 10000;
	}

}
