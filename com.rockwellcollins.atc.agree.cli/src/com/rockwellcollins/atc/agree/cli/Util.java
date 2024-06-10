package com.rockwellcollins.atc.agree.cli;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.rockwellcollins.atc.agree.cli.results.AgreeOutput;
import com.rockwellcollins.atc.agree.cli.results.SyntaxValidationIssue;
import com.rockwellcollins.atc.agree.cli.results.SyntaxValidationResults;

public class Util {

	// Load project specific AADL files
	public static void loadProjectAadlFiles(String projPath, String[] libArray, XtextResourceSet resourceSet)
			throws Exception {

		final String workspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		final Path absProjPath = Paths.get(workspace, projPath);
		final List<String> projectFiles = findFiles(absProjPath, "aadl");
		final File projectFile = new File(absProjPath.toFile(), ".project");
		final String projName = getProjectName(projectFile);

		// Add project to workspace
		addProjectToWorkspace(projPath, projName);

		// Map project folder to project name (they could be different)
		final Map<String, String> projMap = new HashMap<>();
		Path topLevelDir = absProjPath;
		for (int i = 0; i < URI.createURI(projPath).segmentCount() - 1; ++i) {
			if (topLevelDir.getParent() != null) {
				topLevelDir = topLevelDir.getParent();
			}
		}

		final List<String> dotProjFiles = findFiles(topLevelDir, "project");
		for (String dotProjFile : dotProjFiles) {
			final File projFile = new File(dotProjFile);
			projMap.put(getProjectName(projFile), projFile.getParentFile().getAbsolutePath());
		}

		for (String pFile : projectFiles) {
			final File projFile = new File(pFile);
			loadFile(absProjPath.toFile(), projName, projFile, resourceSet);
		}

		// load user specified AADL libs
		if (libArray != null) {
			for (String libName : libArray) {
				loadLibFile(libName, resourceSet);
			}
		}

		// load referenced project AADL files
		final List<String> refProjNames = new ArrayList<>();
		getRefProjName(refProjNames, projName, projMap);
		for (String refProjName : refProjNames) {
			// Add reference project to workspace
			addProjectToWorkspace(projMap.get(refProjName), refProjName);

			final File refProj = new File(projMap.get(refProjName));
			final List<String> refProjFileNames = findFiles(refProj.toPath(), "aadl");
			for (String refProjFileName : refProjFileNames) {
				final File refProjFile = new File(refProjFileName);
				loadFile(new File(projMap.get(refProjName)), refProjName, refProjFile, resourceSet);
			}
		}
	}

