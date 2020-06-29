package edu.uah.rsesc.agree.ge.ui.palette;

import java.util.Optional;

import org.osate.aadl2.Aadl2Factory;
import org.osate.aadl2.BooleanLiteral;
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
import com.rockwellcollins.atc.agree.agree.BoolLitExpr;
import com.rockwellcollins.atc.agree.agree.ConstStatement;
import com.rockwellcollins.atc.agree.agree.PrimType;

import edu.uah.rsesc.agree.ge.AgreeNamingUtil;
import edu.uah.rsesc.agree.ge.businessObjectHandlers.AgreeHandlerUtil;

public class CreateConstantPaletteCommand extends BasePaletteCommand implements TargetedPaletteCommand {
	public CreateConstantPaletteCommand() {
		super("Constant", AgreePaletteCategories.AGREE, null);
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
			getClassifierOpBuilder().buildOperation(createOp,
					targetBoc
					.getBusinessObject())
					.map(AgreeHandlerUtil.toBusinessObjectToModify()).modifyPreviousResult(modifyBo -> {
						final AgreeContract agreeContract = AgreeHandlerUtil.getOrCreateAgreeContract(modifyBo);
						final PrimType type = AgreeFactory.eINSTANCE.createPrimType();
						type.setName("bool");

						final BoolLitExpr trueExpr = AgreeFactory.eINSTANCE.createBoolLitExpr();
						final BooleanLiteral trueLit = Aadl2Factory.eINSTANCE.createBooleanLiteral();
						trueLit.setValue(true);
						trueExpr.setVal(trueLit);

						final ConstStatement newBo = AgreeFactory.eINSTANCE.createConstStatement();
						newBo.setName(AgreeNamingUtil.buildUniqueIdentifier(agreeContract, "constant"));
						newBo.setType(type);
						newBo.setExpr(trueExpr);

						agreeContract.getSpecs().add(newBo);

						return StepResultBuilder.create(newBo).showNewBusinessObject(targetBoc, newBo).build();
					});
		}));
	}

	private static AadlOperationBuilder<NamedElement> getClassifierOpBuilder() {
		return AadlOperationBuilder.packagesAndComponentClassifiers().allowAnyMatchingClassifier();
	}

}
