package com.rockwellcollins.atc.agree.cli;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.Pair;
import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.AnnexSubclause;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.InstancePackage;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.instantiation.InstantiateModel;
import org.osate.aadl2.modelsupport.util.AadlUtil;
import org.osate.aadl2.util.Aadl2ResourceFactoryImpl;
import org.osate.aadl2.util.Aadl2Util;
import org.osate.annexsupport.AnnexUtil;
import org.osate.pluginsupport.PluginSupportUtil;
import org.osate.xtext.aadl2.Aadl2StandaloneSetup;

import com.google.inject.Injector;
import com.rockwellcollins.atc.agree.AgreeStandaloneSetup;
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause;
import com.rockwellcollins.atc.agree.agree.AgreePackage;
import com.rockwellcollins.atc.agree.agree.AgreeSubclause;
import com.rockwellcollins.atc.agree.analysis.Activator;
import com.rockwellcollins.atc.agree.analysis.AgreeException;
import com.rockwellcollins.atc.agree.analysis.AgreeLayout;
import com.rockwellcollins.atc.agree.analysis.AgreeLogger;
import com.rockwellcollins.atc.agree.analysis.AgreeRenaming;
import com.rockwellcollins.atc.agree.analysis.AgreeUtils;
import com.rockwellcollins.atc.agree.analysis.ConsistencyResult;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeASTBuilder;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNode;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeProgram;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeStatement;
import com.rockwellcollins.atc.agree.analysis.extentions.AgreeAutomater;
import com.rockwellcollins.atc.agree.analysis.extentions.AgreeAutomaterRegistry;
import com.rockwellcollins.atc.agree.analysis.extentions.ExtensionRegistry;
import com.rockwellcollins.atc.agree.analysis.lustre.visitors.RenamingVisitor;
import com.rockwellcollins.atc.agree.analysis.preferences.PreferenceConstants;
import com.rockwellcollins.atc.agree.analysis.preferences.PreferencesUtil;
import com.rockwellcollins.atc.agree.analysis.saving.AgreeFileUtil;
import com.rockwellcollins.atc.agree.analysis.translation.LustreAstBuilder;
import com.rockwellcollins.atc.agree.analysis.translation.LustreContractAstBuilder;
import com.rockwellcollins.atc.agree.analysis.views.AgreeResultsLinker;
import com.rockwellcollins.atc.agree.cli.results.AgreeOutput;
import com.rockwellcollins.atc.agree.cli.results.SyntaxValidationResults;

import jkind.JKindException;
import jkind.api.JKindApi;
import jkind.api.JRealizabilityApi;
import jkind.api.KindApi;
import jkind.api.results.AnalysisResult;
import jkind.api.results.CompositeAnalysisResult;
import jkind.api.results.JKindResult;
import jkind.api.results.JRealizabilityResult;
import jkind.lustre.Node;
import jkind.lustre.Program;


public class Agree implements IApplication {

	private final static String HELP = "h";
	private final static String DATA = "data";
	private final static String APPLICATION = "application";
	private final static String NO_SPLASH = "noSplash";
	private final static String PROJECT_PATH = "p";
	private final static String COMP_IMPL = "c";
	private final static String OUTPUT = "o";
	private final static String FILES = "f";
	// Run options
	private final static String STRATEGY = "strategy";
	// Preferences
	private final static String MODEL_CHECKER = "m";
	private final static String SOLVER = "s";
	private final static String DISABLE_K_INDUCTION = "disableKInduction";
	private final static String NO_INDUCTIVE_COUNTEREXAMPLES = "noInductiveCounterexamples";
	private final static String GET_SET_OF_SUPPORT = "getSetOfSupport";
	private final static String GENERATE_SMOOTH_COUNTEREXAMPLES = "generateSmoothCounterexamples";
	private final static String ANALYZE_UNSPECIFIED_AADL_PROPERTIES = "analyzeUnspecifiedAADLProperties";
	private final static String DISPLAY_COUNTEREXAMPLES_AS_DECIMAL = "displayCounterexamplesAsDecimal";
	private final static String GENERATE_BLAMED_COUNTEREXAMPLES = "generateBlamedCounterexamples";
	private final static String MAX_INDUCTION_DEPTH = "maxInductionDepth";
	private final static String MAX_PDR_INSTANCES = "maxPDRInstances";
	private final static String TIMEOUT = "t";
	private final static String CONSISTENCY_DEPTH = "consistencyDepth";
	// Validation options
	private final static String VALIDATION_ONLY = "v";
	private final static String EXIT_ON_VALIDATION_WARNING = "w";

