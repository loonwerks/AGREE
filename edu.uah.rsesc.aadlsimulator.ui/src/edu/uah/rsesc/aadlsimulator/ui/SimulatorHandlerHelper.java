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
package edu.uah.rsesc.aadlsimulator.ui;

import java.util.Objects;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.FrameworkUtil;

import edu.uah.rsesc.aadlsimulator.ui.launch.SimulationLaunchShortcut;
import edu.uah.rsesc.aadlsimulator.ui.services.SimulationUIService;

public class SimulatorHandlerHelper {
	public static Object startSimulator(final ExecutionEvent event, final String engineTypeId) {
		try {
			final SimulationUIService simulationUiService = Objects.requireNonNull(EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(SimulatorHandlerHelper.class).getBundleContext()).get(SimulationUIService.class), "unable to get simulation UI service");
			if(simulationUiService.getCurrentState().getSimulationEngine() != null) {
				final MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		        messageBox.setMessage("A simulation is already active. Do you want to stop the current simulation?");
		        messageBox.setText("Stop Current Simulation");
		        if(messageBox.open() == SWT.NO) {
		        	return null;
		        }
			}

			if(event.getApplicationContext() instanceof IEvaluationContext) {
				final IEvaluationContext appContext = (IEvaluationContext)event.getApplicationContext();
				final ISelection selection = (ISelection)appContext.getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
				final SimulationLaunchShortcut launchShortcut = new SimulationLaunchShortcut();
				launchShortcut.launch(selection, engineTypeId, ILaunchManager.RUN_MODE);
			}
		} catch(final Exception ex) {
			final Status status = new Status(IStatus.ERROR, FrameworkUtil.getBundle(SimulatorHandlerHelper.class).getSymbolicName(), "Error", ex);
			StatusManager.getManager().handle(status, StatusManager.SHOW | StatusManager.LOG);
		}

		return null;
	}
}