	// Validation resource set
	public static SyntaxValidationResults validateResourceSet(XtextResourceSet resourceSet) {

		final List<SyntaxValidationIssue> syntaxValidationIssues = new ArrayList<>();
		int numErrors = 0;
		int numWarnings = 0;
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI().isPlatformResource()) {
				final IResourceValidator validator = ((XtextResource) resource).getResourceServiceProvider()
						.getResourceValidator();
				final List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
				for (Issue issue : issues) {
					final SyntaxValidationIssue valIssue = new SyntaxValidationIssue();
					if (issue.getSeverity().equals(Severity.ERROR)) {
						++numErrors;
						valIssue.setSeverity("error");
					} else if (issue.getSeverity().equals(Severity.WARNING)) {
						++numWarnings;
						valIssue.setSeverity("warning");
					}
					valIssue.setIssue(issue.getMessage());
					valIssue.setFile(resource.getURI().toPlatformString(true));
					valIssue.setLine(issue.getLineNumber());
					syntaxValidationIssues.add(valIssue);
				}
			}
		}

		return new SyntaxValidationResults(numWarnings, numErrors, syntaxValidationIssues);
	}

	private static List<String> findFiles(Path path, String fileExtension) throws Exception {

		final List<String> result;

		try (Stream<Path> walk = Files.walk(path)) {
			result = walk
					.filter(p -> !Files.isDirectory(p))
					.map(p -> p.toString())
					.filter(f -> f.endsWith(fileExtension))
					.collect(Collectors.toList());
		}
		return result;
	}

	/** Adapted from
	 * <a href="https://github.com/osate/osate2/blob/44af9ff8d6309410aeb134a0ae825aa7c916fabf/core/org.osate.testsupport/src/org/osate/testsupport/TestHelper.java#L147">here</a>.
	 *
	 * load file as Xtext resource into resource set
	 * @param filePath String
	 * @param rs ResourceSet
	 * @return
	 */
	//https://github.com/sireum/osate-plugin/blob/master/org.sireum.aadl.osate/src/main/java/org/sireum/aadl/osate/util/AadlProjectUtil.java

	private static Resource loadFile(File projectRootDirectory, String projectName, File file, ResourceSet rs)
			throws Exception {

		final URL url = new URL("file:" + file.getAbsolutePath());
		InputStream stream = null;
		try {
			stream = url.openConnection().getInputStream();
		} catch (Exception ex) {
			throw new Exception("Error loading file " + file.toString());
		}

		final String normalizedRelPath = relativize(projectRootDirectory, file).replace("\\", "/");

		// came up with this uri by comparing what OSATE IDE serialized AIR produces
		final URI resourceUri = URI.createPlatformResourceURI(projectName + "/" + normalizedRelPath, true);
		final Resource res = rs.createResource(resourceUri);
		if (res == null) {
			throw new Exception("Error loading file " + projectName + "/" + normalizedRelPath);
		}
		try {
			res.load(stream, Collections.EMPTY_MAP);
			return res;
		} catch (IOException e) {
			throw new Exception("Error loading file " + projectName + "/" + normalizedRelPath);
		}
	}

	// User specified AADL file path may be outside workspace, cannot use relative path from main project
	private static Resource loadLibFile(String filePath, ResourceSet rs) {
		try {
			final URL url = new URL("file:" + filePath);
			final InputStream stream = url.openConnection().getInputStream();

			final Path path = Paths.get(filePath);
			final URI resourceUri = URI.createPlatformResourceURI("Lib/" + path.getFileName().toString(), true);
			final Resource res = rs.createResource(resourceUri);

			res.load(stream, Collections.EMPTY_MAP);
			return res;
		} catch (IOException e) {
			return null;
		}
	}

	private static String relativize(File root, File other) {
		return Paths.get(root.toURI()).relativize(Paths.get(other.toURI())).toString();
	}

	private static String getProjectName(File projectFile) throws Exception {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document doc = builder.parse(projectFile);
		doc.getDocumentElement().normalize();
		final org.w3c.dom.Element root = doc.getDocumentElement();
		final NodeList list = doc.getElementsByTagName("name");

		for (int i = 0; i < list.getLength(); i++) {
			final Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
				final org.w3c.dom.Element element = (org.w3c.dom.Element) node;
            	// "name" tag could be used at lower level e.g. "<buildCommand>"
            	if (element.getParentNode().isEqualNode(root)) {
					return element.getTextContent();
            	}
            }
		}
		throw new Exception("Error getting project name from file " + projectFile.toString());
	}

	// A reference project could depend on another reference project
	private static void getRefProjName(List<String> list, String projectName, Map<String, String> projMap)
			throws Exception {

		final File projectFile = new File(projMap.get(projectName), ".project");
		if (!projectFile.exists()) {
			throw new Exception("Project " + projectName + " was not found in the workspace.");
		}
		final List<String> refProjList = getReferenceProjectNames(projectFile);
		if (!refProjList.isEmpty()) {
			for (String refProjName : refProjList) {
				// avoid duplicate and break circular reference
				if (!list.contains(refProjName)) {
					list.add(refProjName);
					getRefProjName(list, refProjName, projMap);
				}
			}
		}
		}

	private static List<String> getReferenceProjectNames(File projectFile) throws Exception {
		final List<String> refProjNameList = new ArrayList<>();
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document doc = builder.parse(projectFile);
		doc.getDocumentElement().normalize();
		final NodeList list = doc.getElementsByTagName("projects");

		for (int i = 0; i < list.getLength(); i++) {
			final Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
				final org.w3c.dom.Element element = (org.w3c.dom.Element) node;
				final Node projNode = element.getElementsByTagName("project").item(0);
            	// Handle empty reference project list
            	if (projNode != null) {
					refProjNameList.add(projNode.getTextContent());
            	}
            }
		}
		return refProjNameList;
	}

	/**
	 * When running from the command line, project must be explicitly added to the workspace
	 * @param projPath - Absolute path to project directory
	 * @param projName - Name of project
	 */
	private static void addProjectToWorkspace(String projPath, String projName) throws Exception {

		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
		final IProjectDescription projectDescription = ResourcesPlugin.getWorkspace()
				.newProjectDescription(project.getName());
		final String absProjPath = Paths
				.get(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString(), projPath)
				.toString();
		projectDescription.setLocation(new org.eclipse.core.runtime.Path(absProjPath));

		try {
			if (!project.exists()) {
				project.create(projectDescription, new NullProgressMonitor());
			}
		} catch (CoreException e) {
			boolean name = ResourcesPlugin.getWorkspace().validateName(projName, IResource.PROJECT).isOK();
			boolean loc = ResourcesPlugin.getWorkspace()
					.validateProjectLocation(project, new org.eclipse.core.runtime.Path(absProjPath))
					.isOK();
			throw new Exception("Problem creating project " + projName + " at " + projPath + ": " + e.getMessage()
					+ " Project name is " + (name ? "valid" : "not valid") + ". Project location is "
					+ (loc ? "valid" : "not valid") + ".");
		}

		try {
			if (!project.isOpen()) {
				project.open(new NullProgressMonitor());
			}
		} catch (CoreException e) {
			boolean name = ResourcesPlugin.getWorkspace().validateName(projName, IResource.PROJECT).isOK();
			boolean loc = ResourcesPlugin.getWorkspace()
					.validateProjectLocation(project, new org.eclipse.core.runtime.Path(absProjPath))
					.isOK();
			throw new Exception("Problem opening project " + projName + " at " + projPath + ": " + e.getMessage()
					+ " Project name is " + (name ? "valid" : "not valid") + ". Project location is "
					+ (loc ? "valid" : "not valid") + ".");
		}
	}

	public static void writeOutput(AgreeOutput output, String outputPath) {

		ExclusionStrategy exStrat = new ExclusionStrategy() {

			@Override
			public boolean shouldSkipClass(Class<?> arg0) {
				return false;
			}

			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				return (f.getName().equals("pcs") || f.getName().equals("ticker")
						|| f.getName().equals("renaming")
						|| f.getName().equals("parent"));
			}
		};

		// Convert to json
		final Gson gson = new GsonBuilder().setExclusionStrategies(exStrat)
				.setPrettyPrinting()
				.disableHtmlEscaping()
				.create();
		try {
			if (outputPath != null) {
				final File outputFile = new File(outputPath);
				final JsonWriter jsonWriter = new JsonWriter(new FileWriter(outputFile));
				jsonWriter.setIndent("    ");
				gson.toJson(output, output.getClass(), jsonWriter);
				jsonWriter.close();
			}
		} catch (Exception e) {
			System.out.println("Error writing to " + outputPath);
		}

		// Write to std out
		System.out.println(gson.toJson(output));
	}

}
