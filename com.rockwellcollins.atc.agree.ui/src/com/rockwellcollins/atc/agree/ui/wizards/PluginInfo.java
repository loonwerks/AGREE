/*
 * Copyright (c) 2022, Collins Aerospace.
 * Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this data,
 * including any software or models in source or binary form, as well as any drawings, specifications,
 * and documentation (collectively &quot;the Data&quot;), to deal in the Data without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Data.
 *
 * THE DATA IS PROVIDED &quot;AS IS&quot;, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
 */

package com.rockwellcollins.atc.agree.ui.wizards;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

public class PluginInfo {

	public PluginInfo() {
	}

	public PluginInfo(URL exampleURI, URL readmeURI, String name, String category, String bundle) {
		this.exampleURI = exampleURI;
		this.readmeURI = readmeURI;
		this.name = name;
		this.category = category;
		this.bundle = bundle;
	}

	public PluginInfo(String name) {
		this.name = name;
	}

	public String exampleS;
	public URL exampleURI;
	public URL readmeURI;
	public String name;
	public String category;
	public String bundle;
	public List<String> projectPath;
	protected List<PluginInfo> nodes = new ArrayList<>();
	protected PluginInfo parent;

	public List<PluginInfo> getNode() {
		return nodes;
	}

	public void addProjectPath(String path) {
		if (this.projectPath == null) {
			this.projectPath = new ArrayList<String>();
		}

		this.projectPath.add(path);
	}

	protected void addNode(PluginInfo node) {
		nodes.add(node);
		node.parent = this;
	}

	protected PluginInfo getParent() {
		return parent;
	}

	public IProject getProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}

	public IFile getWorkspaceFile() {
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(exampleURI.getPath()));
	}

}
