package au.com.addstar.serversigns.meta;

import au.com.addstar.serversigns.parsing.command.ServerSignCommand;
import au.com.addstar.serversigns.signs.ServerSign;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class SVSMetaValue {
    private Object object;

    public SVSMetaValue(Object value) {
        this.object = value;
    }

    public long asLong() {
        if ((this.object instanceof Long)) {
            return (Long) this.object;
        }

        return Long.MIN_VALUE;
    }

    public int asInt() {
        if ((this.object instanceof Integer)) {
            return (Integer) this.object;
        }

        return Integer.MIN_VALUE;
    }

    public double asDouble() {
        if ((this.object instanceof Double)) {
            return (Double) this.object;
        }

        return Double.MIN_VALUE;
    }

    public String asString() {
        if ((this.object instanceof String)) {
            return (String) this.object;
        }

        return "";
    }

    public boolean asBoolean() {
        if ((this.object instanceof Boolean)) {
            return (Boolean) this.object;
        }

        return false;
    }

    public Object asObject() {
        return this.object;
    }

    public org.bukkit.Location asLocation() {
        if ((this.object instanceof org.bukkit.Location)) {
            return (org.bukkit.Location) this.object;
        }

        return null;
    }

    public ServerSign asServerSign() {
        if ((this.object instanceof ServerSign)) {
            return (ServerSign) this.object;
        }

        return null;
    }

    public ItemStack asItemStack() {
        if ((this.object instanceof ItemStack)) {
            return (ItemStack) this.object;
        }

        return null;
    }

    public ServerSignCommand asServerSignCommand() {
        if ((this.object instanceof ServerSignCommand)) {
            return (ServerSignCommand) this.object;
        }

        return null;
    }

    public List<String> asStringList() {
        if ((this.object instanceof List)) {
            return (List) this.object;
        }

        return null;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\meta\SVSMetaValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */