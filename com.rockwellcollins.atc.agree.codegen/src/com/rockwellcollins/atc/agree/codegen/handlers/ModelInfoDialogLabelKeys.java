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
package com.rockwellcollins.atc.agree.codegen.handlers;

import org.eclipse.jface.dialogs.IDialogLabelKeys;

public interface ModelInfoDialogLabelKeys extends IDialogLabelKeys {

	/**
	 * The key used to retrieve the label for Export Contracts buttons. Clients should use the pattern
	 * <code>JFaceResources.getString(IDialogLabelKeys.EXPORT_LABEL_KEY)</code> to retrieve the label
	 * dynamically when using multiple locales.
	 *
	 * @since 3.7
	 */
	public String EXPORT_LABEL_KEY = "Export Contracts"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the label for Generate Implementation buttons.
	 * Clients should use the pattern
	 * <code>JFaceResources.getString(IDialogLabelKeys.GENERATE_LABEL_KEY)</code>
	 * to retrieve the label dynamically when using multiple locales.
	 * @since 3.7
	 */
	public String GEN_IMPL_LABEL_KEY = "Generate Implementation"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the label for Generate Verification buttons.
	 * Clients should use the pattern
	 * <code>JFaceResources.getString(IDialogLabelKeys.UPDATE_LABEL_KEY)</code>
	 * to retrieve the label dynamically when using multiple locales.
	 * @since 3.7
	 */
	public String GEN_VERIFICATION_LABEL_KEY = "Generate Verification"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the label for VERIFY buttons.
	 * Clients should use the pattern
	 * <code>JFaceResources.getString(IDialogLabelKeys.VERIFY_LABEL_KEY)</code>
	 * to retrieve the label dynamically when using multiple locales.
	 * @since 3.7
	 */
	public String VERIFY_LABEL_KEY = "Verify Subsystem"; //$NON-NLS-1$

}
