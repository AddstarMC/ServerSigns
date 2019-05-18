package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandSetPrice extends SubCommand {
    public SubCommandSetPrice(ServerSignsPlugin plugin) {
        super(plugin, "set_price", "setprice <cost>", "Add a monetary cost to an existing ServerSign", "spr", "setprice", "priceset", "price");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        if (argDouble(0, -1.0D) < 0.0D) {
            if (verbose) sendUsage();
            return;
        }

        applyMeta(SVSMetaKey.PRICE, new SVSMetaValue(argDouble(0)));
        if (verbose) msg(Message.RIGHT_CLICK_SET_PRICE);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandSetPrice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */