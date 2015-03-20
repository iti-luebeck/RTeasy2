package org.desert.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.desert.helper.CentralLookup;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.modules.openide.windows.GlobalActionContextImpl;
import org.openide.cookies.OpenCookie;
import org.openide.util.ContextGlobalProvider;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * This class proxies the original ContextGlobalProvider. It provides the
 * ability to add and remove objects from the application-wide global selection.
 */
@ServiceProvider(service = ContextGlobalProvider.class,
        supersedes = "org.netbeans.modules.openide.windows.GlobalActionContextImpl")
public class GlobalActionContextProxy implements ContextGlobalProvider {

    /**
     * Additional content for our proxy lookup
     */
    private final InstanceContent content;
    /**
     * The native NetBeans global context Lookup provider
     */
    private final GlobalActionContextImpl globalContextProvider;
    /**
     * The primary lookup managed by the platform
     */
    private final Lookup globalContextLookup;
    /**
     * The project lookup managed by this class
     */
    private Lookup centralLookup;
    /**
     * The project lookup managed by resultChanged
     */
    private Lookup openLookup;
    /**
     * The actual Lookup returned by this class
     */
    private Lookup proxyLookup;

    private final Result openCookies;

    public GlobalActionContextProxy() {
        this.content = new InstanceContent();

        // Create the default GlobalContextProvider
        this.globalContextProvider = new GlobalActionContextImpl();
        this.globalContextLookup = this.globalContextProvider.createGlobalContext();
        this.openCookies = this.globalContextLookup.lookupResult(OpenCookie.class);
        this.openCookies.addLookupListener(new LookupListenerImpl());
        EditorRegistry.addPropertyChangeListener(new EditorRegistryPropertyChangeListener());
    }

    /**
     * Returns a ProxyLookup that adds the application-wide content to the
     * original lookup returned by Utilities.actionsGlobalContext().
     *
     * @return a ProxyLookup that includes the default global context plus our
     * own content
     */
    @Override
    public Lookup createGlobalContext() {
        if (this.proxyLookup == null) {
            // Merge the two lookups that make up the proxy
            this.centralLookup = CentralLookup.getDefault();
            this.openLookup = new AbstractLookup(content);
            this.proxyLookup = new ProxyLookup(this.globalContextLookup, this.centralLookup, this.openLookup);
        }
        return this.proxyLookup;
    }

    /**
     * Clears openLookup and adds new Open instance.
     * 
     * @param o Open instance
     */
    private void updateOpenLookup(Open o) {
        clearOpenLookup();
        content.add(o);
    }

    /**
     * Clears openLookup.
     */
    private void clearOpenLookup() {
        for (Open open : openLookup.lookupAll(Open.class)) {
            content.remove(open);
        }
    }

    /**
     * Listen for new OpenCookies and updates openLookup if one is found.
     */
    class LookupListenerImpl implements LookupListener {

        @Override
        public void resultChanged(LookupEvent ev) {
            if (!openCookies.allInstances().isEmpty()) {
                updateOpenLookup(new Open());
            }
        }

    }

    /**
     * Listen for closing of an editor window. Clears openLookup if editor is closed.
     */
    class EditorRegistryPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(EditorRegistry.LAST_FOCUSED_REMOVED_PROPERTY)) {
                Simulator.cancel();
                clearOpenLookup();
            }
        }

    }
}
