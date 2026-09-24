package app.stral.bukkit.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class GlobalScheduler implements TaskScheduler {

    @Override
    public CancellableTask runRepeating(Plugin plugin, Runnable task, long delay, long period) {
        final ScheduledTask delegate;

        delegate = Bukkit.getServer().getGlobalRegionScheduler()
                .runAtFixedRate(plugin, t -> task.run(), delay, period);
        return delegate::cancel;
    }

    @Override
    public CancellableTask runRepeatingAsynchronously(Plugin plugin, Runnable task, long delay, long period) {
        final ScheduledTask delegate;

        delegate = Bukkit.getServer().getAsyncScheduler()
                .runAtFixedRate(plugin, t -> task.run(),
                        delay * 50L, period * 50L, TimeUnit.MILLISECONDS);
        return delegate::cancel;
    }
}
