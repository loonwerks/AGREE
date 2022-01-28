package com.rockwellcollins.atc.agree.scoping;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.util.IAcceptor;
import org.osate.xtext.aadl2.scoping.Aadl2ResourceDescriptionStrategy;

import com.google.inject.Singleton;
import com.rockwellcollins.atc.agree.agree.ExistsExpr;

/**
 * @since 3.0
 */
@Singleton
public class AgreeResourceDescriptionStrategy extends Aadl2ResourceDescriptionStrategy {

	public AgreeResourceDescriptionStrategy() {
		super();
	}

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		EObject container = eObject.eContainer();
		if (container instanceof ExistsExpr && ((ExistsExpr) container).getBinding().equals(eObject)) {
			return false;
		}
		return defaultCreateEObjectDescriptions(eObject, acceptor);
	}

	@Override
	public boolean createReferenceDescriptions(EObject eObject, URI exportedContainerURI,
			IAcceptor<IReferenceDescription> acceptor) {
		return defaultCreateReferenceDescriptions(eObject, exportedContainerURI, acceptor);
	}

}
