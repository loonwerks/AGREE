/*
Copyright (c) 2015, Rockwell Collins.
Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).

Permission is hereby granted, free of charge, to any person obtaining a copy of this data,
including any software or models in source or binary form, as well as any drawings, specifications,
and documentation (collectively "the Data"), to deal in the Data without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Data.

THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
*/
package edu.uah.rsesc.aadlsimulator.ui;

import java.util.Objects;

import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.ge.services.GraphicalEditorService;
import org.osgi.framework.FrameworkUtil;

public class GraphicalEditorSelectionHelper {
	private final GraphicalEditorService editorService;

	public GraphicalEditorSelectionHelper() {
		editorService = Objects.requireNonNull(EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(getClass()).getBundleContext()).get(GraphicalEditorService.class), "unable to retrieve Graphical Editor Service");
	}

	/**
	 * Returns a component implementation based on the edit part.
	 * If a component implementation can be retrieved based on the diagram in which the edit part is contained, then it is returned.
	 * Otherwise the component implementation(if any) which is related to the specified edit part is returned.
	 * @param object
	 */
	public ComponentImplementation getComponentImplementationByApplicableObject(final Object object) {
		return editorService.getObjectDetails(object).map(d -> {
			return d.getDiagramBusinessObject()
					.map(GraphicalEditorSelectionHelper::getComponentImplementationByBusinessObject)
					.orElseGet(() -> getComponentImplementationByBusinessObject(d.getBusinessObject()));
		}).orElse(null);
	}

	/**
	 * Returns a component implementation for the specified business object. The business object may be a component implementation or a component instance.
	 * @param bo
	 * @return
	 */
	private static ComponentImplementation getComponentImplementationByBusinessObject(final Object bo) {
		if(bo instanceof ComponentImplementation) {
			return (ComponentImplementation)bo;
		} else if(bo instanceof ComponentInstance) {
			final ComponentInstance ci = (ComponentInstance)bo;
			if(ci.getComponentClassifier() instanceof ComponentImplementation) {
				return (ComponentImplementation)ci.getComponentClassifier();
			}
		}

		return null;
	}
}
