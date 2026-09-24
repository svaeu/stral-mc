package app.stral.bukkit.placement.entry;

import app.stral.bukkit.placement.TriggerType;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class PeriodicEntry extends TriggerEntry {

    // 5 minutes interval (default)
    private long intervalTicks = 6000L;

    public PeriodicEntry(String id) {
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

    @Override
    public @NonNull Map<String, Object> serialize() {
        final Map<String, Object> data;

        data = super.serialize();
        data.put("interval-ticks", intervalTicks);
        return data;
    }

    @Override
    protected void load(@NonNull Map<String, Object> data) {
        super.load(data);
        setIntervalTicks(readLong(data, "interval-ticks", 6000L));
    }

    public static PeriodicEntry deserialize(Map<String, Object> data) {
        final PeriodicEntry periodicEntry;

        periodicEntry = new PeriodicEntry(requireId(data));
        periodicEntry.load(data);
        return periodicEntry;
    }
}