	private AgreeResultsLinker linker = new AgreeResultsLinker();
	private Queue<JKindResult> queue = new ArrayDeque<>();
	private Map<String, String> rerunAdviceMap = new HashMap<>();
	private int adviceCount = 0;
	private enum AnalysisType {
		AssumeGuarantee, Consistency, Realizability
	};
	private enum VerificationStrategy {
		Single, All, Monolithic, Realizability
	}
	private VerificationStrategy strategy = null;

	@Override
	public Object start(IApplicationContext context) throws Exception {

		context.applicationRunning();

		System.out.println("Starting AGREE analysis");

		// Output Json object
		final AgreeOutput output = new AgreeOutput();
		output.setDate((new Date()).toString());

		// Process command line options
		final Path workspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toPath();
		Path projPath = null;
		String component = null;
		Path outputPath = null;
		String[] fileArray = null;
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		// Reset preferences to default values
		store.setToDefault(PreferenceConstants.PREF_MODEL_CHECKER);
		store.setToDefault(PreferenceConstants.PREF_SOLVER);
		store.setToDefault(PreferenceConstants.PREF_NO_KINDUCTION);
		store.setToDefault(PreferenceConstants.PREF_SUPPORT);
		store.setToDefault(PreferenceConstants.PREF_SMOOTH_CEX);
		store.setToDefault(PreferenceConstants.PREF_UNSPECIFIED_AADL_PROPERTIES);
		store.setToDefault(PreferenceConstants.PREF_DISPLAY_DECIMAL_CEX);
		store.setToDefault(PreferenceConstants.PREF_BLAME_CEX);
		store.setToDefault(PreferenceConstants.PREF_DEPTH);
		store.setToDefault(PreferenceConstants.PREF_PDR_MAX);
		store.setToDefault(PreferenceConstants.PREF_TIMEOUT);
		store.setToDefault(PreferenceConstants.PREF_CONSIST_DEPTH);
		store.setToDefault(PreferenceConstants.PREF_PROP_LOG);
		boolean exitOnValidationWarning = false;
		boolean validationOnly = false;
		boolean exit = false;

		// Application Args
		final String[] args = (String[]) context.getArguments().get("application.args");
		System.out.println("Application args: " + Arrays.toString(args));
		System.out.println("Workspace: " + workspace);

		// create Options
		final Options options = new Options();
		options.addOption(HELP, "help", false, "print this message");
		options.addOption(NO_SPLASH, false, "optional, hide the splash screen");
		options.addOption(DATA, true, "required, path of workspace");
		options.addOption(APPLICATION, true,
				"required, the name of this analysis (com.rockwellcollins.atc.agree.cli.Agree)");
		options.addOption(PROJECT_PATH, "projectPath", true, "optional, project path relative to workspace");
		options.addOption(COMP_IMPL, "compImpl", true, "required, qualified component implementation name");
		options.addOption(OUTPUT, "output", true, "required, output JSON file absolute path");
		Option option = new Option(FILES, "files", true, "optional, supplementary AADL files (absolute paths)");
		option.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(option);
		options.addOption(STRATEGY, true, "required, verification strategy (single, all, monolithic, realizability)");
		options.addOption(MODEL_CHECKER, "modelChecker", true, "optional, model checker, default " + store.getString(PreferenceConstants.PREF_MODEL_CHECKER));
		options.addOption(SOLVER, "solver", true, "optional, SMT solver, default " + store.getString(PreferenceConstants.PREF_SOLVER));
		options.addOption(DISABLE_K_INDUCTION, false, "optional, disable k-induction");
		options.addOption(NO_INDUCTIVE_COUNTEREXAMPLES, false, "optional, do not generate inductive counterexamples");
		options.addOption(GET_SET_OF_SUPPORT, false, "optional, get set of support");
		options.addOption(GENERATE_SMOOTH_COUNTEREXAMPLES, false, "optional, generate smooth counterexamples");
		options.addOption(ANALYZE_UNSPECIFIED_AADL_PROPERTIES, false, "optional, analyze unspecified AADL properties");
		options.addOption(DISPLAY_COUNTEREXAMPLES_AS_DECIMAL, false, "optional, display counterexamples as decimal");
		options.addOption(GENERATE_BLAMED_COUNTEREXAMPLES, false, "optional, generate blamed counterexamples");
		options.addOption(MAX_INDUCTION_DEPTH, true, "optional, maximum induction depth, default " + store.getInt(PreferenceConstants.PREF_DEPTH));
		options.addOption(MAX_PDR_INSTANCES, true, "optional, maximum number of PDR instances, default " + store.getInt(PreferenceConstants.PREF_PDR_MAX));
		options.addOption(TIMEOUT, "timeout", true, "optional, timeout (ms), default " + store.getInt(PreferenceConstants.PREF_TIMEOUT));
		options.addOption(CONSISTENCY_DEPTH, true, "optional, consistency depth, default " + store.getInt(PreferenceConstants.PREF_CONSIST_DEPTH));
		options.addOption(VALIDATION_ONLY, "validationOnly", false, "validation only, default false");
		options.addOption(EXIT_ON_VALIDATION_WARNING, "exitOnValidtionWarning", false,
				"exit on validation warning, default false");

		// parse options
		try {
			final CommandLineParser parser = new DefaultParser();
			final CommandLine commandLine = parser.parse(options, args);

			if (commandLine.hasOption(HELP)) {
				exit = true;
			}
			if (workspace == null) {
				exit = true;
				output.addStatusMessage("A workspace must be specified.");
			}
			if (commandLine.hasOption(COMP_IMPL)) {
				component = commandLine.getOptionValue(COMP_IMPL);
				output.setComponent(component);
				// expects qualified name
				if (!component.contains("::")) {
					output.addStatusMessage("Component implementation qualified name must be specified.");
					exit = true;
				}
			}
			if (commandLine.hasOption(PROJECT_PATH)) {
				projPath = workspace.resolve(commandLine.getOptionValue(PROJECT_PATH));
			} else {
				projPath = workspace;
			}
			output.setProject(projPath.toString());
			if (commandLine.hasOption(OUTPUT)) {
				outputPath = Paths.get(commandLine.getOptionValue(OUTPUT));

				// Make sure output directory exists and is valid
				try {
					outputPath.getParent().toFile().mkdirs();
				} catch (InvalidPathException e1) {
					exit = true;
					outputPath = null;
					output.addStatusMessage("Invalid output path: " + outputPath + ".");
				} catch (NullPointerException e2) {
					// Do nothing, this is the root directory (which exists)
				} catch (UnsupportedOperationException e3) {
					exit = true;
					outputPath = null;
					output.addStatusMessage("No file system access to output path " + outputPath + ".");
				} catch (SecurityException e4) {
					exit = true;
					outputPath = null;
					output.addStatusMessage("Security settings prohibit writing to output path " + outputPath + ".");
				}
			}
			if (commandLine.hasOption(FILES)) {
				fileArray = commandLine.getOptionValues(FILES);
			}
			if (commandLine.hasOption(STRATEGY)) {
				switch (commandLine.getOptionValue(STRATEGY).toLowerCase()) {
				case "single":
					strategy = VerificationStrategy.Single;
					break;
				case "all":
					strategy = VerificationStrategy.All;
					break;
				case "monolithic":
					strategy = VerificationStrategy.Monolithic;
					break;
				case "realizability":
					strategy = VerificationStrategy.Realizability;
					break;
				default:
					exit = true;
					output.addStatusMessage("Invalid verification strategy.  " + commandLine.getOptionValue(STRATEGY) + " is not supported.");
				}
			} else {
				exit = true;
				output.addStatusMessage("A verification type must be specified (single, all, monolithic, realizability).");
			}
			if (commandLine.hasOption(MODEL_CHECKER)) {
				switch (commandLine.getOptionValue(MODEL_CHECKER).toLowerCase()) {
				case "jkind":
					store.setValue(PreferenceConstants.PREF_MODEL_CHECKER, PreferenceConstants.MODEL_CHECKER_JKIND);
					break;
				case "kind2":
					store.setValue(PreferenceConstants.PREF_MODEL_CHECKER, PreferenceConstants.MODEL_CHECKER_KIND2);
					break;
				case "kind2remote":
					store.setValue(PreferenceConstants.PREF_MODEL_CHECKER, PreferenceConstants.MODEL_CHECKER_KIND2WEB);
					break;
				case "sally":
					store.setValue(PreferenceConstants.PREF_MODEL_CHECKER, PreferenceConstants.MODEL_CHECKER_SALLY);
					break;
				default:
					exit = true;
					output.addStatusMessage("Invalid model checker.  " + commandLine.getOptionValue(MODEL_CHECKER) + " is not supported. Valid options are: JKind, Kind2, Kind2Remote, Sally.");
				}
			}
			if (commandLine.hasOption(SOLVER)) {
				switch (commandLine.getOptionValue(SOLVER).toLowerCase()) {
				case "smtinterpol":
					store.setValue(PreferenceConstants.PREF_SOLVER, PreferenceConstants.SOLVER_SMTINTERPOL);
					break;
				case "yices":
					store.setValue(PreferenceConstants.PREF_SOLVER, PreferenceConstants.SOLVER_YICES);
					break;
				case "yices2":
					store.setValue(PreferenceConstants.PREF_SOLVER, PreferenceConstants.SOLVER_YICES2);
					break;
				case "z3":
					store.setValue(PreferenceConstants.PREF_SOLVER, PreferenceConstants.SOLVER_Z3);
					break;
				case "cvc4":
					store.setValue(PreferenceConstants.PREF_SOLVER, PreferenceConstants.SOLVER_CVC4);
					break;
				case "cvc5":
					// TODO set pref
					break;
				case "dreal":
					store.setValue(PreferenceConstants.PREF_SOLVER, PreferenceConstants.SOLVER_DREAL);
					break;
				default:
					exit = true;
					output.addStatusMessage("Invalid solver.  " + commandLine.getOptionValue(SOLVER) + " is not supported.  Valid options are: SMTInterpol, Yices, Yices2, Z3, CVC4, CVC5, dReal.");
				}
			}
			if (commandLine.hasOption(DISABLE_K_INDUCTION)) {
				store.setValue(PreferenceConstants.PREF_NO_KINDUCTION, true);
			}
			if (commandLine.hasOption(NO_INDUCTIVE_COUNTEREXAMPLES)) {
				store.setValue(PreferenceConstants.PREF_INDUCT_CEX, false);
			}
			if (commandLine.hasOption(GET_SET_OF_SUPPORT)) {
				store.setValue(PreferenceConstants.PREF_SUPPORT, true);
			}
			if (commandLine.hasOption(GENERATE_SMOOTH_COUNTEREXAMPLES)) {
				store.setValue(PreferenceConstants.PREF_SMOOTH_CEX, true);
			}
			if (commandLine.hasOption(ANALYZE_UNSPECIFIED_AADL_PROPERTIES)) {
				store.setValue(PreferenceConstants.PREF_UNSPECIFIED_AADL_PROPERTIES, true);
			}
			if (commandLine.hasOption(DISPLAY_COUNTEREXAMPLES_AS_DECIMAL)) {
				store.setValue(PreferenceConstants.PREF_DISPLAY_DECIMAL_CEX, true);
			}
			if (commandLine.hasOption(GENERATE_BLAMED_COUNTEREXAMPLES)) {
				store.setValue(PreferenceConstants.PREF_BLAME_CEX, true);
			}
			if (commandLine.hasOption(MAX_INDUCTION_DEPTH)) {
				try {
					final int i = Integer.parseInt(commandLine.getOptionValue(MAX_INDUCTION_DEPTH));
					if (i < 0) {
						throw new NumberFormatException("Value must be greater than zero.");
					}
					store.setValue(PreferenceConstants.PREF_DEPTH, i);
				} catch (NumberFormatException e) {
					exit = true;
					output.addStatusMessage("Invalid maximum induction depth specified.  " + e.getMessage());
				}
			}
			if (commandLine.hasOption(MAX_PDR_INSTANCES)) {
				try {
					final int i = Integer.parseInt(commandLine.getOptionValue(MAX_PDR_INSTANCES));
					if (i < 0) {
						throw new NumberFormatException("Value must be greater than zero.");
					}
					store.setValue(PreferenceConstants.PREF_PDR_MAX, i);
				} catch (NumberFormatException e) {
					exit = true;
					output.addStatusMessage("Invalid maximum PDR instances specified.  " + e.getMessage());
				}
			}
			if (commandLine.hasOption(TIMEOUT)) {
				try {
					final int i = Integer.parseInt(commandLine.getOptionValue(TIMEOUT));
					if (i < 0) {
						throw new NumberFormatException("Value must be greater than zero.");
					}
					store.setValue(PreferenceConstants.PREF_TIMEOUT, i);
				} catch (NumberFormatException e) {
					exit = true;
					output.addStatusMessage("Invalid timeout specified.  " + e.getMessage());
				}
			}
			if (commandLine.hasOption(CONSISTENCY_DEPTH)) {
				try {
					final int i = Integer.parseInt(commandLine.getOptionValue(CONSISTENCY_DEPTH));
					if (i < 0) {
						throw new NumberFormatException("Value must be greater than zero.");
					}
					store.setValue(PreferenceConstants.PREF_CONSIST_DEPTH, i);
				} catch (NumberFormatException e) {
					exit = true;
					output.addStatusMessage("Invalid consistency depth specified.  " + e.getMessage());
				}
			}
			if (commandLine.hasOption(VALIDATION_ONLY)) {
				validationOnly = true;
			}
			if (commandLine.hasOption(EXIT_ON_VALIDATION_WARNING)) {
				exitOnValidationWarning = true;
			}
			final String[] remainder = commandLine.getArgs();
			for (String argument : remainder) {
				final String message = "WARNING: unknown argument " + argument
						+ ". See --help for command line options.";
				output.addStatusMessage(message);
				System.err.println(message);
			}
		} catch (ParseException exception) {
			final String message = "Parse error: " + exception.getMessage();
			System.err.println(message);
			output.addStatusMessage(message);
			exit = true;
		}

		if (exit) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("osate", options);
			output.setStatus(AgreeOutput.INTERRUPTED);
			Util.writeOutput(output, outputPath);
			return IApplication.EXIT_OK;
		}

