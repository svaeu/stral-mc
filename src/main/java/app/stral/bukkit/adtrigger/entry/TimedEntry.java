package app.stral.bukkit.adtrigger.entry;

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
}
