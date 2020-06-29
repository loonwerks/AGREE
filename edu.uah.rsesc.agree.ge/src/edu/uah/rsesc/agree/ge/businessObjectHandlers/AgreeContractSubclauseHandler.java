package edu.uah.rsesc.agree.ge.businessObjectHandlers;

import java.util.Optional;

import org.osate.ge.CanonicalBusinessObjectReference;
import org.osate.ge.GraphicalConfiguration;
import org.osate.ge.RelativeBusinessObjectReference;
import org.osate.ge.businessobjecthandling.BusinessObjectHandler;
import org.osate.ge.businessobjecthandling.GetGraphicalConfigurationContext;
import org.osate.ge.businessobjecthandling.GetNameContext;
import org.osate.ge.businessobjecthandling.IsApplicableContext;
import org.osate.ge.businessobjecthandling.ReferenceContext;

import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause;

/**
 * Business object handler for {@link AgreeContractSubclause}.
 * This handler only exists to prevent the annex from being added by the AADL business object provider.
 * Since the object isn't contributed at this time, the other methods are not necessary.
 *
 */
public class AgreeContractSubclauseHandler implements BusinessObjectHandler {
	@Override
	public boolean isApplicable(final IsApplicableContext ctx) {
		return ctx.getBusinessObject(AgreeContractSubclause.class).isPresent();
	}

	@Override
	public CanonicalBusinessObjectReference getCanonicalReference(final ReferenceContext ctx) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public RelativeBusinessObjectReference getRelativeReference(final ReferenceContext ctx) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public Optional<GraphicalConfiguration> getGraphicalConfiguration(final GetGraphicalConfigurationContext ctx) {
		return Optional.empty();
	}

	@Override
	public String getName(GetNameContext ctx) {
		return "";
	}
}
