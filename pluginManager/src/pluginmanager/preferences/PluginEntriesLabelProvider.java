package pluginmanager.preferences;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;
import org.osgi.framework.Bundle;

public class PluginEntriesLabelProvider extends BaseLabelProvider implements
		ITableLabelProvider, IColorProvider {

	public PluginEntriesLabelProvider() {
		super();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element == null) {
			return null;
		}
		Bundle data = (Bundle) element;
		switch (columnIndex) {
		case 0:
			return null;
		case 1:
			return data.getSymbolicName();
		case 2:
			return data.getVersion().toString();
		case 3:
			if (data.getSignerCertificates(Bundle.SIGNERS_ALL) == null) {
				return null;
			}
			StringBuffer sb = new StringBuffer();
			Map<X509Certificate,List<X509Certificate>> cs = data.getSignerCertificates(Bundle.SIGNERS_ALL);
            Set<X509Certificate> ccs = cs.keySet();
            for (Iterator<X509Certificate> iterator = ccs.iterator(); iterator.hasNext();) {
				X509Certificate c = (X509Certificate) iterator.next();
				sb.append(c.getSubjectX500Principal().getName());
			}
			return sb.toString();
		default:
			return null;
		}
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void createColumns(TableViewer viewer) {
		String[] titles = {
				"", //$NON-NLS-1$
				"Name",
				"Version",
				"Sign"};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableViewerColumn(viewer, SWT.NONE)
					.getColumn();
			column.setText(titles[i]);
			column.setResizable(true);
		}
	}

	public Color getBackground(Object element) {
		return null;
	}

	public Color getForeground(Object element) {
		return null;
	}

}