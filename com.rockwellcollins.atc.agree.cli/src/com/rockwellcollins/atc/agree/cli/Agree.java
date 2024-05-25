package com.rockwellcollins.atc.agree.cli;

import java.io.File;
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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.Pair;
import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.AnnexSubclause;
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
import org.osate.pluginsupport.PluginSupportUtil;
import org.osate.xtext.aadl2.Aadl2StandaloneSetup;

import com.google.inject.Injector;
import com.rockwellcollins.atc.agree.AgreeStandaloneSetup;
import com.rockwellcollins.atc.agree.agree.AgreeSubclause;
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
import com.rockwellcollins.atc.agree.analysis.views.AgreeResultsLinker;
import com.rockwellcollins.atc.agree.cli.results.AgreeJsonResult;
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
	private final static String PROJECT = "p";
	private final static String COMP_IMPL = "c";
	private final static String OUTPUT = "o";
	private final static String VALIDATION_ONLY = "v";
	private final static String EXIT_ON_VALIDATION_WARNING = "w";
	private final static String FILES = "f";

	protected AgreeResultsLinker linker = new AgreeResultsLinker();
	protected Queue<JKindResult> queue = new ArrayDeque<>();
	private Map<String, String> rerunAdviceMap = new HashMap<>();
	private int adviceCount = 0;
	private enum AnalysisType {
		AssumeGuarantee, Consistency, Realizability
	};

	@Override
	public Object start(IApplicationContext context) throws Exception {

		System.out.println("Starting AGREE analysis");

		context.applicationRunning();

		// Read the meta information about the plug-ins to get the annex information.
		EcorePlugin.ExtensionProcessor.process(null);

		// Output Json object
		final AgreeOutput output = new AgreeOutput();
		output.setDate((new Date()).toString());

		// Process command line options
		String workspace = Activator.getWorkspace();
		String projPath = null;
		String component = null;
		String outputPath = null;
		String[] fileArray = null;
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
		options.addOption(NO_SPLASH, false, "optional, hide the splash screen, default false");
		options.addOption(DATA, true, "required, path of workspace");
		options.addOption(APPLICATION, true,
				"required, the name of this analysis (com.rockwellcollins.atc.agree.cli.Agree)");
		options.addOption(PROJECT, "project", true, "required, project path (relative to workspace)");
		options.addOption(COMP_IMPL, "compImpl", true, "qualified component implementation name");
		options.addOption(OUTPUT, "output", true, "output JSON file absolute path");
		options.addOption(VALIDATION_ONLY, "validationOnly", false, "validation only, default false");
		options.addOption(EXIT_ON_VALIDATION_WARNING, "exitOnValidtionWarning", false,
				"exit on validation warning, default false");
		Option option = new Option(FILES, "files", true, "Supplementary AADL files (absolute paths)");
		option.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(option);

		// parse options
		try {
			final CommandLineParser parser = new DefaultParser();
			final CommandLine commandLine = parser.parse(options, args);

			if (commandLine.hasOption(HELP)) {
				exit = true;
				output.setStatus(AgreeOutput.INTERRUPTED);
			}
			if (workspace == null || workspace.isBlank()) {
				exit = true;
				output.setStatus(AgreeOutput.INTERRUPTED);
				output.addStatusMessage("A workspace must be specified.");
			}
			if (commandLine.hasOption(COMP_IMPL)) {
				component = commandLine.getOptionValue(COMP_IMPL);
				output.setComponent(component);
				// expects qualified name
				if (!component.contains("::")) {
					output.setStatus(AgreeOutput.INTERRUPTED);
					output.addStatusMessage("Component implementation qualified name must be specified.");
					exit = true;
				}
			}
			if (commandLine.hasOption(PROJECT)) {
				projPath = workspace + File.separator + commandLine.getOptionValue(PROJECT);
				output.setProject(projPath);
			} else {
				output.setStatus(AgreeOutput.INTERRUPTED);
				output.addStatusMessage("Project path must be specified.");
				exit = true;
			}
			if (commandLine.hasOption(OUTPUT)) {
				outputPath = commandLine.getOptionValue(OUTPUT);
			}
			if (commandLine.hasOption(FILES)) {
				fileArray = commandLine.getOptionValues(FILES);
			}
			if (commandLine.hasOption(VALIDATION_ONLY)) {
				validationOnly = true;
			}
			if (commandLine.hasOption(EXIT_ON_VALIDATION_WARNING)) {
				exitOnValidationWarning = true;
			}
			final String[] remainder = commandLine.getArgs();
			for (String argument : remainder) {
				final String message = "WARNING: unknown arguement " + argument
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
			resourceSet.getResource(uri, true);
		}

		// Load project AADL files
		try {
			Util.loadProjectAadlFiles(projPath, fileArray, resourceSet);
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
				final List<AgreeJsonResult> results = runAgree(compImpl);
				output.setStatus(AgreeOutput.COMPLETED);
				output.setResults(results);
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

	private List<AgreeJsonResult> runAgree(ComponentImplementation compImpl) {

		try {
			final SystemInstance si = InstantiateModel.instantiate(compImpl);
			AnalysisResult result;
			CompositeAnalysisResult wrapper = new CompositeAnalysisResult("");

//			if (isRecursive()) {
//				if (AgreeUtils.usingKind2()) {
//					throw new AgreeException("Kind2 only supports monolithic verification");
//				}
//				result = buildAnalysisResult(ci.getName(), si);
//				wrapper.addChild(result);
//				result = wrapper;
//			} else if (isRealizability()) {
//				AgreeProgram agreeProgram = new AgreeASTBuilder().getAgreeProgram(si, false);
//
//				Program program = LustreAstBuilder.getRealizabilityLustreProgram(agreeProgram);
//				wrapper.addChild(createVerification("Realizability Check", si, program, agreeProgram,
//						AnalysisType.Realizability));
//				result = wrapper;
//			} else {
				CompositeAnalysisResult wrapperTop = new CompositeAnalysisResult("Verification for " + compImpl.getName());
				wrapVerificationResult(si, wrapperTop);
				wrapper.addChild(wrapperTop);
				result = wrapper;
//			}

//			showView(result, linker);
			doAnalysis(compImpl, new NullProgressMonitor());
			System.out.println(result.toString());
		} catch (Throwable e) {
			// TODO
//			String messages = getNestedMessages(e);
//			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, messages, e);
		}

		return null;
	}

	private void wrapVerificationResult(ComponentInstance si, CompositeAnalysisResult wrapper) {
//		AgreeProgram agreeProgram = new AgreeASTBuilder().getAgreeProgram(si, isMonolithic());
		AgreeProgram agreeProgram = new AgreeASTBuilder().getAgreeProgram(si, false);


		// generate different lustre depending on which model checker we are
		// using

		Program program;
		if (AgreeUtils.usingKind2()) {
//			if (!isMonolithic()) {
				throw new AgreeException("Kind2 now only supports monolithic verification");
//			}
//			program = LustreContractAstBuilder.getContractLustreProgram(agreeProgram);
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

		/////////
//		Logger logger = Logger.getLogger("MyLog");
//		FileHandler fh;
//		try {

		// This block configure the logger with handler and formatter
//			fh = new FileHandler("/home/thomas/zzz.log");
//			logger.addHandler(fh);
//			SimpleFormatter formatter = new SimpleFormatter();
//			fh.setFormatter(formatter);
//			logger.info("Agree Program:");
//			AgreeASTPrettyprinter pp = new AgreeASTPrettyprinter();
//			pp.visit(agreeProgram);
//			logger.info(pp.toString());

//		} catch (SecurityException ex) {
//			ex.printStackTrace();
//		}
//		catch (IOException ex) {
//			ex.printStackTrace();
//		}
		////////////////

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

		// System.out.println(program);
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

//				try {
//					activateTerminateHandlers(globalMonitor);
					KindApi api = PreferencesUtil.getKindApi();
					KindApi consistApi = PreferencesUtil.getConsistencyApi();
					JRealizabilityApi realApi = PreferencesUtil.getJRealizabilityApi();

					while (!queue.isEmpty() && !globalMonitor.isCanceled()) {
						JKindResult result = queue.peek();
						NullProgressMonitor subMonitor = new NullProgressMonitor();
//						monitorRef.set(subMonitor);

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
//				} finally {
//					deactivateTerminateHandlers();
//					enableRerunHandler(root);
//				}

			}
		};
		analysisThread.start();
		while (analysisThread.isAlive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Status.OK_STATUS;
	}

	@Override
	public void stop() {

	}

}
