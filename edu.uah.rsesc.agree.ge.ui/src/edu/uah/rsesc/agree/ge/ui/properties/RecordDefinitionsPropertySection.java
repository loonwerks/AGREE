package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.RecordDef;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.RecordDefExpressionHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateRecordPaletteCommand;

public class RecordDefinitionsPropertySection extends GenericPropertySection {
	final RecordDefExpressionHandler handler = new RecordDefExpressionHandler();
	private final CreateRecordPaletteCommand createCommand = new CreateRecordPaletteCommand();

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
		return handler.getName((RecordDef) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asPackageOrComponentClassifier, RecordDef.class);
	}

}
