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
    public CancellableTask runRepeatingAsynchronously(Plugin plugin, Runnable task, long delay, long period) {
        final BukkitTask scheduled;

        scheduled = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
        return scheduled::cancel;
    }

    @Override
    public CancellableTask runOnceAsynchronously(Plugin plugin, Runnable task) {
        final BukkitTask scheduled;

        scheduled = Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        return scheduled::cancel;
    }
}
