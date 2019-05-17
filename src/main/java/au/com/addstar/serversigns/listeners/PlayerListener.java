package au.com.addstar.serversigns.listeners;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.translations.Message;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

public class PlayerListener implements Listener {
    private static final EnumSet<Material> PLATE_MATERIALS = EnumSet.of(Material.WOOD_PLATE, Material.STONE_PLATE, Material.IRON_PLATE, Material.GOLD_PLATE);
    private ServerSignsPlugin plugin;
    private HashMap<UUID, PlatePair> plateMap = new HashMap();

    public PlayerListener(ServerSignsPlugin instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void playerInteractCheck(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (SVSMetaManager.hasExclusiveMeta(player, SVSMetaKey.YES)) {
            return;
        }

        if (((event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) || (event.getAction().equals(Action.PHYSICAL)) || ((this.plugin.config.getAllowLeftClicking()) && (event.getAction().equals(Action.LEFT_CLICK_BLOCK)))) && (
                (this.plugin.config.getAnyBlock()) || (this.plugin.config.getBlocks().contains(block.getType())))) {
            UUID playerUniqueId = player.getUniqueId();
            PlatePair pair = this.plateMap.get(playerUniqueId);
            if (pair == null) {
                ServerSign sign = this.plugin.serverSignsManager.getServerSignByLocation(block.getLocation());
                if (sign != null) {
                    if ((event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && (this.plugin.config.getAllowLeftClicking()) && (player.hasPermission("serversigns.admin")) && (
                            ((this.plugin.config.getSneakToDestroy()) && (player.isSneaking())) || ((!this.plugin.config.getSneakToDestroy()) && (!player.isSneaking())))) {
                        this.plugin.serverSignsManager.remove(sign);
                        this.plugin.send(event.getPlayer(), Message.COMMANDS_REMOVED);
                        event.setCancelled(true);
                        return;
                    }


                    this.plugin.serverSignExecutor.executeSignFull(player, sign, event);
                    if (PLATE_MATERIALS.contains(block.getType())) {
                        pair = new PlatePair(createRemoveTask(this.plugin, playerUniqueId), event.isCancelled());
                        this.plateMap.put(playerUniqueId, pair);
                    }
                }
            } else {
                pair.getTask().cancel();
                pair.setTask(createRemoveTask(this.plugin, playerUniqueId));
                event.setCancelled(pair.isCancelled());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void playerDeathCheck(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (this.plugin.config.getCancelTasksOnDeath()) {
            this.plugin.taskManager.removePlayerTasks(player.getUniqueId(), this.plugin.config.getCompiledCancelTaskPattern());
        }
    }

    private BukkitTask createRemoveTask(ServerSignsPlugin plugin, final UUID playerUniqueId) {
        new org.bukkit.scheduler.BukkitRunnable() {
            public void run() {
                PlayerListener.this.plateMap.remove(playerUniqueId);
            }
        }.runTaskLater(plugin, 5L);
    }

    private class PlatePair {
        private boolean cancelled;
        private BukkitTask task;

        public PlatePair(BukkitTask task, boolean cancelled) {
            this.cancelled = cancelled;
            this.task = task;
        }

        public boolean isCancelled() {
            return this.cancelled;
        }

        public BukkitTask getTask() {
            return this.task;
        }

        public void setTask(BukkitTask task) {
            this.task = task;
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\listeners\PlayerListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */