package pluginmanager.preferences;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import pluginmanager.Activator;
import pluginmanager.service.IPluginManagerService;

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
		return ALL_PROVIDER;
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

	public static Bundle[] getProxyData(String provider) {
//		if (provider.equals(TRUSTED_PROVIDER)) {
//			return Activator.context.getBundles();
//		} else if (provider.equals(ECLIPSE_PROVIDER)) {
//			return Activator.context.getBundles();
//		} else if (provider.equals(ALL_PROVIDER)) {
			return Activator.context.getBundles();
//		}
//		throw new IllegalArgumentException("Provider not supported"); //$NON-NLS-1$
	}

	public static boolean belognToProvider(Bundle bundle, String provider){
		if(TRUSTED_PROVIDER.equals(provider) && bundle.getSignerCertificates(Bundle.SIGNERS_TRUSTED)!=null)
			return true;
		else if(ECLIPSE_PROVIDER.equals(provider) && bundle.getSignerCertificates(Bundle.SIGNERS_ALL)!=null){
	        Map<X509Certificate,List<X509Certificate>> cs = bundle.getSignerCertificates(Bundle.SIGNERS_ALL);
	        Set<X509Certificate> ccs = cs.keySet();
	        for (Iterator iterator = ccs.iterator(); iterator.hasNext();) {
				X509Certificate c = (X509Certificate) iterator.next();
				return c.getSubjectDN().getName().contains(ECLIPSE_PROVIDER);
			}
		}
		return true;
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