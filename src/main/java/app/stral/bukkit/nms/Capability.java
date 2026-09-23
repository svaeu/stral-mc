package app.stral.bukkit.nms;

import app.stral.bukkit.gui.redirect.BukkitBookLinkage;
import app.stral.bukkit.gui.redirect.LegacyBookLinkage;
import app.stral.bukkit.gui.redirect.BookLinkage;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class Capability {

    public static final BookLinkage REDIRECT;

    public static void initialize() {}

    static {
        REDIRECT = hasMethod(Player.class, "openBook", ItemStack.class)
                ? new BukkitBookLinkage()
                : new LegacyBookLinkage();
    }

    private static boolean hasMethod(Class<?> type, String name, Class<?>... sign) {
        try {
            type.getMethod(name, sign);
            return true;
        } catch (NoSuchMethodException e) { return false; }
    }

    private Capability() {}
}