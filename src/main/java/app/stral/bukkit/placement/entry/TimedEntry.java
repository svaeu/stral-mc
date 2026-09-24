package app.stral.bukkit.placement.entry;

import org.jspecify.annotations.NonNull;

import java.util.Map;

public abstract class TimedEntry extends TriggerEntry {

    private long delayTicks = 20L;

    protected TimedEntry(String id) {
        super(id);
    }

    public long getDelayTicks() {
        return delayTicks;
    }

    public void setDelayTicks(long delayTicks) {
        this.delayTicks = Math.max(0, delayTicks);
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        final Map<String, Object> data;

        data = super.serialize();
        data.put("delay-ticks", delayTicks);
        return data;
    }

    @Override
    protected void load(@NonNull Map<String, Object> data) {
        super.load(data);
        setDelayTicks(readLong(data, "delay-ticks", 20L));
    }
}
