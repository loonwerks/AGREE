package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.AssertStatement;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.AssertStatementHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateAssertionPaletteCommand;

public class AssertionsPropertySection extends GenericPropertySection {
	private final AssertStatementHandler handler = new AssertStatementHandler();
	private final CreateAssertionPaletteCommand createCommand = new CreateAssertionPaletteCommand();

	public static class Filter implements IFilter {
		@Override
		public boolean select(final Object toTest) {
			return AgreePropertySectionUtil.asComponentImplementation(toTest) != null;
		}
	}

	@Override
	protected void onAdd() {
		getSelectedBos().bocStream().findFirst()
				.ifPresent(target -> createCommand.getOperation(target).ifPresent(PropertySectionUtil::execute));
	}

	@Override
	protected String getName(final Object element) {
		return handler.getName((AssertStatement) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asComponentImplementation, AssertStatement.class);
	}

}
