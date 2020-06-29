package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.ConstStatement;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.ConstStatementHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateConstantPaletteCommand;

public class ConstantsPropertySection extends GenericPropertySection {
	private final ConstStatementHandler handler = new ConstStatementHandler();
	private final CreateConstantPaletteCommand createCommand = new CreateConstantPaletteCommand();

	public static class Filter implements IFilter {
		@Override
		public boolean select(final Object toTest) {
			return AgreePropertySectionUtil.asPackageOrComponentClassifier(toTest) != null;
		}
	}

	@Override
	protected void onAdd() {
		getSelectedBos().bocStream().findFirst()
				.ifPresent(target -> createCommand.getOperation(target).ifPresent(PropertySectionUtil::execute));
	}

	@Override
	protected String getName(final Object element) {
		return handler.getName((ConstStatement) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asPackageOrComponentClassifier, ConstStatement.class);
	}

}
