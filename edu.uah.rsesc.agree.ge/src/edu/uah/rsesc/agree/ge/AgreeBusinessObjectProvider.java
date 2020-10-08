package edu.uah.rsesc.agree.ge;

import java.util.stream.Stream;

import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.Element;
import org.osate.aadl2.Subcomponent;
import org.osate.annexsupport.AnnexUtil;
import org.osate.ge.BusinessObjectProvider;
import org.osate.ge.BusinessObjectProviderContext;

import com.rockwellcollins.atc.agree.agree.AgreeContract;
import com.rockwellcollins.atc.agree.agree.AgreeContractLibrary;
import com.rockwellcollins.atc.agree.agree.AgreeContractSubclause;
import com.rockwellcollins.atc.agree.agree.AgreePackage;
import com.rockwellcollins.atc.agree.agree.AssertStatement;
import com.rockwellcollins.atc.agree.agree.AssumeStatement;
import com.rockwellcollins.atc.agree.agree.ConstStatement;
import com.rockwellcollins.atc.agree.agree.EnumStatement;
import com.rockwellcollins.atc.agree.agree.EqStatement;
import com.rockwellcollins.atc.agree.agree.GuaranteeStatement;
import com.rockwellcollins.atc.agree.agree.LemmaStatement;
import com.rockwellcollins.atc.agree.agree.LinearizationDef;
import com.rockwellcollins.atc.agree.agree.NodeDef;
import com.rockwellcollins.atc.agree.agree.PropertyStatement;
import com.rockwellcollins.atc.agree.agree.RecordDef;
import com.rockwellcollins.atc.agree.agree.SpecStatement;

public class AgreeBusinessObjectProvider implements BusinessObjectProvider {
	@Override
	public Stream<?> getChildBusinessObjects(final BusinessObjectProviderContext ctx) {
		final Object bo = ctx.getBusinessObjectContext().getBusinessObject();

		if (bo instanceof AadlPackage) {
			return getAllSpecStatements((AadlPackage) bo).filter(AgreeBusinessObjectProvider::isSupported);
		} else if (bo instanceof Classifier) {
			return getAllSpecStatements((Classifier) bo).filter(AgreeBusinessObjectProvider::isSupported);
		} else if (bo instanceof Subcomponent) {
			final ComponentClassifier cc = ((Subcomponent) bo).getAllClassifier();
			if (cc != null) {
				return getAllSpecStatements(cc).filter(AgreeBusinessObjectProvider::isSupported);
			}
		}

		return Stream.empty();
	}

	private static boolean isSupported(final SpecStatement bo) {
		return bo instanceof AssertStatement || bo instanceof AssumeStatement || bo instanceof ConstStatement
				|| bo instanceof EnumStatement || bo instanceof EqStatement || bo instanceof GuaranteeStatement
				|| bo instanceof LemmaStatement || bo instanceof LinearizationDef || bo instanceof NodeDef
				|| bo instanceof PropertyStatement || bo instanceof RecordDef;
	}

	public static Stream<SpecStatement> getAllSpecStatements(final Element e) {
		if (e instanceof AadlPackage) {
			return getAllSpecStatements((AadlPackage) e);
		} else if (e instanceof Classifier) {
			return getAllSpecStatements((Classifier) e);
		}

		return Stream.empty();
	}

	public static Stream<SpecStatement> getAllSpecStatements(final AadlPackage pkg) {
		return AnnexUtil.getAllActualAnnexLibraries(pkg, AgreePackage.eINSTANCE.getAgreeContractLibrary()).stream()
				.map(l -> (AgreeContractLibrary) l).filter(agreeLib -> agreeLib.getContract() instanceof AgreeContract)
				.flatMap(agreeLib -> ((AgreeContract) agreeLib.getContract()).getSpecs().stream());
	}

	public static Stream<SpecStatement> getAllSpecStatements(final Classifier classifier) {
		// Work around for exception when an implementation does not have a type.
		if (classifier instanceof ComponentImplementation && ((ComponentImplementation) classifier).getType() == null) {
			return Stream.empty();
		}

		Stream<SpecStatement> results = getSpecStatements(classifier);
		if (classifier instanceof ComponentImplementation) {
			final ComponentType ct = ((ComponentImplementation) classifier).getType();
			results = Stream.concat(getSpecStatements(ct), results);
		}

		return results;
	}

	/**
	 * Returns the spec statements for the specified classifier. Does not return spec statements for inherited classifiers.
	 * @param classifier
	 * @return
	 */
	private static Stream<SpecStatement> getSpecStatements(final Classifier classifier) {
		if (classifier == null) {
			return Stream.empty();
		}

		return classifier.getOwnedAnnexSubclauses().stream().map(sc -> AnnexUtil.getParsedAnnex(sc))
				.filter(sc -> sc instanceof AgreeContractSubclause).map(AgreeContractSubclause.class::cast)
				.map(sc -> sc.getContract()).filter(c -> c instanceof AgreeContract).map(AgreeContract.class::cast)
				.flatMap(c -> c.getSpecs().stream());
	}
}
