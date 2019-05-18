package au.com.addstar.serversigns.meta;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;


public class SVSMetaManager {
    public static final UUID CONSOLE_UUID = UUID.randomUUID();
    private static HashMap<UUID, SVSMeta> map = new HashMap<>();
    private static HashMap<UUID, SVSMeta> specialMap = new HashMap<>();

    public static void setMeta(Player player, SVSMeta meta) {
        setMeta(player.getUniqueId(), meta);
    }

    public static void setMeta(UUID player, SVSMeta meta) {
        map.put(player, meta);
    }

    public static SVSMeta getMeta(Player player) {
        return getMeta(player.getUniqueId());
    }

    public static SVSMeta getMeta(UUID player) {
        return map.get(player);
    }

    public static void removeMeta(Player player) {
        removeMeta(player.getUniqueId());
    }

    public static void removeMeta(UUID player) {
        map.remove(player);
    }

    public static boolean hasMeta(Player player) {
        return hasMeta(player.getUniqueId());
    }

    public static boolean hasMeta(UUID player) {
        return map.containsKey(player);
    }

    public static boolean hasSpecialMeta(UUID player) {
        return specialMap.containsKey(player);
    }

    public static boolean hasSpecialMeta(UUID player, SVSMetaKey key) {
        return (specialMap.get(player) != null) && (specialMap.get(player).getKey().equals(key));
    }

    public static SVSMeta getSpecialMeta(UUID player) {
        return specialMap.get(player);
    }

    public static void setSpecialMeta(UUID player, SVSMeta meta) {
        specialMap.put(player, meta);
    }

    public static void removeSpecialMeta(UUID player) {
        specialMap.remove(player);
    }

    public static boolean hasExclusiveMeta(Player player, SVSMetaKey... excluded) {
        return hasExclusiveMeta(player.getUniqueId(), excluded);
    }

    public static boolean hasExclusiveMeta(UUID player, SVSMetaKey... excluded) {
        if (hasMeta(player)) {
            for (SVSMetaKey key : excluded) {
                if (getMeta(player).getKey().equals(key)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean hasInclusiveMeta(Player player, SVSMetaKey... included) {
        return hasInclusiveMeta(player.getUniqueId(), included);
    }

    public static boolean hasInclusiveMeta(UUID player, SVSMetaKey... included) {
        if (hasMeta(player)) {
            for (SVSMetaKey key : included) {
                if (getMeta(player).getKey().equals(key)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static void clear() {
        map.clear();
    }
}
