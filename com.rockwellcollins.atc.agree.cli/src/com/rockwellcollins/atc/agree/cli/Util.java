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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

		final List<String> projectFiles = findFiles(Paths.get(projPath), "aadl");
		final File projectRootDirectory = new File(projPath);
		final File projectFile = new File(projectRootDirectory, ".project");
		final String projName = getProjectName(projectFile);

		for (String pFile : projectFiles) {
			final File projFile = new File(pFile);
			loadFile(projectRootDirectory, projName, projFile, resourceSet);
		}

		// load user specified AADL libs
		if (libArray != null) {
			for (String libName : libArray) {
				loadLibFile(libName, resourceSet);
			}
		}

		// load referenced project AADL files
		final String projParentPath = projectRootDirectory.getParent();
		final List<String> refProjNames = new ArrayList<>();
		getRefProjName(refProjNames, projName, projParentPath);
		for (String refProjName : refProjNames) {
			// assuming reference project is at the same level of main project
			final File refProj = new File(projParentPath, refProjName);
			final List<String> refProjFileNames = findFiles(refProj.toPath(), "aadl");
			for (String refProjFileName : refProjFileNames) {
				final File refProjFile = new File(refProjFileName);
				loadFile(projectRootDirectory, projName, refProjFile, resourceSet);
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
//				System.out.println("Validating " + resource.getURI().toString());
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
//					System.out.println(issue.getMessage());
				}
			}
		}

		return new SyntaxValidationResults(numWarnings, numErrors, syntaxValidationIssues);
	}

	private static List<String> findFiles(Path path, String fileExtension) throws Exception {

		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Path must be a directory!");
		}

		final List<String> result;

		try (Stream<Path> walk = Files.walk(path)) {
			result = walk
					.filter(p -> !Files.isDirectory(p))
					.map(p -> p.toString().toLowerCase())
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
//			System.out.println("Loading " + file.getAbsolutePath());
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

//			System.out.println("Loading " + filePath);
			res.load(stream, Collections.EMPTY_MAP);
			return res;
		} catch (IOException e) {
//			System.err.println("Error loading file " + filePath);
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
	private static void getRefProjName(List<String> list, String project, String parent) throws Exception {

		final File refProj = new File(parent, project);
		final File projectFile = new File(refProj, ".project");
		final List<String> refProjList = getReferenceProjectName(projectFile);
		if (!refProjList.isEmpty()) {
			for (String refProjName : refProjList) {
				// avoid duplicate and break circular reference
				if (!list.contains(refProjName)) {
					list.add(refProjName);
					getRefProjName(list, refProjName, parent);
				}
            }
		}
	}

	private static List<String> getReferenceProjectName(File projectFile) throws Exception {
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

	public static void writeOutput(AgreeOutput output, String outputPath) {

		// Convert to json
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
