package app.stral.bukkit.placement.entry;

import app.stral.bukkit.placement.TriggerType;

import java.util.Map;

public class PlayerTeleportEntry extends TimedEntry {

    public PlayerTeleportEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.TELEPORT;
    }

    public static PlayerTeleportEntry deserialize(Map<String, Object> data) {
        final PlayerTeleportEntry teleportEntry;

        teleportEntry = new PlayerTeleportEntry(requireId(data));
        teleportEntry.load(data);
        return teleportEntry;
    }
}
