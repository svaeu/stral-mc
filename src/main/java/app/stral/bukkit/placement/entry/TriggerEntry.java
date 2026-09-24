package app.stral.bukkit.placement.entry;

import app.stral.bukkit.placement.TriggerType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class TriggerEntry implements ConfigurationSerializable {

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

    @Override
    public @NonNull Map<String, Object> serialize() {
        final Map<String, Object> baseData;

        baseData = new LinkedHashMap<>();
        baseData.put("id", id);
        baseData.put("enabled", enabled);
        baseData.put("prompt-content", promptContent);
        baseData.put("reward-command", rewardCommand);
        return baseData;
    }

    protected void load(@NonNull Map<String, Object> data) {
        setEnabled(readBoolean(data, "enabled", false));
        setPromptContent(readString(data, "prompt-content", ""));
        setRewardCommand(readString(data, "reward-command", ""));
    }

    protected static String requireId(Map<String, Object> data) {
        final String id;

        id = readString(data, "id", "");
        if (id.trim().isEmpty())
            throw new IllegalArgumentException("Trigger entry is missing an 'id'");

        return id;
    }

    protected static String readString(Map<String, Object> data, String key, String def) {
        final Object value;

        value = data.get(key);
        return value == null ? def : value.toString();
    }

    protected static boolean readBoolean(Map<String, Object> data, String key, boolean def) {
        final Object value;

        value = data.get(key);
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof String) return Boolean.parseBoolean(((String) value).trim());
        return def;
    }

    protected static long readLong(Map<String, Object> data, String key, long def) {
        final Object value;

        value = data.get(key);
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong(((String) value).trim());
            } catch (NumberFormatException ignored) {}
        }
        return def;
    }

    protected static int readInt(Map<String, Object> data, String key, int def) {
        return (int) readLong(data, key, def);
    }
}
