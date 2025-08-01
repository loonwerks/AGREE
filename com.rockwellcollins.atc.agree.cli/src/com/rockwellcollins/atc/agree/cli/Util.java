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
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
	public static void loadProjectAadlFiles(Path workspace, Path projectPath, String[] libArray,
			XtextResourceSet resourceSet)
			throws Exception {

		final List<File> projectAadlFiles = findFiles(projectPath, "aadl");
		final File projectFile = projectPath.resolve(".project").toFile();
		final String projectName = getProjectName(projectFile);

		EcorePlugin.getPlatformResourceMap().put(projectName, URI.createFileURI(projectPath.toString()));

		for (File pFile : projectAadlFiles) {
			loadFile(projectPath, projectName, pFile, resourceSet);
		}

		// load user specified AADL libs
		if (libArray != null) {
			for (String libName : libArray) {
				loadLibFile(libName, resourceSet);
			}
		}

		// load referenced project AADL files
		final Map<String, List<Path>> projectMap = findProjects(workspace);
		// If there are multiple projects in the workspace with the same name as the project being analyzed,
		// ignore them by removing from list
		final ListIterator<Path> i = projectMap.get(projectName).listIterator();
		while (i.hasNext()) {
			final Path p = i.next();
			if (p.compareTo(projectPath) != 0) {
				i.remove();
			}
		}

		final List<Path> refProjPaths = new ArrayList<>();
		getRefProjPaths(refProjPaths, projectName, projectMap);
		for (Path refProjPath : refProjPaths) {
			final List<File> refProjFiles = findFiles(refProjPath, "aadl");
			for (File refProjFile : refProjFiles) {
				loadFile(projectPath, projectName, refProjFile, resourceSet);
			}
		}

		EcoreUtil.resolveAll(resourceSet);

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
					} else {
						continue;
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

	public static boolean reflectDoSetup(String className) {
		try {
			Class.forName(className).getMethod("doSetup").invoke(null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static List<File> findFiles(Path path, String fileExtension) throws Exception {

		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Path must be a directory!");
		}

		final List<File> result;

		try (Stream<Path> walk = Files.walk(path)) {
			result = walk
					.filter(p -> !Files.isDirectory(p))
					.filter(p -> p.toString().toLowerCase().endsWith(fileExtension))
					.map(p -> p.toFile())
					.collect(Collectors.toList());
		}
		return result;
	}


	// Finds all projects in workspace
	// Returns a map of project name to project path
	private static Map<String, List<Path>> findProjects(Path workspace) throws Exception {

		if (!Files.isDirectory(workspace)) {
			throw new IllegalArgumentException("Workspace must be a directory!");
		}

		final List<Path> projectPaths;

		try (Stream<Path> walk = Files.walk(workspace)) {
			projectPaths = walk.filter(p -> !Files.isDirectory(p))
					.filter(f -> f.toString().toLowerCase().endsWith(".project"))
					.collect(Collectors.toList());
		}

		final Map<String, List<Path>> projectMap = new HashMap<>();
		for (Path p : projectPaths) {
			final String projectName = getProjectName(p.toFile());
			if (projectMap.containsKey(projectName)) {
				projectMap.get(projectName).add(p.getParent());
			} else {
				final List<Path> projectPath = new ArrayList<>();
				projectPath.add(p.getParent());
				projectMap.put(projectName, projectPath);
			}
		}

		return projectMap;
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

	private static Resource loadFile(Path projectRootDirectory, String projectName, File file, ResourceSet rs)
			throws Exception {

		final URL url = new URL("file:" + file.getAbsolutePath());
		InputStream stream = null;
		try {
			stream = url.openConnection().getInputStream();
		} catch (Exception ex) {
			throw new Exception("Error loading file " + file.toString());
		}

		final String normalizedRelPath = relativize(projectRootDirectory.toFile(), file).replace("\\", "/");

		// came up with this uri by comparing what OSATE IDE serialized AIR produces
		final URI resourceUri = URI.createPlatformResourceURI(projectName + "/" + normalizedRelPath, true);
		final Resource res = rs.createResource(resourceUri);
		if (res == null) {
			throw new Exception("Error loading file /" + projectName + "/" + normalizedRelPath);
		}
		try {
			res.load(stream, Collections.EMPTY_MAP);
			return res;
		} catch (IOException e) {
			throw new Exception("Error loading file /" + projectName + "/" + normalizedRelPath);
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

	// Maps reference project names to project paths
	// Note there could be multiple projects with the same name in the workspace
	// A reference project could depend on another reference project
	private static void getRefProjPaths(List<Path> refProjPaths, String projectName, Map<String, List<Path>> projectMap)
			throws Exception {

		List<Path> projectPaths = projectMap.get(projectName);
		Path projectPath = null;
		if (projectPaths.size() == 1) {
			projectPath = projectPaths.get(0);
		} else {
			throw new Exception("Multiple projects found in workspace with same name: " + projectName);
		}
		final File projectFile = projectPath.resolve(".project").toFile();
		final List<String> refProjList = getReferenceProjectNames(projectFile);
		for (String refProjName : refProjList) {

			projectPaths = projectMap.get(refProjName);
			projectPath = null;
			if (projectPaths.size() == 1) {
				projectPath = projectPaths.get(0);
			} else {
				throw new Exception("Multiple projects found in workspace with same name: " + refProjName);
			}

			// avoid duplicate and break circular reference
			if (!refProjPaths.contains(projectPath)) {
				refProjPaths.add(projectPath);
				getRefProjPaths(refProjPaths, refProjName, projectMap);
			}
		}
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


	public static void writeOutput(AgreeOutput output, Path outputPath) {

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
				final File outputFile = outputPath.toFile();
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
