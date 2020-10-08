package edu.uah.rsesc.agree.ge.businessObjectHandlers;

import org.osate.ge.CanonicalBusinessObjectReference;
import org.osate.ge.RelativeBusinessObjectReference;
import org.osate.ge.businessobjecthandling.BusinessObjectHandler;
import org.osate.ge.businessobjecthandling.ReferenceContext;

public abstract class AgreeBusinessObjectHandler implements BusinessObjectHandler {
	@Override
	public CanonicalBusinessObjectReference getCanonicalReference(ReferenceContext ctx) {
		return AgreeReferenceUtil.getCanonicalReference(ctx.getBusinessObject(), ctx.getReferenceBuilder());
	}

	@Override
	public RelativeBusinessObjectReference getRelativeReference(ReferenceContext ctx) {
		return AgreeReferenceUtil.getRelativeReference(ctx.getBusinessObject());
	}

}
