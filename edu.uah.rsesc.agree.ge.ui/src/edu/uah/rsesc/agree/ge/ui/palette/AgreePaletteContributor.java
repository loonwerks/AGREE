package edu.uah.rsesc.agree.ge.ui.palette;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.osate.aadl2.AadlPackage;
import org.osate.aadl2.ComponentClassifier;
import org.osate.ge.palette.PaletteCategory;
import org.osate.ge.palette.PaletteCommandProviderContext;
import org.osate.ge.palette.PaletteContributor;
import org.osate.ge.palette.TargetedPaletteCommand;

public class AgreePaletteContributor implements PaletteContributor {
	@Override
	public Stream<PaletteCategory> getCategories() {
		return Stream.of(new PaletteCategory(AgreePaletteCategories.AGREE, "AGREE"));
	}

	@Override
	public Stream<TargetedPaletteCommand> getTargetedCommands(final PaletteCommandProviderContext ctx) {
		final List<TargetedPaletteCommand> commands = new ArrayList<>();

		final Object diagramBo = ctx.getDiagramBusinessObject();
		if (diagramBo == null || diagramBo instanceof AadlPackage || diagramBo instanceof ComponentClassifier) {
			commands.add(new CreateAssertionPaletteCommand());
			commands.add(new CreateAssumptionPaletteCommand());
			commands.add(new CreateConstantPaletteCommand());
			commands.add(new CreateEnumerationPaletteCommand());
			commands.add(new CreateEquationPaletteCommand());
			commands.add(new CreateGuaranteePaletteCommand());
			commands.add(new CreateLemmaPaletteCommand());
			commands.add(new CreateLinearizationPaletteCommand());
			commands.add(new CreateNodePaletteCommand());
			commands.add(new CreatePropertyPaletteCommand());
			commands.add(new CreateRecordPaletteCommand());
		}

		return commands.stream();
	}

}
