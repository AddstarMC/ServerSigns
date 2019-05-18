package au.com.addstar.serversigns.legacy;

import au.com.addstar.serversigns.config.IServerSignsConfig;
import au.com.addstar.serversigns.persist.mapping.BlocksIdMapper;
import au.com.addstar.serversigns.persist.PersistenceEntry;
import au.com.addstar.serversigns.persist.mapping.ColouredStringMapper;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.Material;

public class OldServerSignsConfig implements IServerSignsConfig {
    @PersistenceEntry(configMapper = BlocksIdMapper.class)
    private EnumSet<Material> blocks = EnumSet.of(Material.OAK_WALL_SIGN, Material.OAK_SIGN);

    @PersistenceEntry
    private String language = "en";

    @PersistenceEntry(configMapper = ColouredStringMapper.class)
    private String messageTag = "&2[ServerSigns]";
    @PersistenceEntry(configMapper = ColouredStringMapper.class)
    private String messageColour = "&e";

    @PersistenceEntry
    private Boolean vault_grant = Boolean.TRUE;

    @PersistenceEntry
    private String permission_console_command_add = "pex user <player> add <permission>";
    @PersistenceEntry
    private String permission_console_command_remove = "pex user <player> remove <permission>";

    @PersistenceEntry
    private Boolean must_be_sneaking_to_destroy = Boolean.TRUE;

    @PersistenceEntry
    private Boolean show_funds_removed_message = Boolean.TRUE;

    @PersistenceEntry
    private String currency = "dollars";

    @PersistenceEntry
    private Boolean broadcastDevelopers = Boolean.TRUE;

    @PersistenceEntry
    private Boolean automaticUpdateChecks = Boolean.TRUE;

    @PersistenceEntry
    private Boolean disableConsoleCommandLogging = Boolean.FALSE;

    @PersistenceEntry
    private Boolean allowLeftClicking = Boolean.FALSE;

    @PersistenceEntry
    private Boolean send_payments_to_bank = Boolean.FALSE;
    @PersistenceEntry
    private String deposit_bank_name = "server";

    @PersistenceEntry
    private Boolean alternate_command_dispatching = Boolean.FALSE;

    @PersistenceEntry(configPath = "blocked-commands")
    private List<String> blockedCommands = Arrays.asList("op", "deop", "stop");


    public EnumSet<Material> getBlocks() {
        return this.blocks;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getMessageTag() {
        return this.messageTag;
    }

    public String getMessageColour() {
        return this.messageColour;
    }

    public Boolean getVaultGrant() {
        return this.vault_grant;
    }

    public String getPermissionConsoleCommandAdd() {
        return this.permission_console_command_add;
    }

    public String getPermissionConsoleCommandRemove() {
        return this.permission_console_command_remove;
    }

    public Boolean getMustBeSneakingToDestroy() {
        return this.must_be_sneaking_to_destroy;
    }

    public Boolean getShowFundsRemovedMessage() {
        return this.show_funds_removed_message;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Boolean getBroadcastDevelopers() {
        return this.broadcastDevelopers;
    }

    public Boolean getAutomaticUpdateChecks() {
        return this.automaticUpdateChecks;
    }

    public Boolean getDisableConsoleCommandLogging() {
        return this.disableConsoleCommandLogging;
    }

    public Boolean getAllowLeftClicking() {
        return this.allowLeftClicking;
    }

    public Boolean getSendPaymentsToBank() {
        return this.send_payments_to_bank;
    }

    public String getDepositBankName() {
        return this.deposit_bank_name;
    }

    public Boolean getAlternateCommandDispatching() {
        return this.alternate_command_dispatching;
    }

    public List<String> getBlockedCommands() {
        return this.blockedCommands;
    }

    public int getVersion() {
        return 0;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\legacy\OldServerSignsConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */