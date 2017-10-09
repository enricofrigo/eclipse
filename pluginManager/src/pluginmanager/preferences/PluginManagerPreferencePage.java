package pluginmanager.preferences;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PluginManagerPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Label providerLabel;
	protected Button providerCheckbox;
	private PluginEntriesComposite pluginEntriesComposite;

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		createProviderComposite(composite);
		createPluginEntriesComposite(composite);

		initializeValues();
		return composite;
	}

	private void createProviderComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		providerLabel = new Label(composite, SWT.NONE);
		providerLabel.setText("Show all plugin");
		providerCheckbox = new Button(composite, SWT.CHECK);
		providerCheckbox.setSelection(false);
		providerCheckbox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setProvider(providerCheckbox.getSelection());
			}
		});
	}

	private void createPluginEntriesComposite(Composite parent) {
		pluginEntriesComposite = new PluginEntriesComposite(parent, SWT.NONE);
		pluginEntriesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));
	}

	public void init(IWorkbench workbench) {
		// Nothing to do
	}

	protected void performApply() {
		try {
			pluginEntriesComposite.performApply();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void performDefaults() {
	}

	public boolean performOk() {
		performApply();
		return super.performOk();
	}

	private void initializeValues() {
		providerCheckbox.setSelection(false);
	}

	protected void setProvider(boolean b) {
		pluginEntriesComposite.setShowAll(b);
	}

}