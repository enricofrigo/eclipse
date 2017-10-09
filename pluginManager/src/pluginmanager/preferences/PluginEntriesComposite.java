package pluginmanager.preferences;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
	private boolean showAll=false;
	
	public void setShowAll(boolean showAll){
		this.showAll=showAll;
		Bundle[] entries = PluginSelector.getProxyData(showAll);
		entriesViewer.setInput(entries);
	}


	PluginEntriesComposite(Composite parent, int style) {
		super(parent, style);
		createWidgets();
	}

	protected void createWidgets() {
		setLayout(new GridLayout(2, false));

		Platform.getPreferencesService();
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

		initializeValues();
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

	public void initializeValues() {
		Bundle[] entries = PluginSelector.getProxyData(showAll);
		entriesViewer.setInput(entries);
	}

	public void performApply() throws CoreException {
		PluginSelector.exportPreferences();
	}

}