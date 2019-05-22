package au.com.addstar.serversigns.listeners;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.translations.Message;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerListener implements Listener {
    private static final EnumSet<Material> PLATE_MATERIALS = EnumSet.of(Material.OAK_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
    private ServerSignsPlugin plugin;
    private HashMap<UUID, PlatePair> plateMap = new HashMap<>();

    public PlayerListener(ServerSignsPlugin instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void playerInteractCheck(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Material type = (event.getClickedBlock()!=null)? event.getClickedBlock().getState().getType(): event.getClickedBlock().getType();
        if (SVSMetaManager.hasExclusiveMeta(player, SVSMetaKey.YES)) {
            plugin.debug("player interact excluded by meta - action ignored");
            return;
        }
        plugin.debug("player interact processing for SVS");
        if (((event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) || (event.getAction().equals(Action.PHYSICAL)) || ((this.plugin.config.getAllowLeftClicking()) && (event.getAction().equals(Action.LEFT_CLICK_BLOCK)))) && (
                (this.plugin.config.getAnyBlock()) || (this.plugin.config.getBlocks().contains(type)))) {
            UUID playerUniqueId = player.getUniqueId();
            PlatePair pair = this.plateMap.get(playerUniqueId);
            if (pair == null) {
                ServerSign sign = this.plugin.serverSignsManager.getServerSignByLocation(event.getClickedBlock().getLocation());
                if (sign != null) {
                    plugin.debug("Sign clicked action processing");
                    if ((event.getAction().equals(Action.LEFT_CLICK_BLOCK)) && (this.plugin.config.getAllowLeftClicking()) && (player.hasPermission("serversigns.admin")) && (
                            ((this.plugin.config.getSneakToDestroy()) && (player.isSneaking())) || ((!this.plugin.config.getSneakToDestroy()) && (!player.isSneaking())))) {
                        this.plugin.serverSignsManager.remove(sign);
                        this.plugin.send(event.getPlayer(), Message.COMMANDS_REMOVED);
                        event.setCancelled(true);
                        plugin.debug("Sign clicked - removed by admin");
                        return;
                    }

                    plugin.debug("Sign clicked - triggering");
                    this.plugin.serverSignExecutor.executeSignFull(player, sign, event);
                    plugin.debug("Sign clicked - execute complete");
                    if (PLATE_MATERIALS.contains(type)) {
                        pair = new PlatePair(createRemoveTask(this.plugin, playerUniqueId), false);
                        this.plateMap.put(playerUniqueId, pair);
                    }
                }
                plugin.debug("No Sign detected at location");
            } else {
                plugin.debug("player interact paired result");
                pair.getTask().cancel();
                pair.setTask(createRemoveTask(this.plugin, playerUniqueId));
                event.setCancelled(pair.isCancelled());
            }
        }else {
            plugin.debug("player interact ignorred by SVS Action:"+event.getAction().name()+" BLockType:"+type);
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
        return new BukkitRunnable() {
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


