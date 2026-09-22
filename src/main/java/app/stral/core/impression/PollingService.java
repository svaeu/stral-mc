package app.stral.core.impression;

import app.stral.core.httpclient.FetchingClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Level;

public class PollingService {
    // implement adaptive polling <- todo
    private static final long POLLING_TICKS = 0;
    private static final long TOKEN_TTL_MILLIS = Duration.ofMinutes(15).toMillis();

    private final Plugin plugin;
    private final GrantRegistry registry;
    private final FetchingClient restClient;
    private final RecipientQueue recipient;

    private BukkitTask pollingTask;

    public PollingService(Plugin plugin, GrantRegistry registry,
                          FetchingClient restClient, RecipientQueue recipient) {
        this.plugin = plugin;
        this.registry = registry;
        this.restClient = restClient;
        this.recipient = recipient;
    }

    public void start() {
        pollingTask = Bukkit.getScheduler()
                .runTaskTimerAsynchronously(plugin,
                        this::pollOnce, POLLING_TICKS, POLLING_TICKS);
    }

    public void stop() {
        if(pollingTask != null) {
            pollingTask.cancel();

            pollingTask = null;
        }
    }

    private void pollOnce() {
        try {
            restClient.fetchTerminal().forEach((t, s) -> {
                final GrantRegistry.Grant grant;

                grant = registry.remove(t);
                if(grant != null && "COMPLETE".equalsIgnoreCase(s))
                    recipient.enqueue(grant);
            });
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING,
                    "Polling status endpoint failed", e);
        }
        registry.sweepStale(TOKEN_TTL_MILLIS);
    }
}
