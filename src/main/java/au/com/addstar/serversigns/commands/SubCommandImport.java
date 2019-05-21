package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandImport extends SubCommand {
    public SubCommandImport(ServerSignsPlugin plugin) {
        super(plugin, "import", "import <path to file>", "Import a text file of commands, 1 command per line without /svs", "import", "imp");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        String path = loopArgs(0);
        if (!java.nio.file.Files.exists(java.nio.file.Paths.get(path))) {
            if (verbose) msg(Message.IMPORT_FILE_NOT_FOUND);
            return;
        }

        if (verbose) msg(Message.IMPORT_SELECT_SIGN);
        applyMeta(SVSMetaKey.IMPORT, new SVSMetaValue(path));
    }
}


