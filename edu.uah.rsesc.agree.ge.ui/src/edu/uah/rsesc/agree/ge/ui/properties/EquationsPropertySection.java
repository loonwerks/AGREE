package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.EqStatement;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.EquationStatementHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateEquationPaletteCommand;

public class EquationsPropertySection extends GenericPropertySection {
	private final EquationStatementHandler handler = new EquationStatementHandler();
	private final CreateEquationPaletteCommand createCommand = new CreateEquationPaletteCommand();

	public static class Filter implements IFilter {
		@Override
		public boolean select(final Object toTest) {
			return AgreePropertySectionUtil.asComponentClassifier(toTest) != null;
		}
	}

	@Override
	protected void onAdd() {
		getSelectedBos().bocStream().findFirst()
				.ifPresent(target -> createCommand.getOperation(target).ifPresent(PropertySectionUtil::execute));
	}

	@Override
	protected String getName(final Object element) {
		return handler.getName((EqStatement) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asComponentClassifier, EqStatement.class);
	}

}
