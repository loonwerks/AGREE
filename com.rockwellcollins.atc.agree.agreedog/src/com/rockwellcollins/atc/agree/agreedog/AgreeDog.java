/*
Copyright (c) 2021, Collins Aerospace.
Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).

Permission is hereby granted, free of charge, to any person obtaining a copy of this data,
including any software or models in source or binary form, as well as any drawings, specifications,
and documentation (collectively "the Data"), to deal in the Data without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Data.

THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
*/

package com.rockwellcollins.atc.agree.agreedog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.osate.aadl2.ComponentImplementation;
import org.osgi.framework.Bundle;

import com.rockwellcollins.atc.agree.analysis.extentions.CexExtractor;

import jkind.results.Counterexample;
import jkind.results.Property;

public class AgreeDog implements CexExtractor {

	protected class Runner {
		protected Process process;
		protected BufferedReader fromProcess;
		protected BufferedWriter toProcess;

		Runner() throws Exception {
			ProcessBuilder processBuilder = new ProcessBuilder(getArgs());
			processBuilder.redirectErrorStream(true);
			System.out.println(String.join(" ", processBuilder.command()));
			try {
				process = processBuilder.start();
			} catch (IOException e) {
				Exception generalException = new Exception(
						"Unable to start AgreeDog by executing: " + String.join(" ", processBuilder.command()),
						e);
				throw generalException;
			}
			addShutdownHook();
			toProcess = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			fromProcess = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String out;
			while (!monitor.isCanceled() && (out = fromProcess.readLine()) != null) {
				System.out.println(out);
				output = out;
			}
			if (monitor.isCanceled()) {
				stop();
			}
			process.waitFor();
			System.out.println("Finished running AgreeDog.");

		}

		private final Thread shutdownHook = new Thread("shutdown-hook") {
			@Override
			public void run() {
				Runner.this.stop();
			}
		};

		private void addShutdownHook() {
			Runtime.getRuntime().addShutdownHook(shutdownHook);
		}

		private void removeShutdownHook() {
			try {
				Runtime.getRuntime().removeShutdownHook(shutdownHook);
			} catch (IllegalStateException e) {
				// Ignore, we are already shutting down
			}
		}

		public synchronized void stop() {
			/**
			 * This must be synchronized since two threads (an Engine or a shutdown
			 * hook) may try to stop the process at the same time
			 */

			if (process != null) {
				process.destroy();
				process = null;
			}

			removeShutdownHook();
		}

	};

	private String output = "";
	private IProgressMonitor monitor = null;

	public AgreeDog() {

	}

	public String run(IProgressMonitor monitor) throws Exception {
		this.monitor = monitor;
		this.output = "";
		if (this.monitor == null) {
			this.monitor = new NullProgressMonitor();
		}
		new Runner();
		return this.output;
	}

	private List<String> getArgs() {
		List<String> result = new ArrayList<>();

		result.add("python3");
		result.add(getEntrypoint());

		return result;
	}

	private String getEntrypoint() {

		final Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		try {
			final URL dirUrl = FileLocator.toFileURL(bundle.getEntry("python"));
			final File py = new File(dirUrl.getPath(), "AgreeDog.py");
			return py.getPath();
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to extract AgreeDog.py from plugin", e);
		}
	}

	@Override
	public void receiveCex(ComponentImplementation compImpl, Property property, EObject agreePoperty,
			Counterexample cex, Map<String, EObject> refMap) {
		// TODO
		// System.out.println("CEX:\n\n" + cex.toString());
	}

	@Override
	public String getDisplayText() {
		return "AgreeDog";
	}

}
