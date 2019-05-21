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


