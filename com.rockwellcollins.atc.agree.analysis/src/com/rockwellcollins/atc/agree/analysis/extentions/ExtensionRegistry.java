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
package com.rockwellcollins.atc.agree.analysis.extentions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.rockwellcollins.atc.agree.analysis.Activator;

public abstract class ExtensionRegistry {

	/** ID of counterexample extractor extention point */
	public static final String CEX_EXTRACTOR_EXT_ID = "cexextractor";
	public static final String AGREE_AUTOMATER_EXT_ID = "agreeautomater";
	private static final String ATT_EXTNAME = "pluginName";

	private static final Map<String, ExtensionRegistry> registries = new HashMap<>();

	/** The extensions in this registry */
	protected Map<String, ExtensionProxy> extensions;

	public static ExtensionRegistry getRegistry(String extensionId) {
		ExtensionRegistry registry = registries.get(extensionId);

		if (registry == null) {
			registry = createRegistry(extensionId);
			registries.put(extensionId, registry);
		}
		return registry;
	}

	protected static ExtensionRegistry createRegistry(String extensionId) {
		if (extensionId == CEX_EXTRACTOR_EXT_ID) {
			return new CexExtractorRegistry();
		} else if (extensionId == AGREE_AUTOMATER_EXT_ID) {
			return new AgreeAutomaterRegistry();
		}
		return null;
	}

	protected void initialize(String extensionId) {
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(Activator.PLUGIN_ID, extensionId);
		IExtension[] exts = extensionPoint.getExtensions();

		extensions = new HashMap<>();
		for (int i = 0; i < exts.length; i++) {
			IConfigurationElement[] configElems = exts[i].getConfigurationElements();

			for (int j = 0; j < configElems.length; j++) {
				String extentionName = configElems[j].getAttribute(ATT_EXTNAME);

				if (extensions.get(extentionName) != null) {
					System.err.println("Duplicate extension: " + extensionId + ", name: " + extentionName);
				} else {
					if (extentionName != null) {
						extensions.put(extentionName.toLowerCase(), createProxy(configElems[j]));
					}
				}
			}
		}
	}

	protected abstract ExtensionProxy createProxy(IConfigurationElement configElem);

}
