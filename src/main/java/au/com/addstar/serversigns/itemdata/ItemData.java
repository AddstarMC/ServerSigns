package au.com.addstar.serversigns.itemdata;

public abstract class ItemData implements IItemData {
    private ItemPart key;
    private String[] matchPrefixes;
    private boolean applyToMeta = false;


    public ItemData(ItemPart key, String[] matchPrefixes) {
        this.key = key;
        this.matchPrefixes = matchPrefixes;
    }

    public ItemPart getDataKey() {
        return this.key;
    }

    public String[] getMatcherPrefix() {
        return this.matchPrefixes;
    }

    public boolean canApplyToMeta() {
        return this.applyToMeta;
    }

    protected void setApplyToMeta(boolean newVal) {
        this.applyToMeta = newVal;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\itemdata\ItemData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */