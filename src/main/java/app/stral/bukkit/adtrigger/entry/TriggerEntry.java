package app.stral.bukkit.adtrigger.entry;

import app.stral.bukkit.adtrigger.TriggerType;
import org.jspecify.annotations.NonNull;

public abstract class TriggerEntry {

    private final String id;

    private boolean enabled = false;
    private String promptContent, rewardCommand = "";

    protected TriggerEntry(String id) {
        this.id = id;
    }

    public abstract TriggerType getType();

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPromptContent() {
        return promptContent;
    }

    public void setPromptContent(@NonNull String promptContent) {
        this.promptContent = promptContent;
    }

    public String getRewardCommand() {
        return rewardCommand;
    }

    public void setRewardCommand(@NonNull String rewardCommand) {
        this.rewardCommand = rewardCommand;
    }
}
