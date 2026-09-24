package app.stral.bukkit.placement.entry;

import app.stral.bukkit.placement.TriggerType;

import java.util.Map;

public class PlayerJoinEntry extends TimedEntry {

    public PlayerJoinEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.JOIN;
    }

    public static PlayerJoinEntry deserialize(Map<String, Object> data) {
        final PlayerJoinEntry joinEntry;

        joinEntry = new PlayerJoinEntry(requireId(data));
        joinEntry.load(data);
        return joinEntry;
    }
}
