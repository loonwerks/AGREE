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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.Bundle;

import com.rockwellcollins.atc.agree.ui.internal.AgreeActivator;

public class AgreeExampleWizardPage extends WizardPage {

	public AgreeExampleWizardPage(String pageName) {
		super(pageName);
	}

	public AgreeExampleWizardPage() {
		super("Examples");
		setTitle("Examples Wizard Page");
		setDescription("Examples Wizard");
	}

	private static final String ATT_NODE = "examples";
	private static final String ATT_NAME = "name";
	private static final String ATT_EXAMPLEURI = "exampleURI";
	private static final String ATT_READMEURI = "readmeURI";
	private static final String ATT_CATEGORY = "category";
	private static final String ATT_PROJECTPATH = "path";
	protected Browser browser;
	protected PluginInfo selectedProject;

	public PluginInfo getSelectedProject() {
		return selectedProject;
	}

	public class TreeContentProvider implements ITreeContentProvider {
		PluginInfo treeContent;

		@Override
		public void dispose() {
			// Nothing to do.
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			treeContent = (PluginInfo) newInput;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof PluginInfo) {
				return ((PluginInfo) parentElement).getNode().toArray();
			} else {
				return null;
			}
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof PluginInfo) {
				return ((PluginInfo) element).getParent();
			}

			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	public class FileLabelProvider extends LabelProvider {
		public FileLabelProvider(Image fileImg, Image categoryImg) {
			super();
		}

		@Override
		public Image getImage(Object element) {
			return null;
		}

		@Override
		public void dispose() {
			super.dispose();
		}

		@Override
		public String getText(Object element) {
			return ((PluginInfo) element).name;
		}
	}

