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
import com.rockwellcollins.atc.agree.agree.PrimType;
import com.rockwellcollins.atc.agree.agree.RecordDef;

import edu.uah.rsesc.agree.ge.AgreeNamingUtil;
import edu.uah.rsesc.agree.ge.businessObjectHandlers.AgreeHandlerUtil;

public class CreateRecordPaletteCommand extends BasePaletteCommand implements TargetedPaletteCommand {
	public CreateRecordPaletteCommand() {
		super("Record", AgreePaletteCategories.AGREE, null);
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

						final RecordDef newBo = AgreeFactory.eINSTANCE.createRecordDef();
						agreeContract.getSpecs().add(newBo);
						newBo.setName(AgreeNamingUtil.buildUniqueIdentifier(agreeContract, "record"));

						// Records must have at least one field
						final Arg field = AgreeFactory.eINSTANCE.createArg();
						field.setName("field");
						newBo.getArgs().add(field);

						// Create return type
						final PrimType boolType = AgreeFactory.eINSTANCE.createPrimType();
						boolType.setName("bool");
						field.setType(boolType);

						return StepResultBuilder.create(newBo).showNewBusinessObject(targetBoc, newBo).build();
					});
		}));
	}

	private static AadlOperationBuilder<NamedElement> getClassifierOpBuilder() {
		return AadlOperationBuilder.packagesAndComponentClassifiers().allowAnyMatchingClassifier();
	}
}
