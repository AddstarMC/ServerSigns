package au.com.addstar.serversigns.itemdata;

public enum ItemPart {
    TYPE(ItemType.get()),
    AMOUNT(ItemAmount.get()),
    DAMAGE(ItemDamage.get()),
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


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\itemdata\ItemPart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */