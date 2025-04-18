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

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.osate.aadl2.Element;

import com.rockwellcollins.atc.agree.analysis.Activator;
import com.rockwellcollins.atc.agree.analysis.handlers.AadlHandler;
import com.rockwellcollins.atc.agree.analysis.handlers.RerunHandler;
import com.rockwellcollins.atc.agree.analysis.handlers.TerminateHandler;
import com.rockwellcollins.atc.agree.analysis.handlers.VerifyHandler;
import com.rockwellcollins.atc.agree.analysis.preferences.PreferenceConstants;
import com.rockwellcollins.atc.agree.analysis.saving.AgreeFileUtil;

import jkind.api.results.AnalysisResult;
import jkind.api.results.CompositeAnalysisResult;
import jkind.api.ui.results.AnalysisResultTree;

public class AgreeResultsView extends ViewPart {
	public static final String ID = "com.rockwellcollins.atc.agree.analysis.views.agreeResultsView";

	private AnalysisResultTree tree;
	private AgreeMenuListener menuListener;

	private IHandlerActivation rerunActivation;
	private IHandlerActivation terminateActivation;
	private IHandlerActivation terminateAllActivation;

	@Override
	public void createPartControl(Composite parent) {
		tree = new AnalysisResultTree(parent);
		tree.getViewer().setAutoExpandLevel(2);

		menuListener = new AgreeMenuListener(getViewSite().getWorkbenchWindow(), tree);
		MenuManager manager = new MenuManager();
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(menuListener);
		tree.getControl().setMenu(manager.createContextMenu(tree.getViewer().getTree()));
	}

	@Override
	public void setFocus() {
		tree.getControl().setFocus();
	}

	public void setInput(AnalysisResult result, AgreeResultsLinker linker) {
		tree.setInput(result);
		menuListener.setLinker(linker);
	}

	public void activateTerminateHandlers(
			final IProgressMonitor globalMonitor,
			AtomicReference<IProgressMonitor> monitorRef) {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay().syncExec(() -> {
			IHandlerService handlerService = getHandlerService();
			terminateActivation = handlerService.activateHandler(AadlHandler.TERMINATE_ID,
					new TerminateHandler(monitorRef));
			terminateAllActivation = handlerService
					.activateHandler(AadlHandler.TERMINATE_ALL_ID,
					new TerminateHandler(monitorRef, globalMonitor));
		});
	}

	public void deactivateTerminateHandlers() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay().syncExec(() -> {
			IHandlerService handlerService = getHandlerService();
			handlerService.deactivateHandler(terminateActivation);
			handlerService.deactivateHandler(terminateAllActivation);
		});
	}

	public void enableRerunHandler(final Element root, VerifyHandler verifyHandler) {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay().syncExec(() -> {
			IHandlerService handlerService = getHandlerService();
			rerunActivation = handlerService.activateHandler(RerunHandler.RERUN_ID,
					new RerunHandler(root, verifyHandler));
		});
	}

	public void disableRerunHandler() {
		if (rerunActivation != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay().syncExec(() -> {
				IHandlerService handlerService = getHandlerService();
				handlerService.deactivateHandler(rerunActivation);
				rerunActivation = null;
			});
		}
	}

	public void saveResultsHandler() {
		if (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.PREF_SAVE_RESULTS)) {
			if (tree.getViewer().getInput() instanceof CompositeAnalysisResult) {
				AgreeFileUtil.saveResult((CompositeAnalysisResult) tree.getViewer().getInput());
			}
		}
	}

	private IHandlerService getHandlerService() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(IHandlerService.class);
	}

}
