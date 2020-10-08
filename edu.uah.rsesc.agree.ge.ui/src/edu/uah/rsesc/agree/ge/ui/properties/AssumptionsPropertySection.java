package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.AssumeStatement;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.AssumeStatementHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateAssumptionPaletteCommand;

public class AssumptionsPropertySection extends GenericPropertySection {
	private final AssumeStatementHandler handler = new AssumeStatementHandler();
	private final CreateAssumptionPaletteCommand createCommand = new CreateAssumptionPaletteCommand();

	public static class Filter implements IFilter {
		@Override
		public boolean select(final Object toTest) {
			return AgreePropertySectionUtil.asComponentType(toTest) != null;
		}
	}

	@Override
	protected void onAdd() {
		getSelectedBos().bocStream().findFirst()
				.ifPresent(target -> createCommand.getOperation(target).ifPresent(PropertySectionUtil::execute));
	}

	@Override
	protected String getName(final Object element) {
		return handler.getName((AssumeStatement) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asComponentType, AssumeStatement.class);
	}
}
