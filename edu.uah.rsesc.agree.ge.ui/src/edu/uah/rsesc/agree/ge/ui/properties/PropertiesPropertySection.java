package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.PropertyStatement;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.PropertyStatementHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreatePropertyPaletteCommand;

public class PropertiesPropertySection extends GenericPropertySection {
	final PropertyStatementHandler handler = new PropertyStatementHandler();
	private final CreatePropertyPaletteCommand createCommand = new CreatePropertyPaletteCommand();

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
		return handler.getName((PropertyStatement) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asComponentClassifier, PropertyStatement.class);
	}

}
