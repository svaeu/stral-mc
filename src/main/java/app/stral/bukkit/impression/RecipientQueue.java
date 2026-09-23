package app.stral.bukkit.impression;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class RecipientQueue {

    private static final long DRAIN_TICKS = 60L;
    private static final int CMD_THRESHOLD = 5;
    private static final long GRANT_TTL_MILLIS = Duration.ofMinutes(30).toMillis();

    private final Queue<GrantRegistry.Grant> recipients = new ConcurrentLinkedQueue<>();
    private final Plugin plugin;

    private BukkitTask task;

    public RecipientQueue(Plugin plugin) {
        this.plugin = plugin;
    }

    public void enqueue(GrantRegistry.Grant grant) {
        recipients.add(grant);
    }

    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(
                plugin, this::drain, DRAIN_TICKS, DRAIN_TICKS);
    }

    public void stop() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }

    private void drain() {
        GrantRegistry.Grant grant;

        for(int i = 0; i < CMD_THRESHOLD; i++) {
            grant = recipients.poll();
            if(grant == null) return;
            if(grant.ageMillis() >= GRANT_TTL_MILLIS) continue;

            apply(grant);
        }
    }

    private void apply(GrantRegistry.Grant grant) {
        final OfflinePlayer offline;
        final Player player;

        offline = Bukkit.getOfflinePlayer(grant.getPlayerId());
        if (grant.requiresOnline()) {
            player = offline.isOnline() ? offline.getPlayer() : null;

            if (player == null || getEmptySlots(player) < grant.getSlots()) {
                recipients.add(grant);
                return;
            }
        }

        try {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), grant.getCommand());
        } catch (CommandException e) {
            plugin.getLogger().log(Level.WARNING,
                    "Could not compensate " + offline.getUniqueId()
                            + " (" + offline.getName() + "), command: " + grant.getCommand(), e);
        }
    }

    private int getEmptySlots(Player player) {
        int emptySlots = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                emptySlots++;
            }
        }
        return emptySlots;
    }
}
