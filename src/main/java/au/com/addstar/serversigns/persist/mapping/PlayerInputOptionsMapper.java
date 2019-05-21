package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.signs.PlayerInputOptions;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.MemorySection;

public class PlayerInputOptionsMapper implements IPersistenceMapper<Set<PlayerInputOptions>> {
    private MemorySection memorySection;

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public Set<PlayerInputOptions> getValue(String path) {
        if (!this.memorySection.contains(path)) {
            return null;
        }
        Set<PlayerInputOptions> optionSet = new HashSet<>();
        for (String optionSetName : this.memorySection.getConfigurationSection(path).getKeys(false)) {
            PlayerInputOptions options = new PlayerInputOptions(optionSetName);
            options.setQuestion(this.memorySection.getString(path + "." + optionSetName + ".question"));
            if (this.memorySection.contains(path + "." + optionSetName + ".answers")) {
                for (String answerLabel : this.memorySection.getConfigurationSection(path + "." + optionSetName + ".answers").getKeys(false)) {
                    options.addAnswer(answerLabel, this.memorySection.getString(path + "." + optionSetName + ".answers." + answerLabel));
                }
            }
            optionSet.add(options);
        }
        return optionSet;
    }

    public void setValue(String path, Set<PlayerInputOptions> values) {
        for (PlayerInputOptions value : values) {
            this.memorySection.set(path + "." + value.getName() + ".question", value.getQuestion());
            for (int k = 0; k < value.getAnswersLength(); k++) {
                this.memorySection.set(path + "." + value.getName() + ".answers." + value.getAnswerLabel(k), value.getAnswerDescription(k));
            }
        }
    }
}


