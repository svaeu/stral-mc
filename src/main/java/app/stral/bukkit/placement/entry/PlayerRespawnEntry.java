package app.stral.bukkit.placement.entry;

import app.stral.bukkit.placement.TriggerType;

import java.util.Map;

public class PlayerRespawnEntry extends TimedEntry {

    public PlayerRespawnEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.RESPAWN;
    }

    public static PlayerRespawnEntry deserialize(Map<String, Object> data) {
        final PlayerRespawnEntry respawnEntry;

        respawnEntry = new PlayerRespawnEntry(requireId(data));
        respawnEntry.load(data);
        return respawnEntry;
    }
}
