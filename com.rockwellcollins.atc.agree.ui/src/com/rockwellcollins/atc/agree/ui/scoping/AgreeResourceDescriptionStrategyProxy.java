package com.rockwellcollins.atc.agree.ui.scoping;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.util.IAcceptor;

import com.google.inject.Injector;
import com.rockwellcollins.atc.agree.scoping.AgreeResourceDescriptionStrategy;
import com.rockwellcollins.atc.agree.ui.internal.AgreeActivator;

public class AgreeResourceDescriptionStrategyProxy implements IDefaultResourceDescriptionStrategy {

	private IDefaultResourceDescriptionStrategy agreeResourceDescriptionStrategy = null;

	private IDefaultResourceDescriptionStrategy getAgreeResourceDescriptionStrategy() {
		if (agreeResourceDescriptionStrategy == null) {
			Injector injector = AgreeActivator.getInstance()
					.getInjector(AgreeActivator.COM_ROCKWELLCOLLINS_ATC_AGREE_AGREE);
			agreeResourceDescriptionStrategy = injector.getInstance(AgreeResourceDescriptionStrategy.class);
		}
		return agreeResourceDescriptionStrategy;
	}

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		return getAgreeResourceDescriptionStrategy().createEObjectDescriptions(eObject, acceptor);
	}

	@Override
	public boolean createReferenceDescriptions(EObject eObject, URI exportedContainerURI,
			IAcceptor<IReferenceDescription> acceptor) {
		return getAgreeResourceDescriptionStrategy().createReferenceDescriptions(eObject, exportedContainerURI,
				acceptor);
	}

}
