package au.com.addstar.serversigns.itemdata;

import org.bukkit.Material;

public class BasicItemData {
    private Material material;
    private short durability;

    public BasicItemData(Material type, short damage) {
        this.material = type;
        this.durability = damage;
    }

    public Material getType() {
        return this.material;
    }

    public short getDurability() {
        return this.durability;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\itemdata\BasicItemData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */