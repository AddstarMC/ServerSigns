package au.com.addstar.serversigns.itemdata;

import java.io.Serializable;

import org.bukkit.ChatColor;

public class ItemSearchCriteria implements Serializable {
    private boolean ignoreEnchants;
    private boolean ignoreName;
    private boolean ignoreLore;

    public ItemSearchCriteria(boolean ignoreEnchants, boolean ignoreName, boolean ignoreLore) {
        this.ignoreEnchants = ignoreEnchants;
        this.ignoreName = ignoreName;
        this.ignoreLore = ignoreLore;
    }

    public boolean getEnchantsCriteria() {
        return this.ignoreEnchants;
    }

    public boolean getIgnoreName() {
        return this.ignoreName;
    }

    public boolean getIgnoreLore() {
        return this.ignoreLore;
    }

    public boolean getIgnoreDurability() {
        return true;
    }

    public String getColouredString(ChatColor tru, ChatColor fals) {
        return ((this.ignoreEnchants ? tru + "Enchants " : new StringBuilder().append(fals).append("Enchants ").toString()) + (this.ignoreName ? tru + "Name " : new StringBuilder().append(fals).append("Name ").toString()) + (this.ignoreLore ? tru + "Lores " : new StringBuilder().append(fals).append("Lores ").toString())).trim();
    }
}


