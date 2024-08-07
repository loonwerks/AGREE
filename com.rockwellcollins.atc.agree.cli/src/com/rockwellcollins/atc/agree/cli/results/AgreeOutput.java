package com.rockwellcollins.atc.agree.cli.results;

import java.util.ArrayList;

import jkind.api.results.CompositeAnalysisResult;

public class AgreeOutput {

	public final static String COMPLETED = "Analysis Completed";
	public final static String INTERRUPTED = "Analysis Interrupted";
	public final static String VALID = "No Counterexamples Found";
	public final static String INVALID = "Counterexamples Found";

	private String date;
	private String project;
	private String component;
	private ArrayList<String> statusMessages;
	private String status;
	private SyntaxValidationResults syntaxValidation;
	private CompositeAnalysisResult results;

	public AgreeOutput() {

	}

	public AgreeOutput(AgreeOutput output) {
		setDate(output.getDate());
		setProject(output.getProject());
		setComponent(output.getComponent());
		setStatusMessages(output.getStatusMessages());
		setStatus(output.getStatus());
		setSyntaxValidationResults(output.getSyntaxValidationResults());
	}

	public String getDate() {
		return this.date;
	}

	public String getProject() {
		return this.project;
	}

	public String getComponent() {
		return this.component;
	}

	public String getStatus() {
		return this.status;
	}

	public ArrayList<String> getStatusMessages() {
		return this.statusMessages;
	}

	public SyntaxValidationResults getSyntaxValidationResults() {
		return this.syntaxValidation;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusMessages(ArrayList<String> statusMessages) {
//		if (this.message == null || this.message.isBlank()) {
//			this.message = message;
//		} else {
//			this.message += System.lineSeparator() + message;
//		}
		this.statusMessages = statusMessages;
	}

	public void addStatusMessage(String statusMessages) {
		if (this.statusMessages == null) {
			this.statusMessages = new ArrayList<>();
		}
		this.statusMessages.add(statusMessages);
	}

	public void setSyntaxValidationResults(SyntaxValidationResults syntaxValidationResults) {
		this.syntaxValidation = syntaxValidationResults;
	}


	public CompositeAnalysisResult getResults() {
		return this.results;
	}

	public void setResults(CompositeAnalysisResult results) {
		this.results = results;
	}
}
