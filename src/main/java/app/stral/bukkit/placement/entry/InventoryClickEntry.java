package app.stral.bukkit.placement.entry;

import app.stral.bukkit.placement.TriggerType;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class InventoryClickEntry extends TriggerEntry {

    private String title, material = "";
    private int slot = -1;

    public InventoryClickEntry(String id) {
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

    @Override
    public @NonNull Map<String, Object> serialize() {
        final Map<String, Object> data;

        data = super.serialize();
        data.put("title", title);
        data.put("material", material);
        data.put("slot", slot);
        return data;
    }

    @Override
    protected void load(@NonNull Map<String, Object> data) {
        super.load(data);
        setTitle(readString(data, "title", ""));
        setMaterial(readString(data, "material", ""));
        setSlot(readInt(data, "slot", -1));
    }

    public static InventoryClickEntry deserialize(Map<String, Object> data) {
        final InventoryClickEntry clickEntry;

        clickEntry = new InventoryClickEntry(requireId(data));
        clickEntry.load(data);
        return clickEntry;
    }
}
