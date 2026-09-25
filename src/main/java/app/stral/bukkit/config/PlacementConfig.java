package app.stral.bukkit.config;

import app.stral.bukkit.nms.Capability;
import app.stral.bukkit.placement.TriggerRegistry;
import app.stral.bukkit.placement.TriggerType;
import app.stral.bukkit.placement.entry.TriggerEntry;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlacementConfig {

    private static final String PLACEMENTS_YML = "placements.yml";

    private final Plugin plugin;
    private final Logger logger;
    private final TriggerRegistry registry;
    private final File file;

    private final Object ioLock = new Object();

    public PlacementConfig(Plugin plugin, TriggerRegistry registry) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.registry = registry;
        this.file = new File(plugin.getDataFolder(), PLACEMENTS_YML);
    }

    public PlacementConfig register() {
        for (TriggerType type : TriggerType.values()) {
            ConfigurationSerialization.registerClass(type.getEntryClass(), type.getDisplay());
        }
        return this;
    }

    public void unregister() {
        for (TriggerType type : TriggerType.values()) {
            ConfigurationSerialization.unregisterClass(type.getEntryClass());
        }
    }

    public void load() {
        final YamlConfiguration yaml;
        TriggerEntry entry;
        int loaded = 0;

        if (!file.exists()) {
            registry.clearAll();
            save();
            return;
        }

        yaml = new YamlConfiguration();
        try {
            yaml.load(file);
            registry.clearAll();
        } catch (IOException | InvalidConfigurationException ex) {
            logger.log(Level.SEVERE,
                    "Could not configure Ad placement(s) from " + PLACEMENTS_YML, ex);
            return;
        }

        for (TriggerType type : TriggerType.values()) {
            registry.setMasterEnabled(
                    type, yaml.getBoolean("master." + type.getDisplay(), false));

            for (Object raw : yaml.getList(
                    "entries." + type.getDisplay(), Collections.emptyList())) {
                if (!type.getEntryClass().isInstance(raw)) {
                    logger.warning(
                            "Skipping invalid Ad placement "
                                    + type.getDisplay() + " defined in " + PLACEMENTS_YML);
                    continue;
                }
                entry = (TriggerEntry) raw;
                if (registry.addEntry(entry)) loaded++;
                else logger.warning(
                        "Duplicate " + type.getDisplay() +
                                " Ad placement with id '" + entry.getId() + "' skipped");
            }
        }
        logger.info("Loaded " + loaded + " Ad placement(s) from " + PLACEMENTS_YML);
    }

    public void save() {
        write(serialize());
    }

    public void saveAsynchronously() {
        final String conf;

        conf = serialize();
        if (!plugin.isEnabled()) {
            write(conf);
            return;
        }
        Capability.SCHEDULER.runOnceAsynchronously(plugin, () -> write(conf));
    }

    private String serialize() {
        final YamlConfiguration yaml;

        yaml = new YamlConfiguration();
        for (TriggerType type : TriggerType.values()) {
            yaml.set("master." + type.getDisplay(), registry.isMasterEnabled(type));
            yaml.set("entries." + type.getDisplay(), new ArrayList<>(registry.getEntries(type)));
        }
        return yaml.saveToString();
    }

    private void write(String data) {
        synchronized (ioLock) {
            final Path target, tmp;
            try {
                target = file.toPath();
                Files.createDirectories(target.getParent());

                tmp = target.resolveSibling(PLACEMENTS_YML + ".tmp");
                Files.write(tmp, data.getBytes(StandardCharsets.UTF_8));
                try {
                    Files.move(tmp, target,
                            StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException ex) {
                    Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Could not save configuration " + PLACEMENTS_YML, ex);
            }
        }
    }
}