	@Override
	public void performHelp() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getShell(), "org.osate.ui.help_dialog");
	}

	@Override
	public void createControl(Composite parent) {
		try {
			Composite panelChoice = new Composite(parent, SWT.NONE);

			GridLayout layout = new GridLayout();
			layout.marginHeight = 5;
			layout.marginWidth = 5;
			panelChoice.setLayout(layout);

			SashForm sashForm = new SashForm(panelChoice, SWT.HORIZONTAL);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			sashForm.setLayoutData(gd);
			initializeDialogUnits(sashForm);
			getShell().setSize(convertWidthInCharsToPixels(150), getShell().getSize().y);

			PluginInfo root = loadExamples();
			final TreeViewer pickTree = createTree(sashForm);
			pickTree.setLabelProvider(new FileLabelProvider(null, null));
			pickTree.setContentProvider(new TreeContentProvider());
			pickTree.setInput(root);
			pickTree.setComparator(new ViewerComparator());

			pickTree.addSelectionChangedListener(event -> {
				browser.setText("<p>Loading readme</p>");
				// reset an error message
				super.setErrorMessage(null);
				if (isCurrentPage()) {
					getContainer().updateMessage();
				}

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				for (final Iterator<?> iter = selection.iterator(); iter.hasNext();) {
					final Object object = iter.next();
					if (object instanceof PluginInfo) {
						selectedProject = (PluginInfo) object;

						// do not show error message if user clicked on category
						if (selectedProject.category.equalsIgnoreCase(selectedProject.name)) {
							browser.setText("<p>Please select a project</p>");
							return;
						}
						if (selectedProject.readmeURI != null) {
							try {
								File f = new File(selectedProject.readmeURI.getPath());
								if (f.exists()) {
									browser.setUrl(f.toURI().toURL().toString());
								} else {
									throw new IOException("Readme file not found");
								}
							} catch (IllegalArgumentException | IOException e) {
								browser.setText("<p>Failed to load readme</p>");
								setErrorMessage("Failed to load readme");
								catchError(e, e.getMessage());
							}
						} else {
							browser.setText("<p>Failed to load readme</p>");
							setErrorMessage("Failed to load readme");
							catchError(new IOException(), "Readme file not found or missing");
						}
					}
				}
			});

			browser = new Browser(sashForm, SWT.NONE);
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			// layoutData.widthHint = 20 * getShell().getMonitor().getBounds().width;
			// layoutData.widthHint = getShell().computeSize(20 * SWT.DEFAULT, SWT.DEFAULT).x;
			// layoutData.widthHint = convertWidthInCharsToPixels(500);
			layoutData.verticalSpan = 100;
			browser.setLayoutData(layoutData);
			browser.setText(
					"<p>Expand the category on the left and select an example project to import. If the project has a description available, it will be shown here once the project is selected</p>");

			sashForm.setWeights(new int[] { 1, 4 });

			setControl(panelChoice);
		} catch (Exception e) {
			catchError(e, e.getMessage());
		}
	}

	public List<PluginInfo> loadExamplesFromPlugin() {
		List<PluginInfo> result = new ArrayList<PluginInfo>();

		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(AgreeActivator.PLUGIN_ID, ATT_NODE);
		IExtension[] exts = extensionPoint.getExtensions();

		for (int i = 0; i < exts.length; i++) {
			try {
				IConfigurationElement[] configElems = exts[i].getConfigurationElements();

				if (configElems != null) {
					Bundle bundle = Platform.getBundle(exts[i].getContributor().getName());
					for (int j = 0; j < configElems.length; j++) {

						PluginInfo project = new PluginInfo(
								org.eclipse.core.runtime.FileLocator
										.toFileURL(bundle.getEntry(configElems[j].getAttribute(ATT_EXAMPLEURI))),
								org.eclipse.core.runtime.FileLocator
										.toFileURL(bundle.getEntry(combine(configElems[j].getAttribute(ATT_EXAMPLEURI),
												configElems[j].getAttribute(ATT_READMEURI)))),
								configElems[j].getAttribute(ATT_NAME), configElems[j].getAttribute(ATT_CATEGORY),
								exts[i].getContributor().getName());

						if (project != null && project.name != null && project.exampleURI != null) {
							project.exampleS = configElems[j].getAttribute(ATT_EXAMPLEURI);

							// list of projects
							IConfigurationElement[] projectElems = configElems[j].getChildren();
							if (projectElems != null) {
								for (int k = 0; k < projectElems.length; k++) {
									project.addProjectPath(projectElems[k].getAttribute(ATT_PROJECTPATH));
								}
							}
							result.add(project);
						}
					}
				}
			} catch (NullPointerException | InvalidRegistryObjectException | IOException e) {
				catchError(e, e.getMessage());
			}
		}

		return result;
	}

	/**
	 * @since 4.0
	 */
	protected PluginInfo loadExamples() {
		PluginInfo result = new PluginInfo();

		List<PluginInfo> projectInfo = loadExamplesFromPlugin();

		for (PluginInfo p : projectInfo) {
			try {
				File file = null;

				if (Platform.isRunning()) {
					if (p.exampleURI == null) {
						continue;
					}

					file = new File(FileLocator.toFileURL(p.exampleURI).getFile());
				}

				if (file == null) {
					continue; // if example files are not found, there is no point in showing it as an option to import
				}

				// check if node with this category exists
				List<PluginInfo> existingCategories = result.getNode();
				Object[] currentNodeCategory = existingCategories.stream().filter(x -> x.category != null
						&& p.category != null && x.category.compareToIgnoreCase(p.category) == 0).toArray();
				if (currentNodeCategory != null && currentNodeCategory.length > 0) {
					for (int j = 0; j < currentNodeCategory.length; j++) {
						((PluginInfo) currentNodeCategory[j]).addNode(p);
					}
				} else {
					// add category node to root -> add the project under category
					PluginInfo cNode = new PluginInfo(null, null, p.category, p.category, null);
					cNode.addNode(p);
					result.addNode(cNode);
				}
			}
			catch (IOException e) {
				catchError(e, e.getMessage());
			}
		}

		return result;
	}

	/**
	 * @since 4.0
	 */
	protected TreeViewer createTree(Composite parent) {
		GridData compLayout = new GridData(GridData.FILL_BOTH);
		compLayout.heightHint = 200;
		compLayout.widthHint = 200;

		Composite treeComposite = new Composite(parent, SWT.NONE);
		treeComposite.setLayoutData(compLayout);

		GridData dataLayout = new GridData(GridData.FILL_BOTH);
		dataLayout.heightHint = compLayout.heightHint;
		dataLayout.widthHint = compLayout.widthHint;

		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
		Tree tree = new Tree(treeComposite, style);
		tree.setLayoutData(dataLayout);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(false);
		tree.setFont(parent.getFont());

		TreeColumn col = new TreeColumn(tree, SWT.LEFT);

		TreeColumnLayout treeLayout = new TreeColumnLayout();
		treeLayout.setColumnData(col, new ColumnWeightData(dataLayout.widthHint));
		treeComposite.setLayout(treeLayout);

		return new TreeViewer(tree);
	}

	public static String combine(String path1, String path2) {
		File file1 = new File(path1);
		File file2 = new File(file1, path2);
		return file2.getPath();
	}

	protected void catchError(Exception e, String message) {
		IStatus status = new Status(IStatus.ERROR, AgreeActivator.PLUGIN_ID, message, e);
		StatusManager manager = StatusManager.getManager();
		manager.handle(status, StatusManager.LOG);
	}

	/**
	 * The <code>WizardPage</code> implementation of this method
	 * declared on <code>DialogPage</code> updates the container
	 * if this is the current page.
	 */
	@Override
	public void setErrorMessage(String newMessage) {
		super.setErrorMessage(newMessage);
		if (isCurrentPage()) {
			getContainer().updateMessage();
		}
	}

}
