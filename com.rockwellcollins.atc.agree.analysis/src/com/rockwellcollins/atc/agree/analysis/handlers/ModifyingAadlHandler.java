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
package com.rockwellcollins.atc.agree.analysis.handlers;

import java.io.IOException;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.ui.editor.XtextEditor;

public abstract class ModifyingAadlHandler extends AadlHandler {
	@Override
	protected WorkspaceJob getWorkspaceJob(XtextEditor xtextEditor, URI uri) {
		return new WorkspaceJob(getJobName()) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				// modification may update text editors so we need to run in the SWT thread
				IStatus[] status = new IStatus[1];
				getWindow().getShell().getDisplay().syncExec(() -> {
					if (xtextEditor == null) {
						final ResourceSet rs = new ResourceSetImpl();
						status[0] = getWorker(uri, monitor).apply(rs);

						// Save the model
						final EObject eobj = rs.getEObject(uri, true);
						if (eobj != null) {
							final Resource resource = eobj.eResource();
							if (resource != null) {
								try {
									resource.save(SaveOptions.newBuilder().format().getOptions().toOptionsMap());
								} catch (final IOException e) {
									throw new RuntimeException(e);
								}
							}
						}
					} else {
						status[0] = xtextEditor.getDocument().modify(getUnitOfWork(uri, monitor));
					}
				});
				return status[0];
			};
		};
	}
}
