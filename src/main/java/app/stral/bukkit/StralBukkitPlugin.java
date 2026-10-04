package app.stral.bukkit;

import app.stral.bukkit.config.BukkitConfig;
import app.stral.bukkit.config.PlacementConfig;
import app.stral.bukkit.httpclient.FetchingClient;
import app.stral.bukkit.httpclient.HttpTransport;

import app.stral.bukkit.httpclient.NetworkService;
import app.stral.bukkit.httpclient.TransportException;
import app.stral.bukkit.impression.PollingService;
import app.stral.bukkit.impression.RecipientQueue;
import app.stral.bukkit.listener.JoinListener;
import app.stral.bukkit.nms.Capability;
import app.stral.bukkit.notification.NotificationService;
import app.stral.bukkit.notification.NotificationType;
import app.stral.bukkit.placement.TriggerRegistry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

public final class StralBukkitPlugin extends JavaPlugin {

    public static final String BASE_API_URL = "https://adapter.stral.app";

    private FetchingClient httpClient;
    private NetworkService networkService;

    private RecipientQueue queue;
    private PollingService polling;

    private BukkitConfig bukkitConfig;
    private PlacementConfig placementConfig;

    private NotificationService notifications;

    @Override
    public void onEnable() {
        Capability.initialize();
        initConfiguration();

        notifications = new NotificationService(getLogger());
        registerEvent(new JoinListener(notifications));

        if(bukkitConfig.getApiKey().trim().isEmpty()) {
            notifications.notify(
                    NotificationType.NOT_REGISTERED,
                    BASE_API_URL + "/new/minecraft"
            );
            return;
        }

        httpClient = new FetchingClient(
                new HttpTransport(BASE_API_URL), bukkitConfig.getApiKey());
        networkService = new NetworkService();

        validateEndpoint();
    }

    private void setupPolling() {
        queue = new RecipientQueue(this);
        queue.start();

        polling = new PollingService(
                this,
                httpClient,
                queue,
                networkService
        );
        polling.start();
    }

    private void initConfiguration() {
        saveDefaultConfig();
        bukkitConfig = new BukkitConfig(getConfig());

        placementConfig = new PlacementConfig(this, new TriggerRegistry());
        placementConfig.register().load();
    }

    private void validateEndpoint() {
        networkService.execute(httpClient::fetchIdentity).thenAccept(
                identity -> identity.forEach((pub, stat) -> {
            notifications.notify(NotificationType.VALIDATED, pub, stat);

            if (!"active".equalsIgnoreCase(stat)) {
                notifications.notify(NotificationType.LIMITED);
                return;
            }
            notifications.clear(NotificationType.UNAVAILABLE);

            setupPolling();
        })).exceptionally(error -> {
            final Throwable cause;

            cause = networkService.unwrap(error);
            if (cause instanceof TransportException
                    && ((TransportException) cause).status == 401) {
                notifications.notify(
                        NotificationType.INVALID_KEY,
                        BASE_API_URL + "/new/minecraft"
                );
            } else {
                notifications.notify(NotificationType.UNAVAILABLE);
            }
            return null;
        });
    }

    private <T extends Listener> void registerEvent(T l) {
        getServer().getPluginManager().registerEvents(l, this);
    }

    @Override
    public void onDisable() {
        if (polling != null) polling.stop();
        if (queue != null) queue.stop();

        networkService.shutdown();

        placementConfig.save();
        placementConfig.unregister();

        EnumSet.allOf(NotificationType.class).forEach(notifications::clear);
    }
}
