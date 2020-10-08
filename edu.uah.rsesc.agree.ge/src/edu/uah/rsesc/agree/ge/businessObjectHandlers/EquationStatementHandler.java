package edu.uah.rsesc.agree.ge.businessObjectHandlers;

import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.xtext.util.Strings;
import org.osate.ge.GraphicalConfiguration;
import org.osate.ge.GraphicalConfigurationBuilder;
import org.osate.ge.aadl2.GraphicalExtensionUtil;
import org.osate.ge.businessobjecthandling.CanDeleteContext;
import org.osate.ge.businessobjecthandling.CanRenameContext;
import org.osate.ge.businessobjecthandling.CustomRenamer;
import org.osate.ge.businessobjecthandling.GetGraphicalConfigurationContext;
import org.osate.ge.businessobjecthandling.GetNameContext;
import org.osate.ge.businessobjecthandling.IsApplicableContext;
import org.osate.ge.businessobjecthandling.RenameContext;
import org.osate.ge.graphics.Graphic;
import org.osate.ge.graphics.RectangleBuilder;
import org.osate.ge.graphics.Style;
import org.osate.ge.graphics.StyleBuilder;

import com.rockwellcollins.atc.agree.agree.AgreeFactory;
import com.rockwellcollins.atc.agree.agree.Arg;
import com.rockwellcollins.atc.agree.agree.EqStatement;
import com.rockwellcollins.atc.agree.agree.PrimType;

import edu.uah.rsesc.agree.ge.AgreeNamingUtil;

public class EquationStatementHandler extends AgreeBusinessObjectHandler implements CustomRenamer {
	private static final Graphic graphic = RectangleBuilder.create().build();

	@Override
	public boolean isApplicable(final IsApplicableContext ctx) {
		return ctx.getBusinessObject(EqStatement.class).isPresent();
	}

	@Override
	public boolean canDelete(final CanDeleteContext ctx) {
		return true;
	}

	@Override
	public Optional<GraphicalConfiguration> getGraphicalConfiguration(final GetGraphicalConfigurationContext ctx) {
		return Optional.of(GraphicalConfigurationBuilder.create().graphic(graphic).annotation(
				"<equation>")
				.style(StyleBuilder
						.create(GraphicalExtensionUtil.isInherited(ctx.getBusinessObjectContext())
								? GraphicalExtensionUtil.STYLE_INHERITED_ELEMENT
								: Style.EMPTY)
						.labelsTop().labelsHorizontalCenter().build())
				.build());
	}

	@Override
	public String getName(final GetNameContext ctx) {
		return getName(ctx.getBusinessObject(EqStatement.class).get());
	}

	public String getName(final EqStatement bo) {
		return bo.getLhs().stream().map(a -> Strings.emptyIfNull(a.getName())).collect(Collectors.joining(","));
	}

	@Override
	public boolean canRename(final CanRenameContext ctx) {
		return true;
	}

	@Override
	public Optional<String> validateName(final RenameContext ctx) {
		final EqStatement bo = ctx.getBusinessObject(EqStatement.class).get();
		final String[] names = getNames(ctx);

		for(int i = 0; i < names.length; i++) {
			final String name = names[i];
			if(i < bo.getLhs().size()) {
				final Optional<String> error = AgreeNamingUtil.validateName(bo.getLhs().get(0), name);
				if(error.isPresent()) {
					return error;
				}
			} else {
				if(!AgreeNamingUtil.isValidIdentifier(name)) {
					return Optional.of("'" + name + "' is not a valid identifier.");
				}
			}
		}

		return Optional.empty();
	}

	@Override
	public void rename(final RenameContext ctx) {
		final EqStatement bo = ctx.getBusinessObject(EqStatement.class).get();
		final String[] names = getNames(ctx);

		for(int i = 0; i < names.length; i++) {
			final String name = names[i];
			if(i < bo.getLhs().size()) {
				bo.getLhs().get(0).setName(name);
			} else {
				final Arg newArg = AgreeFactory.eINSTANCE.createArg();
				newArg.setName(name);
				final PrimType type = AgreeFactory.eINSTANCE.createPrimType();
				type.setName("bool");
				newArg.setType(type);
				bo.getLhs().add(newArg);
			}
		}

		// Remove extra names on left-hand side of equation
		while (bo.getLhs().size() > names.length) {
			bo.getLhs().remove(bo.getLhs().size() - 1);
		}
	}

	private String[] getNames(final RenameContext ctx) {
		final String[] names = ctx.getNewName().split(",");
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].trim();
		}

		return names;
	}
}
