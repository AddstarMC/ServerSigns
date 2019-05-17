package au.com.addstar.serversigns.listeners;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.translations.Message;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements org.bukkit.event.Listener {
    public ServerSignsPlugin plugin;

    public BlockListener(ServerSignsPlugin instance) {
        this.plugin = instance;
    }

    @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        org.bukkit.Location location = block.getLocation();

        if ((this.plugin.config.getAnyBlock()) || (this.plugin.config.getBlocks().contains(block.getType()))) {
            ServerSign sign = this.plugin.serverSignsManager.getServerSignByLocation(location);
            if (sign != null) {
                if (event.getPlayer().hasPermission("serversigns.admin")) {
                    if ((this.plugin.config.getSneakToDestroy()) && (!event.getPlayer().isSneaking())) {
                        this.plugin.send(event.getPlayer(), Message.MUST_SNEAK);
                        event.setCancelled(true);
                        return;
                    }

                    this.plugin.serverSignsManager.remove(sign);
                    this.plugin.send(event.getPlayer(), Message.COMMANDS_REMOVED);
                } else {
                    event.setCancelled(true);
                    this.plugin.send(event.getPlayer(), Message.CANNOT_DESTROY);
                }
                return;
            }
        }

        if (this.plugin.serverSignsManager.isLocationProtectedByServerSign(location)) {
            event.setCancelled(true);
            this.plugin.send(event.getPlayer(), Message.BLOCK_IS_PROTECTED);
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\listeners\BlockListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */