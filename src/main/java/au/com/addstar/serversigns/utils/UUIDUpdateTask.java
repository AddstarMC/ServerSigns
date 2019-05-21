package au.com.addstar.serversigns.utils;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UUIDUpdateTask {
    private ServerSignsPlugin plugin;
    private ServerSign sign;
    private java.util.Map<String, java.util.UUID> uuidMap =  new HashMap<>();
    private boolean complete = false;


    public UUIDUpdateTask(ServerSignsPlugin plugin, ServerSign sign) {
        this.plugin = plugin;
        this.sign = sign;
    }

    public void updateLastUse() {
        final List<String> list =  new ArrayList<>(this.sign.getLastUse().keySet());
        if (list.isEmpty()) return;
        ServerSignsPlugin.log("Starting UUID conversion for " + list.size() + " usernames from a ServerSign at " + this.sign.getLocationString());

        fillUUIDMapAsync(list);


        new org.bukkit.scheduler.BukkitRunnable() {
            public void run() {
                if (!UUIDUpdateTask.this.complete) return;
                if ((UUIDUpdateTask.this.uuidMap == null) || (UUIDUpdateTask.this.uuidMap.isEmpty())) {
                    cancel();
                    return;
                }

                HashMap<String, Long> oldLastUse = UUIDUpdateTask.this.sign.getLastUse();
                HashMap<String, Long> newLastUse =  new HashMap<>();

                for (java.util.Map.Entry<String, Long> entry : oldLastUse.entrySet()) {
                    if (UUIDUpdateTask.this.uuidMap.containsKey(entry.getKey())) {

                        newLastUse.put(UUIDUpdateTask.this.uuidMap.get(entry.getKey()).toString().trim(), entry.getValue());
                    }
                }
                UUIDUpdateTask.this.sign.setLastUse(newLastUse);
                UUIDUpdateTask.this.plugin.serverSignsManager.save(UUIDUpdateTask.this.sign);
                cancel();

                ServerSignsPlugin.log("Finishing UUID conversion for " + list.size() + " usernames from a ServerSign at " + UUIDUpdateTask.this.sign.getLocationString());
            }
        }.runTaskTimer(this.plugin, 50L, 50L);
    }

    private void fillUUIDMapAsync(final List<String> usernames) {
        new org.bukkit.scheduler.BukkitRunnable() {
            public void run() {
                int index = 0;


                while (index <= usernames.size()) {
                    int closeIndex = index + 100;
                    if (closeIndex >= usernames.size()) {
                        closeIndex = usernames.size() - 1;
                    }
                    UUIDFetcher fetcher = new UUIDFetcher(usernames.subList(index, closeIndex), true);
                    try {
                        UUIDUpdateTask.this.uuidMap.putAll(fetcher.call());
                    } catch (Exception e) {
                        if (e.getMessage().contains("429")) {
                            try {
                                Thread.sleep(600000L);
                                return;
                            } catch (InterruptedException ignored) {
                            }
                        }

                        ServerSignsPlugin.log("We encountered an error while trying to convert usernames to UUIDs! Error as follows:");
                        e.printStackTrace();
                    }

                    index += 100;
                }

                UUIDUpdateTask.this.complete = true;
            }
        }.runTaskAsynchronously(this.plugin);
    }
}
