package app.stral.bukkit.adtrigger.entry;

import app.stral.bukkit.adtrigger.TriggerType;

public class PlayerJoinEntry extends TimedEntry {

    protected PlayerJoinEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.JOIN;
    }
}
