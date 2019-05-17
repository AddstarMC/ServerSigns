package au.com.addstar.serversigns.itemdata;

import java.io.Serializable;

import org.bukkit.ChatColor;

public class ItemSearchCriteria implements Serializable {
    private boolean ignoreEnchants;
    private boolean ignoreName;
    private boolean ignoreLore;
    private boolean ignoreDurability;

    public ItemSearchCriteria(boolean ignoreEnchants, boolean ignoreName, boolean ignoreLore, boolean ignoreDurability) {
        this.ignoreEnchants = ignoreEnchants;
        this.ignoreName = ignoreName;
        this.ignoreLore = ignoreLore;
        this.ignoreDurability = ignoreDurability;
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
        return this.ignoreDurability;
    }

    public String getColouredString(ChatColor tru, ChatColor fals) {
        return ((this.ignoreEnchants ? tru + "Enchants " : new StringBuilder().append(fals).append("Enchants ").toString()) + (this.ignoreName ? tru + "Name " : new StringBuilder().append(fals).append("Name ").toString()) + (this.ignoreLore ? tru + "Lores " : new StringBuilder().append(fals).append("Lores ").toString()) + (this.ignoreDurability ? tru + "Durability " : new StringBuilder().append(fals).append("Durability ").toString())).trim();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\itemdata\ItemSearchCriteria.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */