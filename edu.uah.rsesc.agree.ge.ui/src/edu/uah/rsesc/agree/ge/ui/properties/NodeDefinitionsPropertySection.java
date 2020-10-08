package edu.uah.rsesc.agree.ge.ui.properties;

import org.eclipse.jface.viewers.IFilter;
import org.osate.ge.ui.PropertySectionUtil;

import com.rockwellcollins.atc.agree.agree.NodeDef;

import edu.uah.rsesc.agree.ge.businessObjectHandlers.NodeDefHandler;
import edu.uah.rsesc.agree.ge.ui.palette.CreateNodePaletteCommand;

public class NodeDefinitionsPropertySection extends GenericPropertySection {
	final NodeDefHandler handler = new NodeDefHandler();
	private final CreateNodePaletteCommand createCommand = new CreateNodePaletteCommand();

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
		return handler.getName((NodeDef) element);
	}

	@Override
	protected Object[] getSpecStatements() {
		return getSpecStatements(AgreePropertySectionUtil::asPackageOrComponentClassifier, NodeDef.class);
	}

}
