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
package com.rockwellcollins.atc.agree.analysis.saving;

import java.util.List;

public class AgreeLogResult {

	private String name = null;
	private String result = null;
	private Long timestamp = null;
	private String hashcode = null;
	private List<AgreeLogResult> analyses = null;

	public AgreeLogResult() {

	}

	public String getName() {
		return name;
	}

	public String getResult() {
		return result;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getHashcode() {
		return hashcode;
	}

	public List<AgreeLogResult> getAnalyses() {
		return analyses;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}

	public void setAnalyses(List<AgreeLogResult> analyses) {
		this.analyses = analyses;
	}

}
