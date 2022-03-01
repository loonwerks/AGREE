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
package com.rockwellcollins.atc.agree.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.osate.aadl2.Aadl2Factory;
import org.osate.aadl2.Aadl2Package;
import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.BusType;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.DeviceType;
import org.osate.aadl2.PackageRename;
import org.osate.aadl2.ProcessType;
import org.osate.aadl2.ProcessorType;
import org.osate.aadl2.PublicPackageSection;
import org.osate.aadl2.SubprogramType;
import org.osate.aadl2.SystemType;
import org.osate.aadl2.ThreadGroupType;
import org.osate.aadl2.ThreadType;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.instantiation.InstantiateModel;
import org.osate.aadl2.modelsupport.AadlConstants;
import org.osate.aadl2.modelsupport.errorreporting.AnalysisErrorReporterManager;
import org.osate.aadl2.modelsupport.errorreporting.MarkerAnalysisErrorReporter;

public class EphemeralImplementationUtil {

	/**
	 * The {@link IProgressMonitor} to inform regarding steps completed during the execution of the commands for
	 * creation and deletion of the ephemeral implementation packages.
	 */
	protected final IProgressMonitor monitor;

	/**
	 * List of {@link Resource} (AADL files) created to contain the packages containing the ephemeral implementations.
	 * <p>
	 * Resources are accumulated as ephemeral implementations are created and deleted at cleanup.
	 */
	private List<Resource> ephemeralResources = new ArrayList<>();

