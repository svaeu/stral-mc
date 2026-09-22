package app.stral.core.config;

import org.bukkit.configuration.file.FileConfiguration;

public class BukkitConfig {

    private final String apiKey;

    public BukkitConfig(FileConfiguration config) {
        this.apiKey = config.getString("api-key", null);
    }

    public String getApiKey() {
        return apiKey;
    }
}
