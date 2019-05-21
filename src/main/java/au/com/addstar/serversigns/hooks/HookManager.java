package au.com.addstar.serversigns.hooks;

import au.com.addstar.serversigns.ServerSignsPlugin;

public class HookManager {
    public final HookWrapper<VaultHook> vault;

    public HookManager(ServerSignsPlugin plugin) {
        this.vault = new HookWrapper(VaultHook.class, new Class[]{ServerSignsPlugin.class}, new Object[]{plugin});
    }

    public void tryInstantiateHooks(boolean deepVerbose) {
        try {
            this.vault.instantiateHook();
        } catch (Exception ex) {
            ServerSignsPlugin.log("Unable to load Vault dependency - certain economy and permission features will be disabled");
            ServerSignsPlugin.log("Please download Vault at http://dev.bukkit.org/server-mods/vault/ for Economy and \"Permission grant\" support.");
        }
    }
}