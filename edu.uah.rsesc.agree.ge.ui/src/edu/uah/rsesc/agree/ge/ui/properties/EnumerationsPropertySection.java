package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.EnumStatement;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.EnumerationStatementHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateEnumerationPaletteCommand;

public class EnumerationsPropertySection extends GenericPropertySection {
	private final EnumerationStatementHandler handler = new EnumerationStatementHandler();
	private final CreateEnumerationPaletteCommand createCommand = new CreateEnumerationPaletteCommand();

	public static class Filter implements IFilter {
		@Override
		public boolean select(final Object toTest) {
			return AgreePropertySectionUtil.asPackage(toTest) != null;
		}
	}

	@Override
	protected void onAdd() {
		getSelectedBos().bocStream().findFirst()
				.ifPresent(target -> createCommand.getOperation(target).ifPresent(PropertySectionUtil::execute));
	}

	@Override
	protected String getName(final Object element) {
		return handler.getName((EnumStatement) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asPackage, EnumStatement.class);
	}

}
