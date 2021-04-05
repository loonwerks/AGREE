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
package com.rockwellcollins.atc.tcg.views;

import java.util.StringJoiner;

import jkind.api.results.MultiStatus;
import jkind.api.results.Status;

public class TcgMultiStatus {
	final private MultiStatus ms;

	public TcgMultiStatus(MultiStatus ms) {
		this.ms = ms;
	}

	public int getCount(Status status) {
		return ms.getCount(status);
	}

	public void add(Status status) {
		ms.add(status);
	}

	public void add(TcgMultiStatus other) {
		ms.add(other.ms);
	}

	public void remove(Status status) {
		ms.remove(status);
	}

	public void remove(TcgMultiStatus other) {
		ms.remove(other.ms);
	}

	private static final Status[] PRECEDENCE = new Status[] { Status.WORKING, Status.WAITING,
			Status.ERROR, Status.INVALID, Status.INCONSISTENT, Status.UNKNOWN, Status.CANCELED,
			Status.VALID };

	public Status getOverallStatus() {
		for (Status status : PRECEDENCE) {
			if (getCount(status) > 0) {
				return status;
			}
		}
		return null;
	}

	public String tcgStatus(Status status) {
		switch (status) {
		case INVALID: return "test case found" ;
		case VALID: return "no test case possible" ;
		default: return status.toString() ;
		}
	}

	@Override
	public String toString() {
		StringJoiner text = new StringJoiner(", ");
		for (Status status : PRECEDENCE) {
			int count = getCount(status);
			if (count > 0) {
				text.add(count + " " + tcgStatus(status));
			}
		}

		return text.toString();
	}
}
