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

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {
	public static final String PREF_INDUCT_CEX = "inductiveCounterexamples";

	public static final String PREF_SUPPORT = "setOfSupport";

	public static final String PREF_SMOOTH_CEX = "smoothCounterexamples";

	public static final String PREF_UNSPECIFIED_AADL_PROPERTIES = "analyzeUnspecifiedAadlProperties";

	public static final String PREF_DISPLAY_DECIMAL_CEX = "displayCounterexamplesAsDecimal";

	public static final String PREF_BLAME_CEX = "blameCounterexamples";

	public static final String PREF_NO_KINDUCTION = "disableKInduction";

	public static final String PREF_DEPTH = "inductionDepth";

	public static final String PREF_TIMEOUT = "timeout";

	public static final String PREF_PDR_MAX = "pdrMax";

	public static final String PREF_SOLVER = "solver";

	public static final String SOLVER_SMTINTERPOL = "SMTInterpol";
	public static final String SOLVER_YICES = "Yices";
	public static final String SOLVER_Z3 = "Z3";
	public static final String SOLVER_CVC4 = "CVC4";
	public static final String SOLVER_YICES2 = "Yices 2";
	public static final String SOLVER_DREAL = "dReal";

	public static final String PREF_MODEL_CHECKER = "modelChecker";

	public static final String MODEL_CHECKER_JKIND = "JKind";
	public static final String MODEL_CHECKER_KIND2 = "Kind 2";
	public static final String MODEL_CHECKER_KIND2WEB = "Kind 2 Remote";
	public static final String MODEL_CHECKER_SALLY = "Sally";

	public static final String PREF_REMOTE_URL = "remoteUrl";

	public static final String PREF_CONSIST_DEPTH = "consistDepth";

	public static final String PREF_PROP_LOG = "propLog";
	public static final String PREF_PROP_LOG_FILENAME = "propLogFileName";

	public static final String PREF_SAVE_RESULTS = "saveResults";
	public static final String PREF_SAVE_RESULTS_FILENAME = "saveResultsFileName";

	public static final String PREF_DEBUG = "debug";
}
