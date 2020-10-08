package edu.uah.rsesc.agree.ge.ui.palette;

import java.util.Optional;

import org.osate.aadl2.Aadl2Factory;
import org.osate.aadl2.BooleanLiteral;
import org.osate.aadl2.ComponentImplementation;
import org.osate.ge.BusinessObjectContext;
import org.osate.ge.aadl2.ui.AadlOperationBuilder;
import org.osate.ge.operations.Operation;
import org.osate.ge.operations.StepResultBuilder;
import org.osate.ge.palette.BasePaletteCommand;
import org.osate.ge.palette.GetTargetedOperationContext;
import org.osate.ge.palette.TargetedPaletteCommand;

import com.rockwellcollins.atc.agree.agree.AgreeContract;
import com.rockwellcollins.atc.agree.agree.AgreeFactory;
import com.rockwellcollins.atc.agree.agree.AssertStatement;
import com.rockwellcollins.atc.agree.agree.BoolLitExpr;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.AgreeHandlerUtil;

public class CreateAssertionPaletteCommand extends BasePaletteCommand implements TargetedPaletteCommand {
	public CreateAssertionPaletteCommand() {
		super("Assertion", AgreePaletteCategories.AGREE, null);
	}

	@Override
	public Optional<Operation> getOperation(final GetTargetedOperationContext ctx) {
		return getOperation(ctx.getTarget());
	}

	public Optional<Operation> getOperation(final BusinessObjectContext target) {
		if (!getClassifierOpBuilder().canBuildOperation(target.getBusinessObject())) {
			return Optional.empty();
		}

		return Optional.of(Operation.createWithBuilder(createOp -> {
			getClassifierOpBuilder().buildOperation(createOp, target
					.getBusinessObject())
					.map(AgreeHandlerUtil.toBusinessObjectToModify()).modifyPreviousResult(modifyBo -> {
						final AgreeContract agreeContract = AgreeHandlerUtil.getOrCreateAgreeContract(modifyBo);
						final AssertStatement newBo = AgreeFactory.eINSTANCE.createAssertStatement();
						newBo.setStr("New Assertion");
						final BoolLitExpr trueExpr = AgreeFactory.eINSTANCE.createBoolLitExpr();
						final BooleanLiteral trueLit = Aadl2Factory.eINSTANCE.createBooleanLiteral();
						trueLit.setValue(true);
						trueExpr.setVal(trueLit);
						newBo.setExpr(trueExpr);
						agreeContract.getSpecs().add(newBo);

								return StepResultBuilder.create(newBo).showNewBusinessObject(target, newBo)
										.build();
							});
		}));
	}

	private static AadlOperationBuilder<ComponentImplementation> getClassifierOpBuilder() {
		return AadlOperationBuilder.componentImplementations().allowAnyMatchingClassifier();
	}

}
