package pluginmanager.preferences;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.osgi.framework.Bundle;

/**
 * This class is the Composite that consists of the controls for proxy entries
 * and is used by ProxyPreferencesPage.
 */
public class PluginEntriesComposite extends Composite {

	private Label entriesLabel;
	private CheckboxTableViewer entriesViewer;
	// private Button addButton;
	private Button editButton;
	private Button removeButton;

	protected String currentProvider;
	private ArrayList bundleEntries = new ArrayList();

	PluginEntriesComposite(Composite parent, int style) {
		super(parent, style);
		createWidgets();
	}

	protected void createWidgets() {
		setLayout(new GridLayout(2, false));

		entriesLabel = new Label(this, SWT.NONE);
		entriesLabel.setText("Select Plugins");
		entriesLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false, 2, 1));

		Table entriesTable = new Table(this, SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.CHECK);
		entriesTable.setHeaderVisible(true);
		entriesTable.setLinesVisible(true);
		entriesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 3));

		entriesViewer = new CheckboxTableViewer(entriesTable);
		PluginEntriesLabelProvider labelProvider = new PluginEntriesLabelProvider();
		PluginEntriesContentProvider contentProvider = new PluginEntriesContentProvider();
		labelProvider.createColumns(entriesViewer);
		entriesViewer.setContentProvider(contentProvider);
		entriesViewer.setLabelProvider(labelProvider);

		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnPixelData(24));
		tableLayout.addColumnData(new ColumnWeightData(20, 50, true));
		tableLayout.addColumnData(new ColumnWeightData(50, 50, true));
		tableLayout.addColumnData(new ColumnWeightData(20, 50, true));

		entriesTable.setLayout(tableLayout);

		// addButton = createButton(NetUIMessages.ProxyPreferencePage_9);
		// editButton = createButton("Edit");
		// removeButton = createButton("Remove");

		entriesViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						enableButtons();
					}
				});
		entriesViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				setProvider(currentProvider);
			}
		});
//		entriesViewer.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				// editSelection();
//			}
//		});
//		editButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				// editSelection();
//			}
//		});
//		removeButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				// removeSelection();
//			}
//		});

		initializeValues();
		enableButtons();
	}

	protected void enableButtons() {
		boolean enabled = getEnabled();
		if (enabled) {
			//editButton.setEnabled(isSelectionEditable());
			//removeButton.setEnabled(isSelectionRemovable());
		} else {
			//editButton.setEnabled(false);
			//removeButton.setEnabled(false);
		}
	}

	private boolean isSelectionEditable() {
		IStructuredSelection selection = (IStructuredSelection) entriesViewer
				.getSelection();
		return isSelectionRemovable() && selection.size() == 1;
	}

	private boolean isSelectionRemovable() {
		return true;
	}

	private String getEditableProvider() {
		return null;
	}

//	protected void editSelection() {
//		if (!isSelectionRemovable()) {
//			return;
//		}
//		Iterator itsel = ((IStructuredSelection) entriesViewer.getSelection()).iterator();
//		Bundle toEdit = null;
//		if (itsel.hasNext()) {
//			toEdit = ((Bundle) itsel.next());
//		} else {
//			return;
//		}
//		Iterator it = bundleEntries.iterator();
//		ArrayList added = new ArrayList();
//		String editableProvider = getEditableProvider();
//		while (it.hasNext()) {
//			ProxyData data = (ProxyData) it.next();
//			if (data.getSource().equalsIgnoreCase(editableProvider)) {
//				if (data.getType() != toEdit.getType()) {
//					added.add(data.getType());
//				}
//			}
//		}
//		String addedArray[] = (String[]) added.toArray(new String[0]);
//		ProxyData data = promptForEntry(toEdit, addedArray,
//				NetUIMessages.ProxyEntryDialog_1);
//		if (data != null) {
//			entriesViewer.refresh();
//		}
//	}
//
//	protected void removeSelection() {
//		IStructuredSelection selection = (IStructuredSelection) entriesViewer
//				.getSelection();
//		Iterator it = selection.iterator();
//		while (it.hasNext()) {
//			ProxyData data = (ProxyData) it.next();
//			data.setHost(""); //$NON-NLS-1$
//			data.setPort(-1);
//			data.setUserid(null);
//			data.setPassword(null);
//		}
//		entriesViewer.refresh();
//	}

	public void initializeValues() {
		String providers[] = PluginSelector.getProviders();
		for (int i = 0; i < providers.length; i++) {
			Bundle[] entries = PluginSelector.getProxyData(providers[i]);
			for (int j = 0; j < entries.length; j++) {
				bundleEntries.add(entries[j]);
			}
		}
		entriesViewer.setInput(bundleEntries);
		setProvider(PluginSelector.getDefaultProvider());
	}

	public void setProvider(String item) {
		if (item == null) {
			item = currentProvider;
		} else {
			currentProvider = item;
		}
		ArrayList checked = new ArrayList();
		Iterator<Bundle> it = bundleEntries.iterator();
		while (it.hasNext()) {
			Bundle data = it.next();
			if (PluginSelector.belognToProvider(data, item)) {
				checked.add(data);
			}
		}
		entriesViewer.setCheckedElements(checked.toArray(new Bundle[0]));
	}

	public void performApply() {
		String provider = getEditableProvider();
		Iterator<Bundle> it = bundleEntries.iterator();
		ArrayList<Bundle> proxies = new ArrayList();
		while (it.hasNext()) {
			Bundle data = it.next();
			if (PluginSelector.belognToProvider(data, provider)) {
				proxies.add(data);
			}
		}
		Bundle[] data = proxies.toArray(new Bundle[0]);
		PluginSelector.setProxyData(provider, data);
	}

}