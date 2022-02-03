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

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;

public class TerminateHandler extends AbstractHandler {
	private final AtomicReference<IProgressMonitor> monitorRef;
	private final IProgressMonitor globalMonitor;

	public TerminateHandler(AtomicReference<IProgressMonitor> monitorRef) {
		this.monitorRef = monitorRef;
		globalMonitor = null;
	}

	public TerminateHandler(AtomicReference<IProgressMonitor> monitorRef, IProgressMonitor globalMonitor) {
		this.monitorRef = monitorRef;
		this.globalMonitor = globalMonitor;
	}

	@Override
	public Object execute(ExecutionEvent event) {
		if (globalMonitor != null) {
			globalMonitor.setCanceled(true);
		}
		IProgressMonitor monitor = monitorRef.get();
		if (monitor != null) {
			monitor.setCanceled(true);
		}
		return null;
	}
}
