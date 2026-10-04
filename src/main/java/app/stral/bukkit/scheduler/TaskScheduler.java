package app.stral.bukkit.scheduler;

import org.bukkit.plugin.Plugin;

public interface TaskScheduler {
    CancellableTask runRepeating(Plugin plugin, Runnable task, long delay, long period);

    CancellableTask runOnce(Plugin plugin, Runnable task, long delay);
    void runOnceAsynchronously(Plugin plugin, Runnable task);
}
