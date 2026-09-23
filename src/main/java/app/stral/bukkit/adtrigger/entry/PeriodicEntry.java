package app.stral.bukkit.adtrigger.entry;

import app.stral.bukkit.adtrigger.TriggerType;

public class PeriodicEntry extends TriggerEntry {

    // 5 minutes interval (default)
    private long intervalTicks = 6000L;

    protected PeriodicEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.PERIODIC;
    }

    public long getIntervalTicks() {
        return intervalTicks;
    }

    public void setIntervalTicks(long intervalTicks) {
        this.intervalTicks = Math.max(1, intervalTicks);
    }
}
