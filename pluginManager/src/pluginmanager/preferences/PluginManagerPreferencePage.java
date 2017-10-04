package pluginmanager.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class PluginManagerPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private static final String PROXY_PREFERENCE_PAGE_CONTEXT_ID = "org.eclipse.ui.net.proxy_preference_page_context"; //$NON-NLS-1$

	private Label providerLabel;
	protected Combo providerCombo;
	private PluginEntriesComposite pluginEntriesComposite;

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);

		createProviderComposite(composite);
		createPluginEntriesComposite(composite);

		// Adding help accessible by F1
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
				PROXY_PREFERENCE_PAGE_CONTEXT_ID);

		initializeValues();
		return composite;
	}

	private void createProviderComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		providerLabel = new Label(composite, SWT.NONE);
		providerLabel.setText("Plugins sign");
		providerCombo = new Combo(composite, SWT.READ_ONLY | SWT.DROP_DOWN);
		providerCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setProvider(PluginSelector.localizeProvider(providerCombo.getText()));
			}
		});
	}

	private void createPluginEntriesComposite(Composite parent) {
		pluginEntriesComposite = new PluginEntriesComposite(parent, SWT.NONE);
		pluginEntriesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true));
	}

	public void init(IWorkbench workbench) {
		// Nothing to do
	}

	protected void performApply() {
		int sel = providerCombo.getSelectionIndex();
		pluginEntriesComposite.performApply();
		PluginSelector.setActiveProvider(PluginSelector.localizeProvider(providerCombo.getItem(sel)));
	}

	protected void performDefaults() {
		int index = 1;
		if (providerCombo.getItemCount() == 3) {
			index = 2;
		}
		providerCombo.select(index);
		setProvider(PluginSelector.localizeProvider(providerCombo.getItem(index)));
	}

	public boolean performOk() {
		performApply();
		return super.performOk();
	}

	private void initializeValues() {
		String[] providers = PluginSelector.getProviders();
		String[] localizedProviders = new String[providers.length];
		for (int i = 0; i < localizedProviders.length; i++) {
			localizedProviders[i] = PluginSelector.localizeProvider(providers[i]);
		}
		providerCombo.setItems(localizedProviders);
		providerCombo.select(providerCombo.indexOf(PluginSelector
				.localizeProvider(PluginSelector.getDefaultProvider())));
	}

	protected void setProvider(String name) {
		pluginEntriesComposite.setProvider(name);
	}

}