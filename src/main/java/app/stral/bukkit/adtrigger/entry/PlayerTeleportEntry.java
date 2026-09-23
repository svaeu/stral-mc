package app.stral.bukkit.adtrigger.entry;

import app.stral.bukkit.adtrigger.TriggerType;

public class PlayerTeleportEntry extends TimedEntry {

    protected PlayerTeleportEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.TELEPORT;
    }
}
