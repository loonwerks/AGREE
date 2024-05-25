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
	
	// Gets the workspace path contained in the -data framework cmd line arg
	public static String getWorkspace() {
		final ServiceReference<EnvironmentInfo> infoRef = context.getServiceReference(EnvironmentInfo.class);
		if (infoRef != null) {
			final EnvironmentInfo envInfo = context.getService(infoRef);
			if (envInfo != null) {
				context.ungetService(infoRef);
				for (int i = 0; i < envInfo.getFrameworkArgs().length; ++i) {
					if ("-data".equals(envInfo.getFrameworkArgs()[i].toLowerCase())
							&& i < envInfo.getFrameworkArgs().length - 1) {
						return envInfo.getFrameworkArgs()[i + 1];
					}
				}
			}
		}
		return null;
	}

}
