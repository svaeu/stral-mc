package app.stral.bukkit.gui.redirect;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BookLinkage {
    void prompt(Player player, ItemStack book);
}