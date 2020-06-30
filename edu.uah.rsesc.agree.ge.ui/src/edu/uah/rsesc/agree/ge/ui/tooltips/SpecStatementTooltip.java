package edu.uah.rsesc.agree.ge.ui.tooltips;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.osate.ge.ui.TooltipContributor;
import org.osate.ge.ui.TooltipContributorContext;

import com.rockwellcollins.atc.agree.agree.SpecStatement;

import edu.uah.rsesc.agree.ge.ui.util.TextConversionUtil;

public class SpecStatementTooltip implements TooltipContributor {
	@Override
	public void addTooltipContents(final TooltipContributorContext ctx) {
		ctx.getBusinessObjectContext().getBusinessObject(SpecStatement.class).ifPresent(bo -> {
			final Label lbl = new Label(ctx.getTooltip(), SWT.NONE);
			lbl.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
			lbl.setText(TextConversionUtil.toText(bo));
		});
	}
}
