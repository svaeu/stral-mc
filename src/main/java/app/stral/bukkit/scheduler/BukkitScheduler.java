package app.stral.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitScheduler implements TaskScheduler {

    @Override
    public CancellableTask runRepeating(Plugin plugin, Runnable task, long delay, long period) {
        final BukkitTask scheduled;

        scheduled = Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
        return scheduled::cancel;
    }

    @Override
    public CancellableTask runOnce(Plugin plugin, Runnable task, long delay) {
        final BukkitTask scheduled;

        scheduled = Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        return scheduled::cancel;
    }

    @Override
    public void runOnceAsynchronously(Plugin plugin, Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }
}