		// Initialize the AADL meta model
		final Injector injector = new Aadl2StandaloneSetup().createInjectorAndDoEMFRegistration();

		AgreeStandaloneSetup.doSetup();

		// Initialize the meta model for instance models -- need both lines
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("aaxl2", new Aadl2ResourceFactoryImpl());
		InstancePackage.eINSTANCE.eClass();

		// Get the resource set from the Injector obtained from initializing the AADL meta model
		final XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

		if (resourceSet == null) {
			output.setStatus(AgreeOutput.INTERRUPTED);
			output.addStatusMessage("Unable to initialize resource set");
			Util.writeOutput(output, outputPath);
			return IApplication.EXIT_OK;
		}

		// Add plug-in contributions to resource set
		for (URI uri : PluginSupportUtil.getContributedAadl()) {
			Resource res = resourceSet.getResource(uri, true);
			System.out.println("... " + res.getURI() + (res.isLoaded() ? " is loaded" : " is not loaded"));
		}

		// Load project AADL files
		try {
			Util.loadProjectAadlFiles(workspace, projPath, fileArray, resourceSet);
		} catch (Exception e) {
			output.setStatus(AgreeOutput.INTERRUPTED);
			output.addStatusMessage(e.getMessage());
			Util.writeOutput(output, outputPath);
			return IApplication.EXIT_OK;
		}

