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
package com.rockwellcollins.atc.agree.codegen.util;

import jkind.Assert;

/**
 *
 * UniqueID class contains the variable/function ID and its record type if applicable
 * This is used in ensuring unique names are created during the translation process.
 *
 */
public class UniqueID {

	public final String id;
	public final String recordId;

	public UniqueID(String id, String recordId) {
		Assert.isNotNull(id);
		Assert.isNotNull(recordId);
		this.id = id;
		this.recordId = recordId;
	}

	@Override
	public String toString() {
		return id + " : " + recordId + "\n";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UniqueID)) {
			return false;
		} else {
			UniqueID otherID = (UniqueID) obj;
			if (id.equals(otherID.id) && recordId.equals(otherID.recordId)) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public int hashCode() {
		int hash = id.hashCode();
		hash = hash * 31 + recordId.hashCode();
		return hash;
	}

}
