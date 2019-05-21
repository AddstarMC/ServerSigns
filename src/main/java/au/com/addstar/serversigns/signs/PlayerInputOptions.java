package au.com.addstar.serversigns.signs;

import java.util.Arrays;


public class PlayerInputOptions {
    String name;
    String question = "";
    String[] answerLabels = new String[0];
    String[] answerDescriptions = new String[0];

    public PlayerInputOptions(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isValidAnswerLabel(String label) {
        for (String str : this.answerLabels) {
            if (str.equalsIgnoreCase(label)) {
                return true;
            }
        }

        return false;
    }

    public String getAnswerLabel(int index) {
        if ((this.answerLabels.length > index) && (index > -1)) {
            return this.answerLabels[index];
        }
        return null;
    }

    public void setAnswerLabel(int index, String newValue) {
        if ((this.answerLabels.length > index) && (index > -1)) {
            this.answerLabels[index] = newValue;
        }
    }

    public int getAnswersLength() {
        return this.answerLabels.length;
    }

    public String getAnswerDescription(int index) {
        if ((this.answerDescriptions.length > index) && (index > -1)) {
            return this.answerDescriptions[index];
        }
        return null;
    }

    public void setAnswerDescription(int index, String newValue) {
        if ((this.answerDescriptions.length > index) && (index > -1)) {
            this.answerDescriptions[index] = newValue;
        }
    }

    public void addAnswer(String label, String description) {
        this.answerLabels = Arrays.copyOf(this.answerLabels, this.answerLabels.length + 1);
        this.answerDescriptions = Arrays.copyOf(this.answerDescriptions, this.answerDescriptions.length + 1);

        this.answerLabels[(this.answerLabels.length - 1)] = label;
        this.answerDescriptions[(this.answerDescriptions.length - 1)] = description;
    }
}


