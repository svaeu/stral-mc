package app.stral.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import org.jetbrains.annotations.NotNull;

import static app.stral.bukkit.nms.ReflectionNms.MINOR;

public class Dashboard implements InventoryHolder {

    private static final int SLOT_CENTER_A = 11;
    private static final int SLOT_CENTER_B = 13;
    private static final int SLOT_CENTER_C = 15;

    private final Inventory inventory;

    @SuppressWarnings("deprecation")
    public Dashboard() {
        this.inventory = Bukkit.createInventory(
                this, 27, "Overview"
        );
        setup();
    }

    private void setup() {
        inventory.setItem(SLOT_CENTER_A, ItemBuilder.of(MINOR < 13
                ? Material.PUMPKIN
                : Material.CARVED_PUMPKIN
        ).build());

        inventory.setItem(SLOT_CENTER_B, ItemBuilder.of(
                Material.matchMaterial("COMPARATOR") != null
                ? Material.COMPARATOR
                : Material.matchMaterial("REDSTONE_COMPARATOR")
        ).build());

        inventory.setItem(SLOT_CENTER_C, ItemBuilder.of(Material.BOOK).build());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void handle(Player player, int slot) {
        switch (slot) {
            case SLOT_CENTER_C:
                break;
            case SLOT_CENTER_A:
                break;
            case SLOT_CENTER_B:
                break;

            default:
                break;
        }
    }
}
