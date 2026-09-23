package app.stral.bukkit.adtrigger.entry;

import app.stral.bukkit.adtrigger.TriggerType;

public class PlayerRespawnEntry extends TimedEntry {

    protected PlayerRespawnEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.RESPAWN;
    }
}
