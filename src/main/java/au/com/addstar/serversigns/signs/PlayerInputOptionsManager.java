package au.com.addstar.serversigns.signs;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.translations.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerInputOptionsManager implements org.bukkit.event.Listener {
    private Map<UUID, List<String>> pendingOptionDisplays = new HashMap();
    private Map<UUID, Data> pendingPlayerData = new HashMap();

    private Map<UUID, Map<String, String>> completedAnswers = new HashMap();
    private ServerSignsPlugin plugin;

    public PlayerInputOptionsManager(ServerSignsPlugin plugin) {
        this.plugin = plugin;
    }

    public void suspend(Player player, List<String> optionDisplayNames, ServerSign sign) {
        if (optionDisplayNames.size() < 1) {
            return;
        }
        askQuestion(player, sign.getInputOption(optionDisplayNames.get(0)));
        this.pendingOptionDisplays.put(player.getUniqueId(), optionDisplayNames);
        this.pendingPlayerData.put(player.getUniqueId(), new Data(sign, optionDisplayNames));
    }

    public boolean isSuspended(Player player) {
        return this.pendingOptionDisplays.containsKey(player.getUniqueId());
    }

    public boolean hasCompletedAnswers(Player player) {
        return this.completedAnswers.containsKey(player.getUniqueId());
    }

    public Map<String, String> getCompletedAnswers(Player player, boolean removeOnReturn) {
        Map<String, String> answers = this.completedAnswers.get(player.getUniqueId());
        if (removeOnReturn) {
            this.completedAnswers.remove(player.getUniqueId());
        }
        return answers;
    }

    public void processAnswer(Player player, String answer) {
        if (!this.pendingPlayerData.containsKey(player.getUniqueId())) {
            return;
        }

        List<String> pendingToDisplay = this.pendingOptionDisplays.get(player.getUniqueId());
        Data playerData = this.pendingPlayerData.get(player.getUniqueId());
        PlayerInputOptions options = playerData.sign.getInputOption(pendingToDisplay.get(0));

        if (!options.isValidAnswerLabel(answer)) {
            this.plugin.send(player, Message.OPTION_INVALID_ANSWER);
            askQuestion(player, options);
            return;
        }

        playerData.answers.add(answer);
        pendingToDisplay.remove(0);
        this.plugin.send(player, "&7&oOK!");

        if (pendingToDisplay.size() > 0) {
            askQuestion(player, playerData.sign.getInputOption(pendingToDisplay.get(0)));
        } else {
            release(player, true);
        }
    }

    private void askQuestion(Player player, PlayerInputOptions inputOptions) {
        if (inputOptions == null) {
            ServerSignsPlugin.log("Player " + player.getName() + " has encountered an incorrectly labelled \"/svs option\" question! " + "The player's current location is: " + player.getLocation().toString(), java.util.logging.Level.SEVERE);

            this.plugin.send(player, "This ServerSign has been setup incorrectly, please alert an Administrator!");
            release(player, false);
            return;
        }

        this.plugin.send(player, inputOptions.getQuestion());
        for (int k = 0; k < inputOptions.getAnswersLength(); k++) {
            this.plugin.send(player, inputOptions.getAnswerLabel(k) + this.plugin.msgHandler.get(Message.OPTION_LABEL_DESC_SEPARATOR) + inputOptions.getAnswerDescription(k));
        }
    }

    public void release(Player player, boolean continueProcessing) {
        this.pendingOptionDisplays.remove(player.getUniqueId());

        if (continueProcessing) {
            Data data = this.pendingPlayerData.get(player.getUniqueId());
            if (data != null) {
                Map<String, String> map = new HashMap();
                for (int k = 0; k < data.originalQuestionIds.size(); k++) {
                    map.put(data.originalQuestionIds.get(k), data.answers.get(k));
                }
                this.completedAnswers.put(player.getUniqueId(), map);
                this.plugin.serverSignExecutor.executeSignFull(player, data.sign, null);

                if (this.pendingOptionDisplays.containsKey(player.getUniqueId())) {
                    Data newData = this.pendingPlayerData.get(player.getUniqueId());
                    data.originalQuestionIds.addAll(newData.originalQuestionIds);
                    this.pendingPlayerData.put(player.getUniqueId(), data);
                } else {
                    this.pendingPlayerData.remove(player.getUniqueId());
                }
            }
        }
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String firstWord = event.getMessage().contains(" ") ? event.getMessage().split(" ")[0] : event.getMessage();

        if (isSuspended(player)) {
            if (firstWord.equalsIgnoreCase("exit")) {
                this.plugin.send(player, "Exiting...");
                release(player, false);
            } else {
                processAnswer(player, firstWord);
            }
            event.setCancelled(true);
        }
    }

    private class Data {

        public ServerSign sign;
        public List<String> originalQuestionIds;
        public List<String> answers = new ArrayList<>();

        public Data(ServerSign sign, List<String> originalQuestions) {
            this.sign = sign;
            this.originalQuestionIds = new ArrayList<>();
            this.originalQuestionIds.addAll(originalQuestions);
        }
    }

}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\signs\PlayerInputOptionsManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */