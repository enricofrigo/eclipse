package pluginmanager.preferences;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.internal.preferences.PrefsMessages;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferenceFilter;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.core.runtime.preferences.PreferenceFilterEntry;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Bundle;

import pluginmanager.Activator;

/**
 * This class adapts ProxyManager to add additional layer of providers on its
 * top.
 */
public class PluginSelector {

	private static final String TRUSTED_PROVIDER = "Trusted"; //$NON-NLS-1$
	private static final String ECLIPSE_PROVIDER = "Eclipse"; //$NON-NLS-1$
	private static final String ALL_PROVIDER = "All"; //$NON-NLS-1$
	
	public static String[] getProviders() {
		return new String[] { TRUSTED_PROVIDER, ECLIPSE_PROVIDER,
				ALL_PROVIDER };
	}

	public static String localizeProvider(String name) {
		if (TRUSTED_PROVIDER.equals(name)) {
			return TRUSTED_PROVIDER;
		} else if (ECLIPSE_PROVIDER.equals(name)) {
			return ECLIPSE_PROVIDER;
		} else if (ALL_PROVIDER.equals(name)) {
			return ALL_PROVIDER;
		}
		Assert.isTrue(false);
		return null;
	}

	public static String getDefaultProvider() {
		return ECLIPSE_PROVIDER;
	}

	public static void setActiveProvider(String provider) {
//		IProxyService service = ProxyManager.getProxyManager();
//		if (provider.equals(TRUSTED_PROVIDER)) {
//			service.setProxiesEnabled(false);
//			service.setSystemProxiesEnabled(false);
//		} else if (provider.equals(ECLIPSE_PROVIDER)) {
//			service.setProxiesEnabled(true);
//			service.setSystemProxiesEnabled(false);
//		} else if (provider.equals(ALL_PROVIDER)) {
//			service.setProxiesEnabled(true);
//			service.setSystemProxiesEnabled(true);
//		} else {
//			throw new IllegalArgumentException("Provider not supported"); //$NON-NLS-1$
//		}
	}

	public static Bundle[] getProxyData(boolean showAll) {
		if (!showAll) {
			Bundle[] bs = Activator.context.getBundles();
			ArrayList<Bundle> albs = new ArrayList<Bundle>();
			for (int i = 0; i < bs.length; i++) {
				Map<X509Certificate,List<X509Certificate>> msgs = bs[i].getSignerCertificates(Bundle.SIGNERS_ALL);
				if(msgs==null || msgs.isEmpty()){
					albs.add(bs[i]);
				}
				Set<X509Certificate> ssgs = msgs.keySet();
				for (Iterator<X509Certificate> iterator = ssgs.iterator(); iterator.hasNext();) {
					X509Certificate cert = (X509Certificate) iterator.next();
					if(!cert.getSubjectX500Principal().getName().contains("clipse"))
						albs.add(bs[i]);
				}
			}
			return albs.toArray(new Bundle[0]);
		} else {
			return Activator.context.getBundles();
		}
	}

//	public static boolean belognToProvider(Bundle bundle, String provider){
//		if(TRUSTED_PROVIDER.equals(provider) && bundle.getSignerCertificates(Bundle.SIGNERS_TRUSTED)!=null)
//			return true;
//		else if(ECLIPSE_PROVIDER.equals(provider) && bundle.getSignerCertificates(Bundle.SIGNERS_ALL)!=null){
//	        Map<X509Certificate,List<X509Certificate>> cs = bundle.getSignerCertificates(Bundle.SIGNERS_ALL);
//	        Set<X509Certificate> ccs = cs.keySet();
//	        for (Iterator<X509Certificate> iterator = ccs.iterator(); iterator.hasNext();) {
//				X509Certificate c = iterator.next();
//				return !c.getSubjectDN().getName().contains(ECLIPSE_PROVIDER);
//			}
//		}
//		return true;
//	}
	
	public static void exportPreferences() throws CoreException {
		File file = new File("c:\\temp\\eclipse_export.epf");// path.toFile();
		if (file.exists())
			file.delete();
		file.getParentFile().mkdirs();
		IPreferencesService service = Platform.getPreferencesService();
		OutputStream output = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			output = new BufferedOutputStream(fos);
			IEclipsePreferences node = (IEclipsePreferences) service.getRootNode();
			IPreferenceFilter pf = new IPreferenceFilter() {
				
				@Override
				public String[] getScopes() {
					return new String[]{InstanceScope.SCOPE,ConfigurationScope.SCOPE};
				}
				
				@Override
				public Map<String, PreferenceFilterEntry[]> getMapping(String arg0) {
					return null;
				}
			};
			service.exportPreferences(node, new IPreferenceFilter[]{pf}, output);
			output.flush();
			fos.getFD().sync();
		} catch (IOException e) {
			String message = NLS.bind(PrefsMessages.preferences_errorWriting, file, e.getMessage());
			IStatus status = new Status(IStatus.ERROR, PrefsMessages.OWNER_NAME, IStatus.ERROR, message, e);
			throw new CoreException(status);
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
				}
		}
	}

	public static void setProxyData(String provider, Bundle bundles[]) {
//		if (provider.equals(ECLIPSE_PROVIDER)) {
//			IPluginManagerService service = ProxyManager.getProxyManager();
//			try {
//				service.setData(bundles);
//			} catch (CoreException e) {
//				// Should never occur since ProxyManager does not
//				// declare CoreException to be thrown
//				throw new RuntimeException(e);
//			}
//		} else {
//			throw new IllegalArgumentException(
//					"Provider does not support setting proxy data"); //$NON-NLS-1$ 
//		}
	}
}