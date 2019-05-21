package au.com.addstar.serversigns.hooks;

import au.com.addstar.serversigns.ServerSignsPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    protected ServerSignsPlugin plugin;
    private Economy economy = null;
    private Permission permission = null;

    public VaultHook(ServerSignsPlugin plugin) {
        this.plugin = plugin;

        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            this.economy = economyProvider.getProvider();
        }

        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            this.permission = permissionProvider.getProvider();
        }
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public Permission getPermission() {
        return this.permission;
    }

    public boolean hasEconomy() {
        return this.economy != null;
    }

    public boolean hasPermissions() {
        return this.permission != null;
    }
}


