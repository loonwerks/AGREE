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
import com.rockwellcollins.atc.agree.agree.NodeBodyExpr;
import com.rockwellcollins.atc.agree.agree.NodeDef;
import com.rockwellcollins.atc.agree.agree.NodeEq;
import com.rockwellcollins.atc.agree.agree.PrimType;
import com.rockwellcollins.atc.agree.agree.RealLitExpr;

import edu.uah.rsesc.agree.ge.AgreeNamingUtil;
import edu.uah.rsesc.agree.ge.businessObjectHandlers.AgreeHandlerUtil;

public class CreateNodePaletteCommand extends BasePaletteCommand implements TargetedPaletteCommand {
	public CreateNodePaletteCommand() {
		super("Node", AgreePaletteCategories.AGREE, null);
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

						final NodeDef newBo = AgreeFactory.eINSTANCE.createNodeDef();
						agreeContract.getSpecs().add(newBo);
						newBo.setName(AgreeNamingUtil.buildUniqueIdentifier(agreeContract, "node"));

						// Create return argument
						final Arg retArg = AgreeFactory.eINSTANCE.createArg();
						retArg.setName("result");
						newBo.getRets().add(retArg);

						// Set Type
						final PrimType realType = AgreeFactory.eINSTANCE.createPrimType();
						realType.setName("real");
						retArg.setType(realType);

						// Create the node body
						final NodeBodyExpr nodeBody = AgreeFactory.eINSTANCE.createNodeBodyExpr();
						newBo.setNodeBody(nodeBody);

						// Create a statement that assigns to the return value.
						NodeEq initialStatement = AgreeFactory.eINSTANCE.createNodeEq();
						nodeBody.getStmts().add(initialStatement);
						initialStatement.getLhs().add(retArg);

						final RealLitExpr zeroExpr = AgreeFactory.eINSTANCE.createRealLitExpr();
						zeroExpr.setVal("0.0");
						initialStatement.setExpr(zeroExpr);

						return StepResultBuilder.create(newBo).showNewBusinessObject(targetBoc, newBo).build();
					});
		}));
	}

	private static AadlOperationBuilder<NamedElement> getClassifierOpBuilder() {
		return AadlOperationBuilder.packagesAndComponentClassifiers().allowAnyMatchingClassifier();
	}
}
