package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.LinearizationDef;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.LinearizationDefHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateLinearizationPaletteCommand;

public class LinearizationDefinitionsPropertySection extends GenericPropertySection {
	final LinearizationDefHandler handler = new LinearizationDefHandler();
	private final CreateLinearizationPaletteCommand createCommand = new CreateLinearizationPaletteCommand();

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
		return handler.getName((LinearizationDef) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asPackageOrComponentClassifier, LinearizationDef.class);
	}

}
