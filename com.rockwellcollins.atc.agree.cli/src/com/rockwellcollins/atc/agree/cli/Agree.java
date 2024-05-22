package com.rockwellcollins.atc.agree.cli;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.ComponentImplementation;
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
import com.rockwellcollins.atc.agree.cli.results.AgreeJsonResult;
import com.rockwellcollins.atc.agree.cli.results.AgreeOutput;
import com.rockwellcollins.atc.agree.cli.results.SyntaxValidationResults;


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
				"required, the name of this analysis (com.rockwellcollins.atc.resolute.cli.Resolute)");
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
				final SystemInstance si = InstantiateModel.instantiate(compImpl);
				final List<AgreeJsonResult> results = runAgree();
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
	
	private List<AgreeJsonResult> runAgree() {
		return null;
	}

	@Override
	public void stop() {

	}

}
