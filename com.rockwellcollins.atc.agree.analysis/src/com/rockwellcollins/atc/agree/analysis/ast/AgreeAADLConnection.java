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
package com.rockwellcollins.atc.agree.analysis.ast;

import org.eclipse.emf.ecore.EObject;

import com.rockwellcollins.atc.agree.analysis.ast.visitors.AgreeASTVisitor;

import jkind.Assert;

public class AgreeAADLConnection implements AgreeConnection {

	public enum ConnectionType {
		EVENT, DATA
	}

	public final AgreeNode sourceNode;
	public final AgreeNode destinationNode;
	public final AgreeVar sourceVarName;
	public final AgreeVar destinationVarName;
	public final ConnectionType type;
	public final EObject reference;
	public final boolean latched;
	public final boolean delayed;

	public AgreeAADLConnection(AgreeNode sourceNode, AgreeNode destinationNode, AgreeVar sourceVarName,
			AgreeVar destinationVarName, ConnectionType type, boolean latched, boolean delayed, EObject reference) {

		Assert.isNotNull(sourceVarName);
		Assert.isNotNull(destinationVarName);
		Assert.isNotNull(type);
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.sourceVarName = sourceVarName;
		this.destinationVarName = destinationVarName;
		this.type = type;
		this.latched = latched;
		this.delayed = delayed;
		this.reference = reference;
	}

	@Override
	public <T> T accept(AgreeASTVisitor<T> visitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AgreeAADLConnection) {
			AgreeAADLConnection otherAgreeAADLConnection = (AgreeAADLConnection) other;
			return ((sourceNode == null) ? (otherAgreeAADLConnection.sourceNode == null)
					: sourceNode.equals(otherAgreeAADLConnection.sourceNode))
					&& ((destinationNode == null) ? (otherAgreeAADLConnection.destinationNode == null)
							: destinationNode.equals(
									otherAgreeAADLConnection.destinationNode))
					&& sourceVarName.equals(otherAgreeAADLConnection.sourceVarName)
					&& destinationVarName.equals(otherAgreeAADLConnection.destinationVarName)
					&& type.equals(otherAgreeAADLConnection.type) && latched == otherAgreeAADLConnection.latched
					&& delayed == otherAgreeAADLConnection.delayed;
		}
		return false;
	}
}