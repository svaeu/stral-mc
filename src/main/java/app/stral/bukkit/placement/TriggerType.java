package app.stral.bukkit.placement;

import app.stral.bukkit.placement.entry.*;

import java.util.function.Function;

public enum TriggerType {
    JOIN("join", PlayerJoinEntry.class, PlayerJoinEntry::new),
    RESPAWN("respawn", PlayerRespawnEntry.class, PlayerRespawnEntry::new),
    TELEPORT("teleport", PlayerTeleportEntry.class, PlayerTeleportEntry::new),
    PERIODIC("periodic", PeriodicEntry.class, PeriodicEntry::new),
    INVENTORY_CLICK("inventory_click", InventoryClickEntry.class, InventoryClickEntry::new),
    COMMAND("command", CommandEntry.class, CommandEntry::new);

    private final String display;
    private final Class<? extends TriggerEntry> entryClass;
    private final Function<String, ? extends TriggerEntry> factory;

    TriggerType(String display, Class<? extends TriggerEntry> entryClass,
                Function<String, ? extends TriggerEntry> factory) {
        this.display = display;
        this.entryClass = entryClass;
        this.factory = factory;
    }

    public String getDisplay() {
        return display;
    }

    public Class<? extends TriggerEntry> getEntryClass() {
        return entryClass;
    }

    public TriggerEntry createEntry(String id) {
        return factory.apply(id);
    }
}