	/**
	 * Create an instance of the utility.
	 *
	 * @param monitor The {@link IProgressMonitor} used for informing the user interface of progress on the commands.
	 * The monitor is assumed to have lifecycle encompassing that of this utility instance.
	 */
	public EphemeralImplementationUtil(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * An error message that is filled by potential methods that instantiate the system and raises an error. This
	 * message is then show in the error dialog when an instantiation error is raised.
	 */
	private static String errorMessage = null;

	/**
	 * To keep under control the error messages and ease debug, we encapsulate the error message string and access it
	 * only through methods (setters and getters).
	 */
	public static void setErrorMessage(String s) {
		errorMessage = s;
	}

	/**
	 * Fetch the current error message.
	 *
	 * @return The current error message string.
	 */
	public static String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Generate a unique {@link URI} for a resource to contain the ephemeral implementation.
	 *
	 * @param ct The {@link ComponentType} whose name to use as a basis for naming the resource to contain an
	 *     ephemeral implementation of component type ct.
	 * @return A URI guaranteed to be unique in the workspace.
	 */
	private static URI getEphemeralImplURI(ComponentType ct) {
		URI ctURI = ct.eResource().getURI();
		String extension = ctURI.fileExtension();
		URI ctBaseURI = ctURI.trimFileExtension();
		URI implURI;
		do {
			implURI = ctBaseURI.trimSegments(1)
					.appendSegment(ctBaseURI.lastSegment() + "_" + UUID.randomUUID().toString().replace("-", "_"))
					.appendFileExtension(extension);
		} while (URIConverter.INSTANCE.exists(implURI, Collections.EMPTY_MAP));
		return implURI;
	}

	/**
	 * Generate an ephemeral {@link ComponentImplementation} matching the subtype of the given {@link ComponentType}.
	 * <p>
	 * Ephemerally generated component implementations are placed it in an ephemeral {@link Resource}.  The ephemeral
	 * resources are intended to have short lifecycles and deleted by the {@link cleanup} method.
	 *
	 * @param ct The component type for which to create an ephemeral implementation.
	 * @return A component implementation for the given component type.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ComponentImplementation generateEphemeralCompImplFromType(ComponentType ct)
			throws Exception {
//		Resource aadlResource = OsateResourceUtil.getResource(getEphemeralImplURI(ct));
		Resource aadlResource = getResource(getEphemeralImplURI(ct));

		ephemeralResources.add(aadlResource);
		List<ComponentImplementation> resultList;
		ComponentImplementation result;

		final TransactionalEditingDomain domain = WorkspaceEditingDomainFactory.INSTANCE.createEditingDomain();
		// We execute this command on the command stack because otherwise, we will not
		// have write permissions on the editing domain.
		Command cmd = new RecordingCommand(domain) {
			public ComponentImplementation implementation;

			@Override
			protected void doExecute() {
				try {
					implementation = createComponentImplementationInternal(ct, aadlResource);
				} catch (InterruptedException e) {
					// Do nothing. Will be thrown after execute.
				}
			}

			@Override
			public List<ComponentImplementation> getResult() {
				return Collections.singletonList(implementation);
			}
		};

		((TransactionalCommandStack) domain.getCommandStack()).execute(cmd, null);
		if (monitor.isCanceled()) {
			throw new InterruptedException();
		}

		try {
			// We're done: Save the model.
			// We don't respond to a cancel at this point
			monitor.subTask("Saving implementation model");
			aadlResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			return null;
		}
		resultList = (List<ComponentImplementation>) cmd.getResult();
		result = resultList.get(0);

		return result;
	}

	Resource getResource(URI uri) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = null;
		try {
			resource = resourceSet.getResource(uri, true);
		} catch (RuntimeException e) {
			resource = resourceSet.getResource(uri, false);
			if (resource == null) {
				resource = resourceSet.createResource(uri);
			}
		}
		return resource;
	}

	/**
	 * Internal method to actually create the ephemeral component implementation and containing resource.
	 * <p>
	 * This method is intended to by invoked only from the command stack so that editing permissions are managed
	 * through the transactional editing domain.
	 *
	 * @param ct The {@link ComponentType} for which to create an ephemeral implementation.
	 * @param aadlResource The {@link Resource} in which to place the ephemeral implementation and it containing
	 *     {@link AadlPackage}.
	 * @return A {@link ComponentImplementation} matching the given component type.
	 * @throws InterruptedException
	 */
	private ComponentImplementation createComponentImplementationInternal(ComponentType ct, Resource aadlResource)
			throws InterruptedException {
		// Create a package and public section to contain the created
		// component implementation
		AadlPackage aadlPackage = Aadl2Factory.eINSTANCE.createAadlPackage();
		aadlPackage.setName(aadlResource.getURI().trimFragment().trimFileExtension().lastSegment().toString());
		PublicPackageSection publicSection = aadlPackage.createOwnedPublicSection();

		// Add import for package containing ct
		AadlPackage ctPackage = (AadlPackage) AgreeUtils.getClosestContainerOfType(ct, AadlPackage.class);
		publicSection.getImportedUnits().add(ctPackage);

		// Add renames clause to make linking to ct easy
		PackageRename ctRename = publicSection.createOwnedPackageRename();
		ctRename.setName("");
		ctRename.setRenamedPackage(ctPackage);
		ctRename.setRenameAll(true);

		// Create the component implementation in the public section
		ComponentImplementation compImpl;
		if (ct instanceof ThreadType) {
			compImpl = (ComponentImplementation) publicSection
					.createOwnedClassifier(Aadl2Package.eINSTANCE.getThreadImplementation());
		} else if (ct instanceof ThreadGroupType) {
			compImpl = (ComponentImplementation) publicSection
					.createOwnedClassifier(Aadl2Package.eINSTANCE.getThreadGroupImplementation());
		} else if (ct instanceof ProcessType) {
			compImpl = (ComponentImplementation) publicSection
					.createOwnedClassifier(Aadl2Package.eINSTANCE.getProcessImplementation());
		} else if (ct instanceof SubprogramType) {
			compImpl = (ComponentImplementation) publicSection
					.createOwnedClassifier(Aadl2Package.eINSTANCE.getSubprogramImplementation());
		} else if (ct instanceof ProcessorType) {
			compImpl = (ComponentImplementation) publicSection
					.createOwnedClassifier(Aadl2Package.eINSTANCE.getProcessorImplementation());
		} else if (ct instanceof BusType) {
			compImpl = (ComponentImplementation) publicSection
					.createOwnedClassifier(Aadl2Package.eINSTANCE.getBusImplementation());
		} else if (ct instanceof DeviceType) {
			compImpl = (ComponentImplementation) publicSection
					.createOwnedClassifier(Aadl2Package.eINSTANCE.getDeviceImplementation());
		} else if (ct instanceof SystemType) {
			compImpl = (ComponentImplementation) publicSection
					.createOwnedClassifier(Aadl2Package.eINSTANCE.getSystemImplementation());
		} else {
			throw new AgreeException("Unhandled component type: " + ct.getClass().toString());
		}
		compImpl.setType(ct);
		compImpl.setName(ct.getName() + ".wrapper");

		// Add the package and its contents to the resource
		aadlResource.getContents().add(aadlPackage);

		// Needed to save the root object because we may attach warnings to the
		// IResource as we build it.
		try {
			aadlResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			return null;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			setErrorMessage(npe.getMessage());

			npe.getMessage();
			return null;
//		} catch (InterruptedException e) {
//			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();

			e.getMessage();
			return null;
		}
		return compImpl;
	}

	/**
	 * Generate an ephemeral {@link SystemInstance} matching the subtype of the given {@link ComponentType}.
	 * <p>
	 * Ephemerally generated system instances are placed it in an ephemeral {@link Resource}.  The ephemeral
	 * resources are intended to have short lifecycles and deleted by the {@link cleanup} method.
	 *
	 * @param ct The component type for which to create an ephemeral implementation.
	 * @return A system instance for the given component type.
	 * @throws Exception
	 * @since 2.8
	 */
	@SuppressWarnings("unchecked")
	public SystemInstance generateEphemeralCompInstanceFromType(ComponentType ct) throws Exception {
		Resource implementationAadlResource = getResource(getEphemeralImplURI(ct));

		ephemeralResources.add(implementationAadlResource);
		List<ComponentImplementation> implementationResultList;
		ComponentImplementation implementationResult;

		final TransactionalEditingDomain domain = WorkspaceEditingDomainFactory.INSTANCE.createEditingDomain();

		Command implementationCmd = new RecordingCommand(domain) {
			public ComponentImplementation implementation;

			@Override
			protected void doExecute() {
				try {
					implementation = createComponentImplementationInternal(ct, implementationAadlResource);
				} catch (InterruptedException e) {
					// Do nothing. Will be thrown after execute.
				}
			}

			@Override
			public List<ComponentImplementation> getResult() {
				return Collections.singletonList(implementation);
			}
		};

		((TransactionalCommandStack) domain.getCommandStack()).execute(implementationCmd, null);
		if (monitor.isCanceled()) {
			throw new InterruptedException();
		}

		try {
			// We're done: Save the model.
			// We don't respond to a cancel at this point
			monitor.subTask("Saving implementation model");
			implementationAadlResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			return null;
		}
		implementationResultList = (List<ComponentImplementation>) implementationCmd.getResult();
		implementationResult = implementationResultList.get(0);

		Resource instanceAadlResource = getResource(InstantiateModel.getInstanceModelURI(implementationResult));
		ephemeralResources.add(instanceAadlResource);

		List<SystemInstance> instanceResultList;
		SystemInstance instanceResult;

		// We execute this command on the command stack because otherwise, we will not
		// have write permissions on the editing domain.
		Command instanceCmd = new RecordingCommand(domain) {
			public SystemInstance systemInstance;

			@Override
			protected void doExecute() {
				try {
					final InstantiateModel instantiateModel = new InstantiateModel(monitor,
							new AnalysisErrorReporterManager(new MarkerAnalysisErrorReporter.Factory(
									AadlConstants.INSTANTIATION_OBJECT_MARKER)));
					systemInstance = instantiateModel.createSystemInstance(implementationResult, instanceAadlResource);
				} catch (InterruptedException e) {
					// Do nothing. Will be thrown after execute.
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = e.getMessage();

					e.getMessage();
				}
			}

			@Override
			public List<SystemInstance> getResult() {
				return Collections.singletonList(systemInstance);
			}
		};

		((TransactionalCommandStack) domain.getCommandStack()).execute(instanceCmd, null);
		if (monitor.isCanceled()) {
			throw new InterruptedException();
		}

		try {
			// We're done: Save the model.
			monitor.subTask("Saving instance model");
			instanceAadlResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			return null;
		}
		instanceResultList = (List<SystemInstance>) instanceCmd.getResult();
		instanceResult = instanceResultList.get(0);

		return instanceResult;
	}

	/**
	 * Generate an ephemeral {@link SystemInstance} matching the subtype of the given {@link ComponentType}.
	 * <p>
	 * Ephemerally generated system instances are placed it in an ephemeral {@link Resource}.  The ephemeral
	 * resources are intended to have short lifecycles and deleted by the {@link cleanup} method.
	 *
	 * @param ct The component type for which to create an ephemeral implementation.
	 * @return A system instance for the given component type.
	 * @throws Exception
	 * @since 2.8
	 */
	@SuppressWarnings("unchecked")
	public SystemInstance generateEphemeralCompInstanceFromImplementation(ComponentImplementation ci) throws Exception {
		final TransactionalEditingDomain domain = WorkspaceEditingDomainFactory.INSTANCE.createEditingDomain();

		Resource instanceAadlResource = getResource(InstantiateModel.getInstanceModelURI(ci));
		ephemeralResources.add(instanceAadlResource);

		List<SystemInstance> instanceResultList;
		SystemInstance instanceResult;

		// We execute this command on the command stack because otherwise, we will not
		// have write permissions on the editing domain.
		Command instanceCmd = new RecordingCommand(domain) {
			public SystemInstance systemInstance;

			@Override
			protected void doExecute() {
				try {
					final InstantiateModel instantiateModel = new InstantiateModel(monitor,
							new AnalysisErrorReporterManager(new MarkerAnalysisErrorReporter.Factory(
									AadlConstants.INSTANTIATION_OBJECT_MARKER)));
					systemInstance = instantiateModel.createSystemInstance(ci, instanceAadlResource);
				} catch (InterruptedException e) {
					// Do nothing. Will be thrown after execute.
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = e.getMessage();

					e.getMessage();
				}
			}

			@Override
			public List<SystemInstance> getResult() {
				return Collections.singletonList(systemInstance);
			}
		};

		((TransactionalCommandStack) domain.getCommandStack()).execute(instanceCmd, null);
		if (monitor.isCanceled()) {
			throw new InterruptedException();
		}

		try {
			// We're done: Save the model.
			monitor.subTask("Saving instance model");
			instanceAadlResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			return null;
		}
		instanceResultList = (List<SystemInstance>) instanceCmd.getResult();
		instanceResult = instanceResultList.get(0);

		return instanceResult;
	}

	/**
	 * Delete the accumulated ephemeral component implementations by deleting their containing {@link Resource}.
	 * <p>
	 * This method is intended to be called immediately prior to the instance of this utility going out of scope.
	 * However, it may be called multiple times, deleting the ephemeral implementations and resource accumulated to
	 * that point.
	 */
	public void cleanup() {
		final TransactionalEditingDomain domain = WorkspaceEditingDomainFactory.INSTANCE.createEditingDomain();
		// We execute this command on the command stack because otherwise, we will not
		// have write permissions on the editing domain.
		Command cmd = new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				try {
					Iterator<Resource> iter = ephemeralResources.iterator();
					while (iter.hasNext()) {
						Resource res = iter.next();
						res.delete(Collections.EMPTY_MAP);
						iter.remove();
					}
				} catch (IOException e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
			}
		};

		try {
			((TransactionalCommandStack) domain.getCommandStack()).execute(cmd, null);
		} catch (InterruptedException | RollbackException e) {
			setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}

}