		// Validate resource set
		final SyntaxValidationResults validationResults = Util.validateResourceSet(resourceSet);
		// Add validation results to output
		output.setSyntaxValidationResults(validationResults);

		// If there are any errors, do not continue
		// Don't continue if user doesn't want any warnings
		// Don't continue if user only wants to validate model
		if (validationResults.getErrors() > 0 || (exitOnValidationWarning && validationResults.getWarnings() > 0)) {
			output.setStatus(AgreeOutput.INTERRUPTED);
			output.addStatusMessage("Syntax validation issues found.");
			Util.writeOutput(output, outputPath);
			return IApplication.EXIT_OK;
		} else if (validationOnly) {
			output.setStatus(AgreeOutput.COMPLETED);
			Util.writeOutput(output, outputPath);
			return IApplication.EXIT_OK;
		}

		// Run AGREE
		ComponentImplementation compImpl = null;
		for (Resource resource : resourceSet.getResources()) {

			if (!resource.getContents().isEmpty() && resource.getContents().get(0) instanceof AadlPackage) {
				final AadlPackage pkg = (AadlPackage) resource.getContents().get(0);
				if (pkg.getName().equalsIgnoreCase(Aadl2Util.getPackageName(component))) {

					compImpl = (ComponentImplementation) AadlUtil.findNamedElementInList(
							AadlUtil.getAllComponentImpl(pkg), Aadl2Util.getItemNameWithoutQualification(component));
					break;
				}
			}
		}
		if (compImpl != null) {
			try {
				final CompositeAnalysisResult results = runAgree(compImpl);
				output.setResults(results);
				final jkind.api.results.Status status = results.getMultiStatus().getOverallStatus();
				if (status.equals(jkind.api.results.Status.VALID)
						|| status.equals(jkind.api.results.Status.VALID_REFINED)) {
					output.setStatus(AgreeOutput.VALID);
				} else if (status.equals(jkind.api.results.Status.INVALID)
						|| status.equals(jkind.api.results.Status.INCONSISTENT)) {
					output.setStatus(AgreeOutput.INVALID);
				} else {
					output.setStatus(AgreeOutput.INTERRUPTED);
				}
				Util.writeOutput(output, outputPath);
			} catch (Exception e) {
				e.printStackTrace();
				output.setStatus(AgreeOutput.INTERRUPTED);
				output.addStatusMessage(e.getMessage());
				Util.writeOutput(output, outputPath);
				return IApplication.EXIT_OK;
			}
		} else {
			output.setStatus(AgreeOutput.INTERRUPTED);
			output.addStatusMessage("Could not find component implementation in project.");
			Util.writeOutput(output, outputPath);
			return IApplication.EXIT_OK;
		}

