package com.rockwellcollins.atc.agree.cli.results;

public class SyntaxValidationIssue {
	private String severity;
	private String issue;
	private String file;
	private int line;

	public SyntaxValidationIssue() {

	}

	public SyntaxValidationIssue(String severity, String issue, String file, int line) {
		this.severity = severity;
		this.issue = issue;
		this.file = file;
		this.line = line;
	}

	public String getSeverity() {
		return this.severity;
	}

	public String getIssue() {
		return this.issue;
	}

	public String getFile() {
		return this.file;
	}

	public int getLine() {
		return this.line;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setLine(int line) {
		this.line = line;
	}

}
