package com.rockwellcollins.atc.agree.cli.results;

import java.util.List;

public class SyntaxValidationResults {
	private int warnings;
	private int errors;
	private List<SyntaxValidationIssue> issues;

	public SyntaxValidationResults() {

	}

	public SyntaxValidationResults(int warnings, int errors, List<SyntaxValidationIssue> issues) {
		this.warnings = warnings;
		this.errors = errors;
		if (!issues.isEmpty()) {
			this.issues = issues;
		}
	}

	public int getWarnings() {
		return this.warnings;
	}

	public int getErrors() {
		return this.errors;
	}

	public List<SyntaxValidationIssue> getIssues() {
		return this.issues;
	}

	public void setWarnings(int warnings) {
		this.warnings = warnings;
	}

	public void setErrors(int errors) {
		this.errors = errors;
	}

	public void setIssues(List<SyntaxValidationIssue> issues) {
		this.issues = issues;
	}
}
