package app.stral.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;

public class BukkitConfig {

    private final String apiKey;

    public BukkitConfig(FileConfiguration config) {
        this.apiKey = config.getString("api-key", "");
    }

    public String getApiKey() {
        return apiKey;
    }
}
