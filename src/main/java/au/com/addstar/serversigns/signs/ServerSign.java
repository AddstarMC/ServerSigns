package au.com.addstar.serversigns.signs;

import au.com.addstar.serversigns.persist.mapping.*;
import au.com.addstar.serversigns.itemdata.ItemSearchCriteria;
import au.com.addstar.serversigns.parsing.command.ServerSignCommand;
import au.com.addstar.serversigns.persist.PersistenceEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import au.com.addstar.serversigns.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Attachable;

public class ServerSign implements Cloneable, java.io.Serializable {
    @PersistenceEntry
    private java.util.List<String> permissions = new ArrayList();
    @PersistenceEntry
    private String permissionMessage = "";

    @PersistenceEntry
    private String cancelPermission = "";
    @PersistenceEntry
    private String cancelPermissionMessage = "";

    @PersistenceEntry
    private String world;

    @PersistenceEntry(configPath = "X")
    private int x;
    @PersistenceEntry(configPath = "Y")
    private int y;
    @PersistenceEntry(configPath = "Z")
    private int z;
    @PersistenceEntry
    private double price = 0.0D;
    @PersistenceEntry(configPath = "exp")
    private int xp = 0;

    @PersistenceEntry
    private long cooldown = 0L;
    @PersistenceEntry
    private long globalCooldown = 0L;
    @PersistenceEntry
    private long lastGlobalUse = 0L;
    @PersistenceEntry(configMapper = StringLongHashMapper.class)
    private HashMap<String, Long> lastUse = new HashMap();

    @PersistenceEntry
    private boolean confirmation = false;
    @PersistenceEntry
    private String confirmationMessage = "";

    @PersistenceEntry(configMapper = CancelEnumMapper.class, configPath = "cancel_mode")
    private CancelMode cancel = CancelMode.ALWAYS;

    @PersistenceEntry(configMapper = ServerSignCommandListMapper.class)
    private ArrayList<ServerSignCommand> commands = new ArrayList();

    @PersistenceEntry
    private ArrayList<String> grantPermissions = new ArrayList();

    @PersistenceEntry(configMapper = ItemStackListMapper.class)
    private ArrayList<ItemStack> priceItems = new ArrayList();
    @PersistenceEntry(configMapper = ItemStackListMapper.class)
    private ArrayList<ItemStack> heldItems = new ArrayList();

    @PersistenceEntry(configMapper = ItemSearchCriteriaMapper.class, configPath = "pi_criteria")
    private ItemSearchCriteria pic_options = new ItemSearchCriteria(false, false, false, false);
    @PersistenceEntry(configMapper = ItemSearchCriteriaMapper.class, configPath = "hi_criteria")
    private ItemSearchCriteria hic_options = new ItemSearchCriteria(false, false, false, false);

    @PersistenceEntry
    private int loops = -1;
    @PersistenceEntry(configPath = "loop_delay")
    private int loopDelayInSecs = 1;

    private int currentLoop = 0;
    @PersistenceEntry(configPath = "uses_limit")
    private int useLimit = 0;
    @PersistenceEntry(configPath = "uses_tally")
    private int useTally = 0;

    @PersistenceEntry(configMapper = LocationSetMapper.class)
    private Set<Location> protectedBlocks = new java.util.HashSet();

    @PersistenceEntry
    private boolean displayInternalMessages = true;

    @PersistenceEntry
    private long timeLimitMaximum = 0L;

    @PersistenceEntry
    private long timeLimitMinimum = 0L;

    @PersistenceEntry(configMapper = PlayerInputOptionsMapper.class)
    private Set<PlayerInputOptions> playerInputOptions = new java.util.HashSet();


    public ServerSign() {
    }

    public ServerSign(Location location, ServerSignCommand command) {
        setLocation(location);
        this.commands.add(command);
    }

