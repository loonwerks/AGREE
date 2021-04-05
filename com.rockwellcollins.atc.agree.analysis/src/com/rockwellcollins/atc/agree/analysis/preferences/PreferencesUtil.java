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
package com.rockwellcollins.atc.agree.analysis.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

//import com.rockwellcollins.atc.z3.Z3Plugin;
import com.collins.trustedsystems.z3.Z3Plugin;
import com.rockwellcollins.atc.agree.analysis.Activator;

import jkind.SolverOption;
import jkind.api.JKindApi;
import jkind.api.JRealizabilityApi;
import jkind.api.Kind2Api;
import jkind.api.Kind2WebApi;
import jkind.api.KindApi;
import jkind.api.SallyApi;

public class PreferencesUtil {
	public static KindApi getKindApi() {
		IPreferenceStore prefs = getPreferenceStore();
		String modelChecker = prefs.getString(PreferenceConstants.PREF_MODEL_CHECKER);
		String remoteUrl = prefs.getString(PreferenceConstants.PREF_REMOTE_URL);
		KindApi api = getKindApi(modelChecker, remoteUrl);
		if (prefs.getBoolean(PreferenceConstants.PREF_DEBUG)) {
			api.setApiDebug();
		}
		return api;
	}

	public static SolverOption getSolverOption() {
		IPreferenceStore prefs = getPreferenceStore();
		String solverString = prefs.getString(PreferenceConstants.PREF_SOLVER).toUpperCase().replaceAll(" ", "");
		SolverOption solver = SolverOption.valueOf(solverString);
		return solver;
	}

	private static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static KindApi getKindApi(String modelChecker, String remoteUrl) {
		switch (modelChecker) {
		case PreferenceConstants.MODEL_CHECKER_JKIND:
			return getJKindApi();
		case PreferenceConstants.MODEL_CHECKER_KIND2:
			return getKind2Api();
		case PreferenceConstants.MODEL_CHECKER_KIND2WEB:
			return getKind2WebApi(remoteUrl);
		case PreferenceConstants.MODEL_CHECKER_SALLY:
			return getSallyApi();
		default:
			throw new IllegalArgumentException("Unknown model checker setting: " + modelChecker);
		}
	}

	public static KindApi getConsistencyApi() {
		KindApi api = getKindApi();

		if (api instanceof JKindApi) {
			IPreferenceStore prefs = getPreferenceStore();
			int depth = prefs.getInt(PreferenceConstants.PREF_CONSIST_DEPTH) + 1;
			((JKindApi) api).setN(depth);
			((JKindApi) api).setPdrMax(0);
			if (prefs.getBoolean(PreferenceConstants.PREF_SUPPORT)) {
				((JKindApi) api).setIvcReduction();
			}
		}
		return api;
	}

	private static JKindApi getJKindApi() {
		IPreferenceStore prefs = getPreferenceStore();
		JKindApi api = new JKindApi();
		api.setJKindJar(getJKindJar());
		try {
			api.setEnvironment("Z3_HOME", Z3Plugin.getZ3Directory());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
			// Z3Plugin not present
		} catch (Exception e) {
			// Some unknown exception finding Z3
			e.printStackTrace();
		}

		String solverString = prefs.getString(PreferenceConstants.PREF_SOLVER).toUpperCase().replaceAll(" ", "");
		SolverOption solver = SolverOption.valueOf(solverString);
		api.setSolver(solver);

		if (prefs.getBoolean(PreferenceConstants.PREF_INDUCT_CEX)) {
			api.setInductiveCounterexamples();
		}
		if (prefs.getBoolean(PreferenceConstants.PREF_SMOOTH_CEX) && solver == SolverOption.YICES) {
			api.setSmoothCounterexamples();
		}
		if (prefs.getBoolean(PreferenceConstants.PREF_SUPPORT)) {
			api.setIvcReduction();
		}
		api.setN(prefs.getInt(PreferenceConstants.PREF_DEPTH));
		api.setTimeout(prefs.getInt(PreferenceConstants.PREF_TIMEOUT));
		api.setPdrMax(prefs.getInt(PreferenceConstants.PREF_PDR_MAX));
		// TODO set pdr invariants as preferences option
		// api.setPdrInvariants();
		if (prefs.getBoolean(PreferenceConstants.PREF_NO_KINDUCTION)) {
			api.disableKInduction();
		}
		return api;
	}

	public static JRealizabilityApi getJRealizabilityApi() {
		IPreferenceStore prefs = getPreferenceStore();
		JRealizabilityApi api = new JRealizabilityApi();
		api.setJKindJar(getJKindJar());
		try {
			api.setEnvironment("Z3_HOME", Z3Plugin.getZ3Directory());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
			// Z3Plugin not present
		} catch (Exception e) {
			// Some unknown exception finding Z3
			e.printStackTrace();
		}

		api.setN(prefs.getInt(PreferenceConstants.PREF_DEPTH));
		api.setTimeout(prefs.getInt(PreferenceConstants.PREF_TIMEOUT));

		return api;
	}

	// TODO: Need to update this for the JKind plugin, not the jar in the project.
	public static String getJKindJar() {
		return com.collins.trustedsystems.jkindapi.PluginUtil.getJKindJar();
//		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
//		URL url = bundle.getEntry("dependencies/jkind.jar");
//		try {
//			URL fileUrl = FileLocator.toFileURL(url);
//			return new File(fileUrl.getPath()).toString();
//		} catch (Exception e) {
//			throw new JKindException("Unable to extract jkind.jar from plug-in", e);
//		}
	}

	private static Kind2Api getKind2Api() {
		IPreferenceStore prefs = getPreferenceStore();
		Kind2Api api = new Kind2Api();
		api.setTimeout(prefs.getInt(PreferenceConstants.PREF_TIMEOUT));
		return api;
	}

	private static Kind2WebApi getKind2WebApi(String uri) {
		IPreferenceStore prefs = getPreferenceStore();
		Kind2WebApi api = new Kind2WebApi(uri);
		api.setTimeout(prefs.getInt(PreferenceConstants.PREF_TIMEOUT));
		return api;
	}

	private static SallyApi getSallyApi() {
		IPreferenceStore prefs = getPreferenceStore();
		SallyApi api = new SallyApi();
		api.setTimeout(prefs.getInt(PreferenceConstants.PREF_TIMEOUT));
		return api;
	}
}
