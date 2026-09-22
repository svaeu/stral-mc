package app.stral.core.gui.redirect;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BukkitBookLinkage implements BookLinkage {
    @Override
    public void prompt(Player player, ItemStack book) {
        player.openBook(book);
    }
}
