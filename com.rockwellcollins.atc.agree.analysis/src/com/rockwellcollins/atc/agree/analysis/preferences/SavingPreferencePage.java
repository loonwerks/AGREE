package com.rockwellcollins.atc.agree.analysis.preferences;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osate.ui.dialogs.Dialog;

import com.rockwellcollins.atc.agree.analysis.Activator;

public class SavingPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private BooleanFieldEditor propLogFieldEditor;
	private FileFieldEditor propLogFileFieldEditor;
	private BooleanFieldEditor saveResultsFieldEditor;
	private FileFieldEditor saveResultsFileFieldEditor;
	private BooleanFieldEditor debugFieldEditor;

	public SavingPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Save Analysis Settings");
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {

		propLogFieldEditor = new BooleanFieldEditor(PreferenceConstants.PREF_PROP_LOG, "Generate property analysis log",
				getFieldEditorParent());
		addField(propLogFieldEditor);

		propLogFileFieldEditor = new FileFieldEditor(PreferenceConstants.PREF_PROP_LOG_FILENAME,
				"Property log filename:", true, getFieldEditorParent()) {

			@Override
			protected String changePressed() {

				FileDialog dlgSaveAs = new FileDialog(getShell(), SWT.SAVE | SWT.SHEET);
				dlgSaveAs.setText("AGREE analysis log file");
				if (!getTextControl().getText().isEmpty()) {
					dlgSaveAs.setFileName(getTextControl().getText());
				} else {
					dlgSaveAs.setFileName("agree.log");
				}
				dlgSaveAs.setOverwrite(false);
				dlgSaveAs.setFilterExtensions(new String[] { "*.log", "*.txt", "*.*" });
				String fileName = dlgSaveAs.open();
				if (fileName == null) {
					return null;
				} else {
					fileName = fileName.trim();
				}

				// Create the file if it doesn't exist
				try {
					File file = new File(fileName);
					file.createNewFile();
				} catch (IOException e) {
					Dialog.showError("AGREE analysis log file - Error", "A problem occurred while creating the file.");
					return null;
				}

				return fileName;
			}

			@Override
			protected boolean checkState() {
				// Don't want to enforce proper path/filenaming
				clearErrorMessage();
				return true;
			}
		};
		addField(propLogFileFieldEditor);

		saveResultsFieldEditor = new BooleanFieldEditor(PreferenceConstants.PREF_SAVE_RESULTS, "Save analysis results",
				getFieldEditorParent());
		addField(saveResultsFieldEditor);

		saveResultsFileFieldEditor = new FileFieldEditor(PreferenceConstants.PREF_SAVE_RESULTS_FILENAME,
				"Analysis results filename:", true, getFieldEditorParent()) {

			@Override
			protected String changePressed() {

				FileDialog dlgSaveAs = new FileDialog(getShell(), SWT.SAVE | SWT.SHEET);
				dlgSaveAs.setText("AGREE analysis results file");
				if (!getTextControl().getText().isEmpty()) {
					dlgSaveAs.setFileName(getTextControl().getText());
				} else {
					dlgSaveAs.setFileName("agree_analysis_results.json");
				}
				dlgSaveAs.setOverwrite(false);
				dlgSaveAs.setFilterExtensions(new String[] { "*.json", "*.txt", "*.log", "*.*" });
				String fileName = dlgSaveAs.open();
				if (fileName == null) {
					return null;
				} else {
					fileName = fileName.trim();
				}

				// Create the file if it doesn't exist
				try {
					File file = new File(fileName);
					file.createNewFile();
				} catch (IOException e) {
					Dialog.showError("AGREE analysis results file - Error",
							"A problem occurred while creating the file.");
					return null;
				}

				return fileName;
			}

			@Override
			protected boolean checkState() {
				// Don't want to enforce proper path/filenaming
				clearErrorMessage();
				return true;
			}
		};
		addField(saveResultsFileFieldEditor);

		debugFieldEditor = new BooleanButtonFieldEditor(PreferenceConstants.PREF_DEBUG,
				"Debug mode (record log files in temp folder)",
				"Open temporary folder", this::openTemporaryFolder, getFieldEditorParent());
		addField(debugFieldEditor);

	}

	private void openTemporaryFolder() {
		Desktop desktop = Desktop.getDesktop();
		try {
			File tempFile = File.createTempFile("agree", ".tmp");
			tempFile.delete();
			File tempDir = tempFile.getParentFile();
			desktop.open(tempDir);
		} catch (Throwable t) {
			MessageDialog.openError(getShell(), "Error opening temporary directory",
					"Error opening temporary directory: " + t.getMessage());
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		configureEnabledFieldEditors();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (propLogFieldEditor.getBooleanValue() && saveResultsFieldEditor.getBooleanValue()
				&& propLogFileFieldEditor.getStringValue().contentEquals(saveResultsFileFieldEditor.getStringValue())) {
			setValid(false);
			setErrorMessage("AGREE property analysis log and analysis results cannot have the same filename.");
		} else {
			setValid(true);
			setErrorMessage(null);
			super.propertyChange(event);
		}
		configureEnabledFieldEditors();
	}

	private void configureEnabledFieldEditors() {
		propLogFileFieldEditor.setEnabled(propLogFieldEditor.getBooleanValue(), getFieldEditorParent());
		saveResultsFileFieldEditor.setEnabled(saveResultsFieldEditor.getBooleanValue(), getFieldEditorParent());
	}

}
