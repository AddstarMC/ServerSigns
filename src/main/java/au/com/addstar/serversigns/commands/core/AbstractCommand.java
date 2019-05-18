package au.com.addstar.serversigns.commands.core;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.translations.Message;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand {
    protected ServerSignsPlugin plugin;
    protected String label;
    protected List<String> args;
    protected Player player;
    protected CommandSender sender;

    protected AbstractCommand(ServerSignsPlugin plugin) {
        this.plugin = plugin;
    }

    public String getLastLabel() {
        return this.label;
    }

    protected String loopArgs(int startIndex) {
        StringBuilder bldr = new StringBuilder();
        for (int i = startIndex; i < this.args.size(); i++) {
            if (i != startIndex) {
                bldr.append(" ");
            }
            bldr.append(this.args.get(i));
        }
        return bldr.toString();
    }

    protected boolean argSet(int idx) {
        return this.args.size() >= idx + 1;
    }

    protected String arg(int idx, String def) {
        if (this.args.size() < idx + 1) {
            return def;
        }
        return this.args.get(idx);
    }

    protected String arg(int idx) {
        return arg(idx, null);
    }

    protected String argStr(int idx, String def) {
        return arg(idx, def);
    }

    protected String argStr(int idx) {
        return arg(idx, null);
    }

    protected Long strAsLong(String str, Long def) {
        if (str == null) {
            return def;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception ignored) {
        }
        return def;
    }

    protected Long argLong(int idx, Long def) {
        return strAsLong(arg(idx), def);
    }

    protected Long argLong(int idx) {
        return argLong(idx, null);
    }

    protected Integer strAsInt(String str, Integer def) {
        if (str == null) {
            return def;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception ignored) {
        }
        return def;
    }

    protected Integer argInt(int idx, Integer def) {
        return strAsInt(arg(idx), def);
    }

    protected Integer argInt(int idx) {
        return argInt(idx, null);
    }

    protected Double strAsDouble(String str, Double def) {
        if (str == null) {
            return def;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception ignored) {
        }
        return def;
    }

    protected Double argDouble(int idx, Double def) {
        return strAsDouble(arg(idx), def);
    }

    protected Double argDouble(int idx) {
        return argDouble(idx, null);
    }

    protected Boolean strAsBool(String str) {
        str = str.toLowerCase();
        return (str.startsWith("y")) || (str.startsWith("t")) || (str.startsWith("on")) || (str.startsWith("+")) || (str.startsWith("1"));
    }

    protected Boolean argBool(int idx, boolean def) {
        String str = arg(idx);
        if (str == null) {
            return def;
        }
        return strAsBool(str);
    }

    protected Boolean argBool(int idx) {
        return argBool(idx, false);
    }

    protected void msg(String message) {
        this.plugin.send(this.sender, message);
    }

    protected void msg(Message message) {
        this.plugin.send(this.sender, message);
    }

    protected void msg(Message message, String... pairedStrings) {
        this.plugin.send(this.sender, message, pairedStrings);
    }

    protected void msg(String[] messages) {
        for (String str : messages) {
            msg(str);
        }
    }

    protected void msg(Collection<String> messages) {
        for (String str : messages) {
            msg(str);
        }
    }

    protected void msg(String message, Object... args) {
        this.plugin.send(this.sender, String.format(message, args));
    }

    protected void msg(String[] messages, Object... args) {
        for (String str : messages) {
            msg(str, args);
        }
    }

    protected void msg(Collection<String> messages, Object... args) {
        for (String str : messages) {
            msg(str, args);
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\core\AbstractCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */