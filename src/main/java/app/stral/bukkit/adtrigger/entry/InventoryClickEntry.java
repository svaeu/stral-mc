package app.stral.bukkit.adtrigger.entry;

import app.stral.bukkit.adtrigger.TriggerType;
import org.jspecify.annotations.NonNull;

public class InventoryClickEntry extends TriggerEntry {

    private String title, material = "";
    private int slot = -1;

    protected InventoryClickEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.INVENTORY_CLICK;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(@NonNull String material) {
        this.material = material;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
