/*
 * Copyright (c) 2022, Collins Aerospace.
 * Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this data,
 * including any software or models in source or binary form, as well as any drawings, specifications,
 * and documentation (collectively &quot;the Data&quot;), to deal in the Data without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Data.
 *
 * THE DATA IS PROVIDED &quot;AS IS&quot;, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
 */

package com.rockwellcollins.atc.agree.ui.wizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.rockwellcollins.atc.agree.ui.internal.AgreeActivator;

/**
 * @since 3.1
 */
public class AgreeExampleWizard extends BasicNewResourceWizard {
	public AgreeExampleWizard() {
		super();
		IDialogSettings workbenchSettings = AgreeActivator.getInstance().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("BasicNewProjectResourceWizard");//$NON-NLS-1$
		if (section == null) {
			section = workbenchSettings.addNewSection("BasicNewProjectResourceWizard");//$NON-NLS-1$
		}

		setDialogSettings(section);
	}

	protected PluginInfo selectedProject;
	protected Browser browser;
	public AgreeExampleWizardPage mainPage;

	// cache of newly-created project
	protected IProject newProject;

	@Override
	public void addPages() {
		super.addPages();
		mainPage = new AgreeExampleWizardPage("wizardAgreeExamplePage"); //$NON-NLS-1$
		mainPage.setTitle("Import AGREE Example Project"); //$NON-NLS-1$
		mainPage.setDescription("Import an example project into your workspace."); //$NON-NLS-1$
		addPage(mainPage);
	}

	@Override
	public boolean performFinish() {
		if (mainPage != null) {
			PluginInfo selected = mainPage.getSelectedProject();
			if (selected != null) {
				if (selected.exampleS != null && selected.projectPath != null) {
					try {
						selected.projectPath.forEach(s -> {
							try {
								importFiles(new File(org.eclipse.core.runtime.FileLocator
										.toFileURL(Platform.getBundle(selected.bundle).getEntry(selected.exampleS))
										.getPath(), s));
							} catch (InvocationTargetException | InterruptedException | IOException e) {
								catchError(e, e.getMessage(), false);
							}
						});
					} catch (Exception e) {
						catchError(e, "Unexpected error occurred. Please try again", false);
					}
				}
			}
		}

		return true;
	}

	protected void importFiles(File projectPath) throws InvocationTargetException, InterruptedException {
		ImportOperation importOperation = new ImportOperation(new Path(Path.ROOT + projectPath.getName()), projectPath,
				FileSystemStructureProvider.INSTANCE, OVERWRITE_ALL_QUERY);
		importOperation.setCreateContainerStructure(false);
		importOperation.run(null);
	}

	protected void catchError(Exception e, String message, Boolean logOnly) {
		IStatus status = new Status(IStatus.ERROR, AgreeActivator.PLUGIN_ID, message, e);
		StatusManager manager = StatusManager.getManager();
		manager.handle(status, logOnly ? StatusManager.LOG : StatusManager.SHOW | StatusManager.LOG);
	}

	protected static final IOverwriteQuery OVERWRITE_ALL_QUERY = pathString -> IOverwriteQuery.ALL;
}