    public Location getLocation() {
        return new Location(org.bukkit.Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }

    public void setLocation(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.world = location.getWorld().getName();


        updateProtectedBlocks();
    }

    public String getLocationString() {
        return this.world + ", " + this.x + ", " + this.y + ", " + this.z;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }


    public ArrayList<ServerSignCommand> getCommands() {
        return this.commands;
    }

    public void setCommands(ArrayList<ServerSignCommand> commands) {
        this.commands = commands;
    }

    public void addCommand(ServerSignCommand command) {
        this.commands.add(command);
    }

    public void removeCommand(int index) throws IndexOutOfBoundsException {
        this.commands.remove(index);
    }

    public void editCommand(int index, ServerSignCommand command) {
        this.commands.set(index, command);
    }


    public void addGrantPermissions(String permission) {
        this.grantPermissions.add(permission);
    }

    public void removeGrantPermissions() {
        this.grantPermissions.clear();
    }

    public ArrayList<String> getGrantPermissions() {
        return this.grantPermissions;
    }

    public void setGrantPermissions(ArrayList<String> grantPermissions) {
        this.grantPermissions = grantPermissions;
    }


    public java.util.List<String> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(java.util.List<String> permissions) {
        this.permissions = permissions;
    }

    public String getPermissionMessage() {
        return this.permissionMessage;
    }

    public void setPermissionMessage(String message) {
        this.permissionMessage = message;
    }


    public String getCancelPermission() {
        return this.cancelPermission;
    }

    public void setCancelPermission(String permission) {
        this.cancelPermission = permission;
    }

    public String getCancelPermissionMessage() {
        return this.cancelPermissionMessage;
    }

    public void setCancelPermissionMessage(String message) {
        this.cancelPermissionMessage = message;
    }


    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public boolean isConfirmation() {
        return this.confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public String getConfirmationMessage() {
        return this.confirmationMessage;
    }

    public void setConfirmationMessage(String message) {
        this.confirmationMessage = message;
    }

    public long getGlobalCooldown() {
        return this.globalCooldown;
    }

    public void setGlobalCooldown(long globalcooldown) {
        this.globalCooldown = globalcooldown;
    }

    public long getLastGlobalUse() {
        return this.lastGlobalUse;
    }

    public void setLastGlobalUse(long lastGlobalUse) {
        if (this.globalCooldown > 0L) {
            this.lastGlobalUse = lastGlobalUse;
        }
    }


    public long getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public HashMap<String, Long> getLastUse() {
        return this.lastUse;
    }

    public void setLastUse(HashMap<String, Long> lastUse) {
        this.lastUse = lastUse;
    }

    public void addLastUse(UUID player) {
        if (this.cooldown > 0L) {
            this.lastUse.put(player.toString(), System.currentTimeMillis());
        }
    }

    public void removeLastUse(UUID player) {
        this.lastUse.remove(player.toString());
    }

    public long getLastUse(UUID player) {
        if (this.lastUse.containsKey(player.toString())) {
            return this.lastUse.get(player.toString());
        }

        return 0L;
    }


    public void resetCooldowns() {
        this.lastGlobalUse = 0L;
        this.lastUse.clear();
    }


    public void addPriceItem(ItemStack item) {
        this.priceItems.add(item);
    }

    public ArrayList<ItemStack> getPriceItems() {
        return this.priceItems;
    }

    public ArrayList<String> getPriceItemStrings() {
        ArrayList<String> list = new ArrayList(this.priceItems.size());
        for (ItemStack stack : this.priceItems) {
            list.add(ItemUtils.getStringFromItemStack(stack));
        }
        return list;
    }

    public void setPriceItem(ArrayList<ItemStack> array) {
        this.priceItems = array;
    }

    public void clearPriceItems() {
        this.priceItems.clear();
    }

    public ItemSearchCriteria getPIC() {
        return this.pic_options;
    }

    public void setPIC(ItemSearchCriteria options) {
        this.pic_options = options;
    }

    public int getXP() {
        return this.xp;
    }

    public void setXP(Integer cost) {
        this.xp = cost;
    }

    public CancelMode getCancelMode() {
        return this.cancel;
    }

    public void setCancelMode(CancelMode mode) {
        this.cancel = mode;
    }

    public int getLoops() {
        return this.loops;
    }

    public void setLoops(int numberOfLoops) {
        this.loops = numberOfLoops;
    }

    public void setLoopDelay(int delayInSecs) {
        this.loopDelayInSecs = delayInSecs;
    }

    public int getLoopDelayInSecs() {
        return this.loopDelayInSecs;
    }

    public int getCurrentLoop() {
        return this.currentLoop;
    }

    public void setCurrentLoop(int loop) {
        this.currentLoop = loop;
    }


    public ArrayList<ItemStack> getHeldItems() {
        return this.heldItems;
    }

    public void setHeldItems(ArrayList<ItemStack> items) {
        this.heldItems = items;
    }

    public void addHeldItem(ItemStack item) {
        this.heldItems.add(item);
    }

    public void clearHeldItems() {
        this.heldItems.clear();
    }

    public ArrayList<String> getHeldItemStrings() {
        ArrayList<String> list = new ArrayList(this.heldItems.size());
        for (ItemStack stack : this.heldItems) {
            list.add(ItemUtils.getStringFromItemStack(stack));
        }
        return list;
    }

    public ItemSearchCriteria getHIC() {
        return this.hic_options;
    }

    public void setHIC(ItemSearchCriteria options) {
        this.hic_options = options;
    }

    public int getUseLimit() {
        return this.useLimit;
    }

    public void setUseLimit(int newVal) {
        this.useLimit = newVal;
        this.useTally = 0;
    }

    public int getUseTally() {
        return this.useTally;
    }

    public void setUseTally(int newVal) {
        this.useTally = newVal;
    }

    public void incrementUseTally() {
        this.useTally += 1;
    }


    public Set<Location> getProtectedBlocks() {
        return this.protectedBlocks;
    }

    public void setProtectedBlocks(Set<Location> blocks) {
        this.protectedBlocks = blocks;
    }

    public void clearProtectedBlocks() {
        this.protectedBlocks.clear();
    }

    public void addProtectedBlock(Location location) {
        this.protectedBlocks.add(location);
    }

    public boolean isProtected(Location location) {
        return this.protectedBlocks.contains(location);
    }

    public void updateProtectedBlocks() {
        this.protectedBlocks.clear();
        Block block = getLocation().getBlock();

        if ((block.getState().getData() instanceof Attachable)) {
            Attachable attachable = (Attachable) block.getState().getData();
            addProtectedBlock(block.getRelative(attachable.getAttachedFace()).getLocation());
        } else if ((block.getState().getData() instanceof org.bukkit.material.Door)) {
            org.bukkit.material.Door door = (org.bukkit.material.Door) block.getState().getData();
            if (door.isTopHalf()) {
                addProtectedBlock(block.getRelative(org.bukkit.block.BlockFace.DOWN).getRelative(org.bukkit.block.BlockFace.DOWN).getLocation());
            } else {
                addProtectedBlock(block.getRelative(org.bukkit.block.BlockFace.DOWN).getLocation());
            }
        }
    }


    public boolean shouldDisplayInternalMessages() {
        return this.displayInternalMessages;
    }

    public void setDisplayInternalMessages(boolean val) {
        this.displayInternalMessages = val;
    }


    public long getTimeLimitMaximum() {
        return this.timeLimitMaximum;
    }

    public void setTimeLimitMaximum(long value) {
        this.timeLimitMaximum = value;
    }

    public long getTimeLimitMinimum() {
        return this.timeLimitMinimum;
    }

    public void setTimeLimitMinimum(long value) {
        this.timeLimitMinimum = value;
    }

    public void setTimeLimit(long maximum, long minimum) {
        setTimeLimitMinimum(minimum);
        setTimeLimitMaximum(maximum);
    }

    public void setInputOptionQuestion(String id, String question) {
        for (PlayerInputOptions options : this.playerInputOptions) {
            if (options.getName().equalsIgnoreCase(id)) {
                options.setQuestion(question);
                return;
            }
        }

        PlayerInputOptions options = new PlayerInputOptions(id);
        options.setQuestion(question);
        this.playerInputOptions.add(options);
    }

    public void addInputOptionAnswer(String id, String label, String description) {
        for (PlayerInputOptions options : this.playerInputOptions) {
            if (options.getName().equalsIgnoreCase(id)) {
                options.addAnswer(label, description);
                return;
            }
        }
    }

    public void removeInputOptionAnswer(String id, String label) {
        Iterator<PlayerInputOptions> iterator = this.playerInputOptions.iterator();
        while (iterator.hasNext()) {
            PlayerInputOptions options = iterator.next();
            if (options.getName().equalsIgnoreCase(id)) {
                iterator.remove();
                return;
            }
        }
    }

    public boolean containsInputOption(String id) {
        for (PlayerInputOptions option : this.playerInputOptions) {
            if (option.getName().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public PlayerInputOptions getInputOption(String id) {
        for (PlayerInputOptions option : this.playerInputOptions) {
            if (option.getName().equalsIgnoreCase(id)) {
                return option;
            }
        }
        return null;
    }

    public Set<PlayerInputOptions> getInputOptions() {
        return this.playerInputOptions;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        return "ServerSign@" + getLocationString();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\signs\ServerSign.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */