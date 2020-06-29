package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.GuaranteeStatement;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.GuaranteeStatementHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateGuaranteePaletteCommand;

public class GuaranteesPropertySection extends GenericPropertySection {
	final GuaranteeStatementHandler handler = new GuaranteeStatementHandler();
	private final CreateGuaranteePaletteCommand createCommand = new CreateGuaranteePaletteCommand();

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
		return handler.getName((GuaranteeStatement) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asComponentType, GuaranteeStatement.class);
	}

}
