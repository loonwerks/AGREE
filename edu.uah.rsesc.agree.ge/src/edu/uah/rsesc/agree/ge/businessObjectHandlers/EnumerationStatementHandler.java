package edu.uah.rsesc.agree.ge.businessObjectHandlers;

import java.util.Optional;

import org.osate.ge.GraphicalConfiguration;
import org.osate.ge.GraphicalConfigurationBuilder;
import org.osate.ge.businessobjecthandling.CanDeleteContext;
import org.osate.ge.businessobjecthandling.CanRenameContext;
import org.osate.ge.businessobjecthandling.CustomRenamer;
import org.osate.ge.businessobjecthandling.GetGraphicalConfigurationContext;
import org.osate.ge.businessobjecthandling.GetNameContext;
import org.osate.ge.businessobjecthandling.IsApplicableContext;
import org.osate.ge.businessobjecthandling.RenameContext;
import org.osate.ge.graphics.Graphic;
import org.osate.ge.graphics.RectangleBuilder;
import org.osate.ge.graphics.StyleBuilder;

import com.rockwellcollins.atc.agree.agree.EnumStatement;

import edu.uah.rsesc.agree.ge.AgreeNamingUtil;

public class EnumerationStatementHandler extends AgreeBusinessObjectHandler implements CustomRenamer {
	private static final Graphic graphic = RectangleBuilder.create().build();

	@Override
	public boolean isApplicable(final IsApplicableContext ctx) {
		return ctx.getBusinessObject(EnumStatement.class).isPresent();
	}

	@Override
	public boolean canDelete(final CanDeleteContext ctx) {
		return true;
	}

	@Override
	public Optional<GraphicalConfiguration> getGraphicalConfiguration(final GetGraphicalConfigurationContext ctx) {
		return Optional.of(GraphicalConfigurationBuilder.create().graphic(graphic).annotation(
				"<enumeration>")
				.style(StyleBuilder
						.create()
						.labelsTop().labelsHorizontalCenter().build())
				.build());
	}

	@Override
	public String getName(final GetNameContext ctx) {
		return getName(ctx.getBusinessObject(EnumStatement.class).get());
	}

	public String getName(final EnumStatement bo) {
		return bo.getName();
	}

	@Override
	public boolean canRename(final CanRenameContext ctx) {
		return true;
	}

	@Override
	public Optional<String> validateName(final RenameContext ctx) {
		return AgreeNamingUtil.validateName(ctx.getBusinessObject(EnumStatement.class).get(), ctx.getNewName());
	}

	@Override
	public void rename(final RenameContext ctx) {
		ctx.getBusinessObject(EnumStatement.class).ifPresent(bo -> bo.setName(ctx.getNewName()));
	}
}
