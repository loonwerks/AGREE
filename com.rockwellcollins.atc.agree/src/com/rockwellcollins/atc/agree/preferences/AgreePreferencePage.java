package com.rockwellcollins.atc.agree.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.rockwellcollins.atc.agree.Activator;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */
public class AgreePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public AgreePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Agree Validation Settings");
	}

	private BooleanFieldEditor validationEnabledFieldEditor;

	@Override
	public void createFieldEditors() {
		validationEnabledFieldEditor = new BooleanFieldEditor(PreferenceConstants.PREF_VALIDATION_ENABLED,
				"Enable AGREE validation in IDE", getFieldEditorParent());
		addField(validationEnabledFieldEditor);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		configureEnabledFieldEditors();
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		// IPreferenceStore prefs = getPreferenceStore();
		configureEnabledFieldEditors();
	}

	private void configureEnabledFieldEditors() {
		validationEnabledFieldEditor.setEnabled(true, getFieldEditorParent());
	}

	@Override
	protected void initialize() {
		super.initialize();
		initializeStateVariables();
		configureEnabledFieldEditors();
	}

	private void initializeStateVariables() {
		// IPreferenceStore prefs = getPreferenceStore();
	}

	@Override
	public void init(IWorkbench workbench) {
	}
}
