package app.stral.bukkit.impression;

import app.stral.bukkit.httpclient.FetchingClient;
import app.stral.bukkit.httpclient.NetworkService;
import app.stral.bukkit.nms.Capability;
import app.stral.bukkit.scheduler.CancellableTask;
import org.bukkit.plugin.Plugin;

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
    private final NetworkService network;

    private CancellableTask pollingTask;
    private boolean pollRunning;

    public PollingService(Plugin plugin, FetchingClient restClient,
                          RecipientQueue recipient, NetworkService network) {
        this.plugin = plugin;
        this.restClient = restClient;
        this.recipient = recipient;
        this.network = network;

        this.registry = new GrantRegistry(plugin.getLogger());
    }

    public void start() {
        if(pollRunning) return;

        pollRunning = true;
        pollOnce();
    }

    public void stop() {
        pollRunning = false;

        if(pollingTask != null) {
            pollingTask.cancel();

            pollingTask = null;
        }
    }

    private void pollOnce() {
        if (!pollRunning) return;

        network.execute(restClient::fetchStatus)
                .whenComplete((status, error) -> {

                    if (error != null) {
                        plugin.getLogger().log(Level.WARNING,
                                "Polling status endpoint failed", error);
                    } else {
                        status.forEach((t, s) -> {
                            GrantRegistry.Grant grant = registry.remove(t);

                            if (grant != null
                                    && "COMPLETE".equalsIgnoreCase(s)) {
                                recipient.enqueue(grant);
                            }
                        });
                        registry.sweepStale(TOKEN_TTL_MILLIS);
                    }
                    if (!pollRunning) return;

                    pollingTask = Capability.SCHEDULER.runOnce(
                            plugin,
                            this::pollOnce,
                            POLLING_TICKS
                    );
                });
    }
}
