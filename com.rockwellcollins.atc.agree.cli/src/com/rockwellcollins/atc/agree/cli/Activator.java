package com.rockwellcollins.atc.agree.cli;

import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
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
