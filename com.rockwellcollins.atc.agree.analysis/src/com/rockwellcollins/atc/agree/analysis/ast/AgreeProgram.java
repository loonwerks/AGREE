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

import java.util.List;

import com.rockwellcollins.atc.agree.analysis.ast.visitors.AgreeASTVisitor;

import jkind.lustre.Function;
import jkind.lustre.Node;
import jkind.lustre.Type;

public class AgreeProgram implements AgreeASTElement {
	public final List<AgreeNode> agreeNodes;
	public final List<Node> globalLustreNodes;
	public final List<Function> uninterpretedFunctions;
	public final List<Type> globalTypes;
	public final AgreeNode topNode;
	public boolean containsRealTimePatterns;

	public AgreeProgram(List<AgreeNode> agreeNodes, List<Node> globalLustreNodes, List<Function> uninterpretedFunctions,
			List<Type> globalTypes, AgreeNode topNode) {
		this.agreeNodes = jkind.util.Util.safeList(agreeNodes);
		this.globalLustreNodes = jkind.util.Util.safeList(globalLustreNodes);
		this.uninterpretedFunctions = jkind.util.Util.safeList(uninterpretedFunctions);
		this.globalTypes = jkind.util.Util.safeList(globalTypes);
		this.topNode = topNode;
		containsRealTimePatterns = false;
	}

	public AgreeProgram(List<AgreeNode> agreeNodes, List<Node> globalLustreNodes, List<Function> uninterpretedFunctions,
			List<Type> globalTypes, AgreeNode topNode, boolean containsRealTimePatterns) {
		this.agreeNodes = jkind.util.Util.safeList(agreeNodes);
		this.globalLustreNodes = jkind.util.Util.safeList(globalLustreNodes);
		this.uninterpretedFunctions = jkind.util.Util.safeList(uninterpretedFunctions);
		this.globalTypes = jkind.util.Util.safeList(globalTypes);
		this.topNode = topNode;
		this.containsRealTimePatterns = containsRealTimePatterns;
	}

	@Override
	public <T> T accept(AgreeASTVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public void containsRealTimePatterns(boolean containsRTPatterns) {
		containsRealTimePatterns = containsRTPatterns;
	}

}
