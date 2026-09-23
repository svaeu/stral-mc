package app.stral.bukkit.adtrigger;

public enum TriggerType {
    JOIN("join"),
    RESPAWN("respawn"),
    TELEPORT("teleport"),
    PERIODIC("periodic"),
    INVENTORY_CLICK("inventory_click"),
    COMMAND("command");

    private final String display;

    TriggerType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
