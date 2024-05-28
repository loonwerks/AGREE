package com.rockwellcollins.atc.agree.cli;

import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


//public class Activator implements BundleActivator {
public class Activator extends AbstractUIPlugin {

	private static BundleContext context;
	
	// The shared instance
	private static Activator plugin;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		plugin = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		plugin = null;
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
