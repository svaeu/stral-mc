package app.stral.bukkit.nms;

import app.stral.bukkit.gui.redirect.BukkitBookLinkage;
import app.stral.bukkit.gui.redirect.LegacyBookLinkage;
import app.stral.bukkit.gui.redirect.BookLinkage;

import app.stral.bukkit.scheduler.BukkitScheduler;
import app.stral.bukkit.scheduler.GlobalScheduler;
import app.stral.bukkit.scheduler.TaskScheduler;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class Capability {

    public static final BookLinkage REDIRECT;
    public static final TaskScheduler SCHEDULER;

    public static void initialize() {}

    static {
        REDIRECT = hasMethod(Player.class, "openBook", ItemStack.class)
                ? new BukkitBookLinkage()
                : new LegacyBookLinkage();

        SCHEDULER = hasMethod(Server.class, "getGlobalRegionScheduler")
                ? new GlobalScheduler()
                : new BukkitScheduler();
    }

    private static boolean hasMethod(Class<?> type, String name, Class<?>... sign) {
        try {
            type.getMethod(name, sign);
            return true;
        } catch (NoSuchMethodException e) { return false; }
    }

    private Capability() {}
}