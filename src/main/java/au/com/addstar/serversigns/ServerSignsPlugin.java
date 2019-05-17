package au.com.addstar.serversigns;

import au.com.addstar.serversigns.commands.CommandServerSigns;
import au.com.addstar.serversigns.commands.CommandServerSignsRemote;
import au.com.addstar.serversigns.commands.core.Command;
import au.com.addstar.serversigns.commands.core.CommandException;
import au.com.addstar.serversigns.config.ConfigLoader;
import au.com.addstar.serversigns.config.ConfigLoadingException;
import au.com.addstar.serversigns.config.ServerSignsConfig;
import au.com.addstar.serversigns.hooks.HookManager;
import au.com.addstar.serversigns.listeners.AdminListener;
import au.com.addstar.serversigns.listeners.BlockListener;
import au.com.addstar.serversigns.listeners.PlayerListener;
import au.com.addstar.serversigns.signs.PlayerInputOptionsManager;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.signs.ServerSignExecutor;
import au.com.addstar.serversigns.signs.ServerSignManager;
import au.com.addstar.serversigns.taskmanager.TaskManager;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.translations.MessageHandler;
import au.com.addstar.serversigns.translations.NoDefaultException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerSignsPlugin extends JavaPlugin {
    public static final Random r = new Random();
    private static Logger logger;
    public PluginManager pm;
    public PlayerListener playerListener = new PlayerListener(this);
    public BlockListener blockListener = new BlockListener(this);
    public AdminListener adminListener = new AdminListener(this);
    public ServerSignsConfig config;
    public MessageHandler msgHandler;
    public ServerSignManager serverSignsManager;
    public ServerSignExecutor serverSignExecutor;
    public TaskManager taskManager;
    public HookManager hookManager;
    public PlayerInputOptionsManager inputOptionsManager;

    public static void log(String log) {
        log(log, Level.INFO, null);
    }

    public static void log(String log, Level level) {
        log(log, level, null);
    }

    public static void log(String log, Level level, Throwable thrown) {
        logger.log(level, log, thrown);
    }

    public void onEnable() {
        try {
            logger = getLogger();
            Path dataFolder = Files.createDirectories(getDataFolder().toPath());
            loadConfig(dataFolder);

            this.taskManager = new TaskManager(this, dataFolder);
            this.taskManager.init();

            this.serverSignsManager = new ServerSignManager(this);
            final Set<ServerSign> preparedSigns = this.serverSignsManager.prepareServerSignsSet();

            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                    ServerSignsPlugin.this.taskManager.start();
                    ServerSignsPlugin.this.serverSignsManager.populateSignsMap(preparedSigns);
                }
            }, 1L);


            this.serverSignExecutor = new ServerSignExecutor(this);
            this.inputOptionsManager = new PlayerInputOptionsManager(this);

            this.hookManager = new HookManager(this);
            this.hookManager.tryInstantiateHooks(false);
            this.pm = getServer().getPluginManager();

            this.pm.registerEvents(this.adminListener, this);
            this.pm.registerEvents(this.playerListener, this);
            this.pm.registerEvents(this.blockListener, this);
            this.pm.registerEvents(this.inputOptionsManager, this);

            log("Version " + getDescription().getVersion() + " is now enabled.");
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "Error while enabling " + getDescription().getFullName() + ". Disabling...", ex);
            setEnabled(false);
        }
    }

    public void onDisable() {
        if (this.taskManager != null) {
            this.taskManager.stop();
        }
        log(getDescription().getName() + " is now disabled.");
    }

    public void loadConfig(Path dataFolder) throws ConfigLoadingException, NoDefaultException {
        this.config = ConfigLoader.loadConfig(dataFolder.resolve("config.yml"));

        this.msgHandler = new MessageHandler(this);
        this.msgHandler.setCurrentTranslation(this.config.getLanguage());
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String commandLabel, String[] args) {
        try {
            Command cmd;
            if (command.getName().equalsIgnoreCase("serversigns")) {
                cmd = new CommandServerSigns(this);
            } else if (command.getName().equalsIgnoreCase("serversignsremote")) {
                cmd = new CommandServerSignsRemote(this);
            } else
                return false;
            try {
                cmd.run(getServer(), sender, commandLabel, command, args);
                return true;
            } catch (CommandException ex) {
                sender.sendMessage(ex.getMessage());
                return true;
            } catch (Exception ex) {
                send(sender, "Error: An internal error has occurred. If unexpected, please report this at http://dev.bukkit.org/server-mods/serversigns !");
                ex.printStackTrace();
                return true;
            }
        } catch (Throwable ex) {
            getLogger().log(Level.SEVERE, String.format("Failed to execute command '%s' [executor: %s]. Stack trace as follows:", commandLabel, sender.getName()), ex);
            return false;
        }
    }

    public void serverCommand(String command) {
        if (!this.config.getDisableCommandLogging()) {
            log("Executing command: " + command);
        }
        getServer().dispatchCommand(getServer().getConsoleSender(), command);
    }

    public void send(CommandSender sender, String message) {
        if (message.isEmpty()) return;
        sender.sendMessage((this.config.getMessagePrefix().isEmpty() ? "" : new StringBuilder().append(ChatColor.DARK_GREEN).append(this.config.getMessagePrefix()).append(" ").toString()) + ChatColor.YELLOW + this.config.getMessageColour() + ChatColor.translateAlternateColorCodes('&', message));
    }

    public void send(CommandSender to, Collection<String> messages) {
        for (String str : messages)
            send(to, str);
    }

    public void send(CommandSender to, Message message) {
        send(to, this.msgHandler.get(message));
    }

    public void send(CommandSender to, Message message, String... pairedStrings) {
        String msg = this.msgHandler.get(message);
        String buf = "";
        for (String str : pairedStrings) {
            if (buf.isEmpty()) {
                buf = str;
            } else {
                msg = msg.replaceAll(buf, Matcher.quoteReplacement(str));
                buf = "";
            }
        }
        send(to, msg);
    }

    public void sendBlank(CommandSender to, String message) {
        to.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public ServerSignsConfig getServerSignsConfig() {
        return this.config;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\ServerSignsPlugin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */