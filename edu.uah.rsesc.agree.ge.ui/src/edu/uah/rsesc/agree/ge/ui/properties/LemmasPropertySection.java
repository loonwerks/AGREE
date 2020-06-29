package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.LemmaStatement;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.LemmaStatementHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateLemmaPaletteCommand;

public class LemmasPropertySection extends GenericPropertySection {
	private final LemmaStatementHandler handler = new LemmaStatementHandler();
	private final CreateLemmaPaletteCommand createCommand = new CreateLemmaPaletteCommand();

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
		return handler.getName((LemmaStatement) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asComponentImplementation, LemmaStatement.class);
	}

}
