package app.stral.bukkit.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class GlobalScheduler implements TaskScheduler {

    @Override
    public CancellableTask runRepeating(Plugin plugin, Runnable task, long delay, long period) {
        final ScheduledTask delegate;

        delegate = Bukkit.getServer().getGlobalRegionScheduler()
                .runAtFixedRate(plugin, t -> task.run(), delay, period);
        return delegate::cancel;
    }

    @Override
    public CancellableTask runOnce(Plugin plugin, Runnable task, long delay) {
        final ScheduledTask delegate;

        delegate = Bukkit.getServer().getGlobalRegionScheduler()
                .runDelayed(plugin, t -> task.run(), delay);
        return delegate::cancel;
    }

    @Override
    public void runOnceAsynchronously(Plugin plugin, Runnable task) {
        Bukkit.getServer().getAsyncScheduler().runNow(plugin, t -> task.run());
    }
}
