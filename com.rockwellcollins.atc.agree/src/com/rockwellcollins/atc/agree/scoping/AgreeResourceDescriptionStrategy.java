package com.rockwellcollins.atc.agree.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.util.IAcceptor;

import com.google.inject.Singleton;
import com.rockwellcollins.atc.agree.agree.ExistsExpr;
import com.rockwellcollins.atc.agree.agree.FlatmapExpr;
import com.rockwellcollins.atc.agree.agree.FoldLeftExpr;
import com.rockwellcollins.atc.agree.agree.FoldRightExpr;
import com.rockwellcollins.atc.agree.agree.ForallExpr;

/**
 * @since 4.0
 */
@Singleton
public class AgreeResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy {

	public AgreeResourceDescriptionStrategy() {
		super();
	}

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		EObject container = eObject.eContainer();
		if (container instanceof ForallExpr && ((ForallExpr) container).getBinding().equals(eObject)) {
			return false;
		} else if (container instanceof ExistsExpr && ((ExistsExpr) container).getBinding().equals(eObject)) {
			return false;
		} else if (container instanceof FlatmapExpr && ((FlatmapExpr) container).getBinding().equals(eObject)) {
			return false;
		} else if (container instanceof FoldLeftExpr && (((FoldLeftExpr) container).getAccumulator().equals(eObject)
				|| ((FoldLeftExpr) container).getBinding().equals(eObject))) {
			return false;
		} else if (container instanceof FoldRightExpr && (((FoldRightExpr) container).getAccumulator().equals(eObject)
				|| ((FoldLeftExpr) container).getBinding().equals(eObject))) {
			return false;
		}
		return super.createEObjectDescriptions(eObject, acceptor);
	}

}
