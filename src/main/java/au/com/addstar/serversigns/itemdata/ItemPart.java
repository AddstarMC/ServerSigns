package au.com.addstar.serversigns.itemdata;

public enum ItemPart {
    TYPE(ItemType.get()),
    AMOUNT(ItemAmount.get()),
    ENCHANTS(ItemEnchants.get()),
    NAME(ItemName.get()),
    LORES(ItemLore.get()),
    COLOURS(ItemColour.get());


    private ItemData attached;

    private String[] prefixes;

    ItemPart(ItemData attached) {
        this.attached = attached;
        this.prefixes = attached.getMatcherPrefix();
    }

    public static ItemPart getPartFromPrefix(String input) {
        if (input.length() < 2) {
            return null;
        }
        for (ItemPart part : values()) {
            for (String prefix : part.getPrefixes()) {
                if (prefix.endsWith(".")) {
                    if (input.equalsIgnoreCase(prefix.substring(0, prefix.length() - 1)))
                        return part;
                } else if (input.equalsIgnoreCase(prefix)) {
                    return part;
                }
            }
        }
        return null;
    }

    public ItemData getAttachedData() {
        return this.attached;
    }

    public String[] getPrefixes() {
        return this.prefixes;
    }
}


