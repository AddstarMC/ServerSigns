package au.com.addstar.serversigns.config;

import au.com.addstar.serversigns.persist.mapping.BlocksMapper;
import au.com.addstar.serversigns.legacy.OldServerSignsConfig;
import au.com.addstar.serversigns.persist.PersistenceEntry;
import au.com.addstar.serversigns.persist.mapping.ColouredStringMapper;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ServerSignsConfig implements IServerSignsConfig {
    @PersistenceEntry(configMapper = BlocksMapper.class, comments = {"# A list of material names (should be in the Bukkit/Spigot Material enum form)", "# These materials define the blocks which can be used with ServerSigns", "# Refer to this page for the list: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html"})
    private EnumSet<Material> blocks = EnumSet.of(Material.WALL_SIGN, Material.SIGN_POST);


    @PersistenceEntry(comments = {"# Whether or not any block can be used with ServerSigns (overrides 'blocks' list)"})
    private boolean any_block = false;

    @PersistenceEntry(comments = {"# The language key which should be used to find the languages file", "# For example, 'en' will resolve to the 'ServerSigns/translations/en.yml' file"})
    private String language = "en_default";


    @PersistenceEntry(configMapper = ColouredStringMapper.class, comments = {"# The prefix used in most ServerSigns messages"})
    private String message_prefix = "&2[ServerSigns]";
    @PersistenceEntry(configMapper = ColouredStringMapper.class, comments = {"# The colour for most ServerSigns messages"})
    private String message_colour = "&e";

    @PersistenceEntry(comments = {"# Whether or not Vault should be used for permissions granting"})
    private boolean vault_grant = true;

    @PersistenceEntry(comments = {"# The command to execute from console for setting permissions without Vault"})
    private String permission_add_command = "pex user <player> add <permission>";
    @PersistenceEntry(comments = {"# The command to execute from console for removing permissions without Vault"})
    private String permission_remove_command = "pex user <player> remove <permission>";

    @PersistenceEntry(comments = {"# Whether or not admins must be sneaking to destroy ServerSigns"})
    private boolean sneak_to_destroy = true;

    @PersistenceEntry(comments = {"# Whether or not to display a message to players when funds are removed from account"})
    private boolean show_funds_removed_message = true;

    @PersistenceEntry(comments = {"# The currency string or symbol for use in messages"})
    private String currency_string = "dollars";

    @PersistenceEntry(comments = {"# Whether or not the plugin should announce ServerSigns developers joining your server"})
    private boolean broadcast_developers = true;

    @PersistenceEntry(comments = {"# Whether or not the plugin should automatically check for the latest version and download it"})
    private boolean check_for_updates = true;

    @PersistenceEntry(comments = {"# Whether command logging to the console should be disabled"})
    private boolean disable_command_logging = false;

    @PersistenceEntry(comments = {"# Whether the plugin should listen for left clicks as well as right clicks for ServerSign activation", "# This option must be enabled to allow ServerSigns to execute different commands for left & right clicks"})
    private boolean allow_left_clicking = true;


    @PersistenceEntry(comments = {"# Whether funds remove via /svs setprice should be sent to a server bank"})
    private boolean send_payments_to_bank = false;
    @PersistenceEntry(comments = {"# The server bank name (used if send_payments_to_bank is set to true)"})
    private String deposit_bank_name = "server";

    @PersistenceEntry(comments = {"# Whether the Player#chat() function should be used instead of Player#performCommand() for commands", "# If enabled, this means commands executed through ServerSigns will fire the command pre-process event"})
    private boolean alternate_command_dispatching = false;


    @PersistenceEntry(comments = {"# Whether or not you want to opt-out of Metrics statistic gathering through www.mcstats.org"})
    private boolean metrics_opt_out = false;

    @PersistenceEntry(comments = {"# A list of commands which cannot be attached to ServerSigns (to prevent console-only command exploits)"})
    private List<String> blocked_commands = Arrays.asList("op", "deop", "stop");

    @PersistenceEntry(comments = {"# Defines the task delay threshold (in seconds) above which tasks will be persisted to disk"})
    private long task_persist_threshold = 10L;

    @PersistenceEntry(comments = {"# Whether or not you want to have tasks that match the defined regex pattern cancelled on player death", "# Please note that in servers with over 10,000 queued tasks, the regex search can affect performance"})
    private boolean cancel_tasks_on_death = false;


    @PersistenceEntry(comments = {"# The Regular Expression pattern used when determining which tasks to cancel upon a player's death", "# This Regex pattern will be used to compare each pending command a player has on their death; matching commands will be cancelled"})
    private String cancel_tasks_regex_pattern = "warp .*";


    @PersistenceEntry(comments = {"# The number of hours your timezone is offset from GMT/UTC - must be an integer between -12 and 12"})
    private int time_zone_offset = 0;
    private transient TimeZone timeZone;
    private transient Pattern compiledCancelTaskPattern;

    public EnumSet<Material> getBlocks() {
        return this.blocks;
    }

    public boolean getAnyBlock() {
        return this.any_block;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getMessagePrefix() {
        return this.message_prefix;
    }

    public String getMessageColour() {
        return this.message_colour;
    }

    public void colourise() {
        this.message_prefix = ChatColor.translateAlternateColorCodes('&', this.message_prefix);
        this.message_colour = ChatColor.translateAlternateColorCodes('&', this.message_colour);
    }

    public boolean getVaultGrant() {
        return this.vault_grant;
    }

    public String getPermissionAddCommand() {
        return this.permission_add_command;
    }

    public String getPermissionRemoveCommand() {
        return this.permission_remove_command;
    }

    public boolean getSneakToDestroy() {
        return this.sneak_to_destroy;
    }

    public boolean getShowFundsRemovedMessage() {
        return this.show_funds_removed_message;
    }

    public String getCurrencyString() {
        return this.currency_string;
    }

    public boolean getBroadcastDevelopers() {
        return this.broadcast_developers;
    }

    public boolean getCheckForUpdates() {
        return this.check_for_updates;
    }

    public boolean getDisableCommandLogging() {
        return this.disable_command_logging;
    }

    public boolean getAllowLeftClicking() {
        return this.allow_left_clicking;
    }

    public boolean getSendPaymentsToBank() {
        return this.send_payments_to_bank;
    }

    public String getDepositBankName() {
        return this.deposit_bank_name;
    }

    public boolean getAlternateCommandDispatching() {
        return this.alternate_command_dispatching;
    }

    public boolean getMetricsOptOut() {
        return this.metrics_opt_out;
    }

    public List<String> getBlockedCommands() {
        return this.blocked_commands;
    }

    public long getTaskPersistThreshold() {
        return this.task_persist_threshold;
    }

    public boolean getCancelTasksOnDeath() {
        return this.cancel_tasks_on_death;
    }

    public int getTimeZoneOffset() {
        return this.time_zone_offset;
    }


    public TimeZone getTimeZone() {
        return this.timeZone == null ? (this.timeZone = TimeZone.getTimeZone("GMT" + (getTimeZoneOffset() > -1 ? "+" + getTimeZoneOffset() : Integer.valueOf(getTimeZoneOffset())))) : this.timeZone;
    }


    public Pattern getCompiledCancelTaskPattern() {
        return this.compiledCancelTaskPattern;
    }

    public void compilePatterns() throws PatternSyntaxException {
        if (this.cancel_tasks_regex_pattern.isEmpty()) return;
        this.compiledCancelTaskPattern = Pattern.compile(this.cancel_tasks_regex_pattern);
    }

    public void findTimeZone() {
        this.time_zone_offset = ((int) TimeUnit.MILLISECONDS.toHours(TimeZone.getDefault().getRawOffset()));
    }

    public void setFromOldConfig(OldServerSignsConfig oldConfig) {
        this.blocks = oldConfig.getBlocks();
        this.language = oldConfig.getLanguage();
        this.message_prefix = oldConfig.getMessageTag();
        this.message_colour = oldConfig.getMessageColour();
        this.vault_grant = oldConfig.getVaultGrant().booleanValue();
        this.permission_add_command = oldConfig.getPermissionConsoleCommandAdd();
        this.permission_remove_command = oldConfig.getPermissionConsoleCommandRemove();
        this.sneak_to_destroy = oldConfig.getMustBeSneakingToDestroy().booleanValue();
        this.show_funds_removed_message = oldConfig.getShowFundsRemovedMessage().booleanValue();
        this.currency_string = oldConfig.getCurrency();
        this.broadcast_developers = oldConfig.getBroadcastDevelopers().booleanValue();
        this.check_for_updates = oldConfig.getAutomaticUpdateChecks().booleanValue();
        this.disable_command_logging = oldConfig.getDisableConsoleCommandLogging().booleanValue();
        this.allow_left_clicking = oldConfig.getAllowLeftClicking().booleanValue();
        this.send_payments_to_bank = oldConfig.getSendPaymentsToBank().booleanValue();
        this.deposit_bank_name = oldConfig.getDepositBankName();
        this.alternate_command_dispatching = oldConfig.getAlternateCommandDispatching().booleanValue();
        this.blocked_commands = oldConfig.getBlockedCommands();
        colourise();
    }

    public int getVersion() {
        return 3;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\config\ServerSignsConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */