package edu.uah.rsesc.agree.ge.ui.palette;

import java.util.Optional;

import org.osate.aadl2.NamedElement;
import org.osate.ge.BusinessObjectContext;
import org.osate.ge.aadl2.ui.AadlOperationBuilder;
import org.osate.ge.operations.Operation;
import org.osate.ge.operations.StepResultBuilder;
import org.osate.ge.palette.BasePaletteCommand;
import org.osate.ge.palette.GetTargetedOperationContext;
import org.osate.ge.palette.TargetedPaletteCommand;

import com.rockwellcollins.atc.agree.agree.AgreeContract;
import com.rockwellcollins.atc.agree.agree.AgreeFactory;
import com.rockwellcollins.atc.agree.agree.Arg;
import com.rockwellcollins.atc.agree.agree.LinearizationDef;
import com.rockwellcollins.atc.agree.agree.LinearizationInterval;
import com.rockwellcollins.atc.agree.agree.NamedElmExpr;
import com.rockwellcollins.atc.agree.agree.PrimType;
import com.rockwellcollins.atc.agree.agree.RealLitExpr;
import com.rockwellcollins.atc.agree.agree.UnaryExpr;

import edu.uah.rsesc.agree.ge.AgreeNamingUtil;
import edu.uah.rsesc.agree.ge.businessObjectHandlers.AgreeHandlerUtil;

public class CreateLinearizationPaletteCommand extends BasePaletteCommand implements TargetedPaletteCommand {
	public CreateLinearizationPaletteCommand() {
		super("Linearization", AgreePaletteCategories.AGREE, null);
	}

	@Override
	public Optional<Operation> getOperation(final GetTargetedOperationContext ctx) {
		return getOperation(ctx.getTarget());
	}

	public Optional<Operation> getOperation(final BusinessObjectContext targetBoc) {
		if (!getClassifierOpBuilder().canBuildOperation(targetBoc.getBusinessObject())) {
			return Optional.empty();
		}

		return Optional.of(Operation.createWithBuilder(createOp -> {
			getClassifierOpBuilder().buildOperation(createOp, targetBoc.getBusinessObject())
					.map(AgreeHandlerUtil.toBusinessObjectToModify()).modifyPreviousResult(modifyBo -> {
						final AgreeContract agreeContract = AgreeHandlerUtil.getOrCreateAgreeContract(modifyBo);
						final LinearizationDef newBo = AgreeFactory.eINSTANCE.createLinearizationDef();
						newBo.setName(AgreeNamingUtil.buildUniqueIdentifier(agreeContract, "linearization"));

						// Parameter
						final Arg newArg = AgreeFactory.eINSTANCE.createArg();
						newArg.setName("x");
						final PrimType type = AgreeFactory.eINSTANCE.createPrimType();
						type.setName("real");
						newArg.setType(type);
						newBo.getArgs().add(newArg);

						// Interval
						final LinearizationInterval interval = AgreeFactory.eINSTANCE.createLinearizationInterval();
						final RealLitExpr startExprReal = AgreeFactory.eINSTANCE.createRealLitExpr();
						startExprReal.setVal("1.0");

						final UnaryExpr startExpr = AgreeFactory.eINSTANCE.createUnaryExpr();
						startExpr.setOp("-");
						startExpr.setExpr(startExprReal);
						interval.setStart(startExpr);

						final RealLitExpr endExpr = AgreeFactory.eINSTANCE.createRealLitExpr();
						endExpr.setVal("1.0");
						interval.setEnd(endExpr);
						newBo.getIntervals().add(interval);

						// Precision
						final RealLitExpr precisionExpr = AgreeFactory.eINSTANCE.createRealLitExpr();
						precisionExpr.setVal("0.01");
						newBo.setPrecision(precisionExpr);

						// Body
						final NamedElmExpr body = AgreeFactory.eINSTANCE.createNamedElmExpr();
						body.setElm(newArg);

						newBo.setExprBody(body);

						agreeContract.getSpecs().add(newBo);

						return StepResultBuilder.create(newBo).showNewBusinessObject(targetBoc, newBo).build();
					});
		}));
	}

	private static AadlOperationBuilder<NamedElement> getClassifierOpBuilder() {
		return AadlOperationBuilder.packagesAndComponentClassifiers().allowAnyMatchingClassifier();
	}
}