		return IApplication.EXIT_OK;

	}

	private CompositeAnalysisResult runAgree(ComponentImplementation compImpl) throws Exception {

		final SystemInstance si = InstantiateModel.instantiate(compImpl);
		AnalysisResult result;
		CompositeAnalysisResult wrapper = new CompositeAnalysisResult("");

		if (strategy == VerificationStrategy.All) {
			if (AgreeUtils.usingKind2()) {
				throw new AgreeException("Kind2 only supports monolithic verification");
			}
			result = buildAnalysisResult(compImpl.getName(), si);
			wrapper.addChild(result);
			result = wrapper;
		} else if (strategy == VerificationStrategy.Realizability) {
			AgreeProgram agreeProgram = new AgreeASTBuilder().getAgreeProgram(si, false);

			Program program = LustreAstBuilder.getRealizabilityLustreProgram(agreeProgram);
			wrapper.addChild(createVerification("Realizability Check", si, program, agreeProgram,
					AnalysisType.Realizability));
			result = wrapper;
		} else {
			CompositeAnalysisResult wrapperTop = new CompositeAnalysisResult("Verification for " + compImpl.getName());
			wrapVerificationResult(si, wrapperTop);
			wrapper.addChild(wrapperTop);
			result = wrapper;
		}

		doAnalysis(compImpl, new NullProgressMonitor());
		System.out.println(result.toString());

		return wrapper;
	}

	private AnalysisResult buildAnalysisResult(String name, ComponentInstance ci) {
		CompositeAnalysisResult result = new CompositeAnalysisResult("Verification for " + name);

		if (containsAGREEAnnex(ci)) {
			wrapVerificationResult(ci, result);
			ComponentImplementation compImpl = AgreeUtils.getInstanceImplementation(ci);
			for (ComponentInstance subInst : ci.getComponentInstances()) {
				if (AgreeUtils.getInstanceImplementation(subInst) != null) {
					AnalysisResult buildAnalysisResult = buildAnalysisResult(subInst.getName(), subInst);
					if (buildAnalysisResult != null) {
						result.addChild(buildAnalysisResult);
					}
				}
			}

			if (result.getChildren().size() != 0) {
				linker.setComponent(result, compImpl);
				return result;
			}
		}
		return null;
	}

	private boolean containsAGREEAnnex(ComponentInstance ci) {
		ComponentClassifier compClass = ci.getComponentClassifier();
		if (compClass instanceof ComponentImplementation) {
			compClass = ((ComponentImplementation) compClass).getType();
		}
		for (AnnexSubclause annex : AnnexUtil.getAllAnnexSubclauses(compClass,
				AgreePackage.eINSTANCE.getAgreeContractSubclause())) {
			if (annex instanceof AgreeContractSubclause) {
				return true;
			}
		}
		return false;
	}

	private void wrapVerificationResult(ComponentInstance si, CompositeAnalysisResult wrapper) {
		AgreeProgram agreeProgram = new AgreeASTBuilder().getAgreeProgram(si, strategy == VerificationStrategy.Monolithic);

		// generate different lustre depending on which model checker we are using

		Program program;
		if (AgreeUtils.usingKind2()) {
			if (strategy != VerificationStrategy.Monolithic) {
				throw new AgreeException("Kind2 now only supports monolithic verification");
			}
			program = LustreContractAstBuilder.getContractLustreProgram(agreeProgram);
		} else {
			program = LustreAstBuilder.getAssumeGuaranteeLustreProgram(agreeProgram);
		}
		List<Pair<String, Program>> consistencies = LustreAstBuilder.getConsistencyChecks(agreeProgram);

		wrapper.addChild(
				createVerification("Contract Guarantees", si, program, agreeProgram, AnalysisType.AssumeGuarantee));
		for (Pair<String, Program> consistencyAnalysis : consistencies) {
			wrapper.addChild(createVerification(consistencyAnalysis.getFirst(), si, consistencyAnalysis.getSecond(),
					agreeProgram, AnalysisType.Consistency));
		}
	}

	private AnalysisResult createVerification(String resultName, ComponentInstance compInst, Program lustreProgram,
			AgreeProgram agreeProgram, AnalysisType analysisType) {

		AgreeAutomaterRegistry aAReg = (AgreeAutomaterRegistry) ExtensionRegistry
				.getRegistry(ExtensionRegistry.AGREE_AUTOMATER_EXT_ID);
		List<AgreeAutomater> automaters = aAReg.getAgreeAutomaters();
		AgreeRenaming renaming = new AgreeRenaming();
		AgreeLayout layout = new AgreeLayout();
		Node mainNode = null;
		for (Node node : lustreProgram.nodes) {
			if (node.id.equals(lustreProgram.main)) {
				mainNode = node;
				break;
			}
		}

		if (mainNode == null) {
			throw new AgreeException("Could not find main lustre node after translation");
		}

		List<String> properties = new ArrayList<>();

		RenamingVisitor.addRenamings(lustreProgram, renaming, compInst, layout);
		addProperties(renaming, properties, mainNode, agreeProgram);

		for (AgreeAutomater aa : automaters) {
			renaming = aa.rename(renaming);
			layout = aa.transformLayout(layout);
		}

		JKindResult result;
		switch (analysisType) {
		case Consistency:
			result = new ConsistencyResult(resultName, mainNode.properties, Collections.singletonList(true), renaming);
			break;
		case Realizability:
			result = new JRealizabilityResult(resultName, renaming);
			break;
		case AssumeGuarantee:
			final Set<String> invertedProperties = renaming.getInvertedProperties();
			List<Boolean> invertedPropertyMask = mainNode.properties.stream().map(p -> invertedProperties.contains(p))
					.collect(Collectors.toList());
			result = new JKindResult(resultName, properties, invertedPropertyMask, renaming);
			break;
		default:
			throw new AgreeException("Unhandled Analysis Type");
		}

		queue.add(result);

		ComponentImplementation compImpl = AgreeUtils.getInstanceImplementation(compInst);
		linker.setProgram(result, lustreProgram);
		linker.setComponent(result, compImpl);
		linker.setContract(result, getContract(compImpl));
		linker.setLayout(result, layout);
		linker.setReferenceMap(result, renaming.getRefMap());
		linker.setLog(result, AgreeLogger.getLog());
		linker.setRenaming(result, renaming);

		return result;

	}

	private void addProperties(AgreeRenaming renaming, List<String> properties, Node mainNode,
			AgreeProgram agreeProgram) {

		// there is a special case in the AgreeRenaming which handles this
		// translation
		if (AgreeUtils.usingKind2()) {
			addKind2Properties(agreeProgram.topNode, properties, renaming, "_TOP", "");
		} else {
			properties.addAll(mainNode.properties);
		}

		Set<String> strs = new HashSet<>();
		for (String prop : properties) {
			String renamed = renaming.rename(prop);
			if (!strs.add(renamed)) {
				throw new AgreeException("Multiple properties named \"" + renamed + "\"");
			}
		}

	}

	private void addKind2Properties(AgreeNode agreeNode, List<String> properties, AgreeRenaming renaming,
			String prefix, String userPropPrefix) {
		int i = 0;

		String propPrefix = (userPropPrefix.equals("")) ? "" : userPropPrefix + ": ";
		for (AgreeStatement statement : agreeNode.lemmas) {
			renaming.addExplicitRename(prefix + "[" + (++i) + "]", propPrefix + statement.string);
			properties.add(prefix.replaceAll("\\.", AgreeASTBuilder.dotChar) + "[" + i + "]");
		}
		for (AgreeStatement statement : agreeNode.guarantees) {
			renaming.addExplicitRename(prefix + "[" + (++i) + "]", propPrefix + statement.string);
			properties.add(prefix.replaceAll("\\.", AgreeASTBuilder.dotChar) + "[" + i + "]");
		}

		userPropPrefix = userPropPrefix.equals("") ? "" : userPropPrefix + ".";
		for (AgreeNode subNode : agreeNode.subNodes) {
			addKind2Properties(subNode, properties, renaming, prefix + "." + subNode.id, userPropPrefix + subNode.id);
		}
	}

	private AgreeSubclause getContract(ComponentImplementation ci) {
		ComponentType ct = ci.getOwnedRealization().getImplemented();
		for (AnnexSubclause annex : ct.getOwnedAnnexSubclauses()) {
			if (annex instanceof AgreeSubclause) {
				return (AgreeSubclause) annex;
			}
		}
		return null;
	}

	private IStatus doAnalysis(final Element root, final IProgressMonitor globalMonitor) {

		Thread analysisThread = new Thread() {
			@Override
			public void run() {

				// Record the analysis start time and get model hashcode for
				// saving to property analysis log, if necessary
				String modelHash = "";
				long startTime = 0;
				if (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.PREF_PROP_LOG)) {
					try {
						modelHash = AgreeFileUtil.getModelHashcode(root);
						startTime = System.currentTimeMillis();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						return;
					}
				}


				KindApi api = PreferencesUtil.getKindApi();
				KindApi consistApi = PreferencesUtil.getConsistencyApi();
				JRealizabilityApi realApi = PreferencesUtil.getJRealizabilityApi();

				while (!queue.isEmpty() && !globalMonitor.isCanceled()) {
					JKindResult result = queue.peek();
					NullProgressMonitor subMonitor = new NullProgressMonitor();

					Program program = linker.getProgram(result);

					if (api instanceof JKindApi) {
						String resultName = result.getName();
						String adviceFileName = rerunAdviceMap.get(resultName);
						if (adviceFileName == null) {
							adviceFileName = "agree_advice" + adviceCount++;
							rerunAdviceMap.put(resultName, adviceFileName);
						} else {
							((JKindApi) api).setReadAdviceFile(adviceFileName);
						}
						((JKindApi) api).setWriteAdviceFile(adviceFileName);
					}

					try {
						if (result instanceof ConsistencyResult) {
							consistApi.execute(program, result, subMonitor);
						} else if (result instanceof JRealizabilityResult) {
							realApi.execute(program, (JRealizabilityResult) result, subMonitor);
						} else {
							api.execute(program, result, subMonitor);
						}
					} catch (JKindException e) {
						// TODO make sure error reporting is correct
						System.out.println("******** JKindException Text ********");
						e.printStackTrace(System.out);

						String errStr = e.getMessage();
						int l = Math.min(errStr.length(), 300);
						System.out.println(e.getMessage().substring(0, l));

						break;
					}

					// Print to property analysis log, if necessary
					if (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.PREF_PROP_LOG)) {
						AgreeFileUtil.printLog(result, startTime, modelHash);
					}

					queue.remove();
				}

				while (!queue.isEmpty()) {
					queue.remove().cancel();
				}

			}
		};
		analysisThread.start();
		while (analysisThread.isAlive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return Status.OK_STATUS;
	}

	@Override
	public void stop() {

	}

}
