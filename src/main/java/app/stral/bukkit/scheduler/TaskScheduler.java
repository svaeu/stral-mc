package app.stral.bukkit.scheduler;

import org.bukkit.plugin.Plugin;

public interface TaskScheduler {
    CancellableTask runRepeating(Plugin plugin, Runnable task, long delay, long period);
    CancellableTask runRepeatingAsynchronously(Plugin plugin, Runnable task, long delay, long period);
}
