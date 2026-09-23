package app.stral.bukkit;

import app.stral.bukkit.config.BukkitConfig;
import app.stral.bukkit.httpclient.FetchingClient;
import app.stral.bukkit.httpclient.HttpTransport;

import app.stral.bukkit.impression.GrantRegistry;
import app.stral.bukkit.impression.PollingService;
import app.stral.bukkit.impression.RecipientQueue;
import app.stral.bukkit.listener.InventoryListener;
import app.stral.bukkit.listener.JoinListener;
import app.stral.bukkit.nms.Capability;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class StralBukkitPlugin extends JavaPlugin {

    public static final String BASE_API_URL = "https://adapter.stral.app";

    private RecipientQueue queue;
    private PollingService polling;
    private BukkitConfig bukkitConfig;

    public static boolean missingSecret = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        bukkitConfig = new BukkitConfig(getConfig());

        registerEvent(new InventoryListener());
        registerEvent(new JoinListener());

        if(bukkitConfig.getApiKey() == null) {
            getLogger().warning(
                    "Get API Key: " + BASE_API_URL + "");

            missingSecret = true;
            return;
        }
        setupRestServices();

        Capability.initialize();
    }

    private <T extends Listener> void registerEvent(T l) {
        getServer().getPluginManager().registerEvents(l, this);
    }

    private void setupRestServices() {
        final HttpTransport transport;
        final FetchingClient httpClient;
        final GrantRegistry registry;

        transport = new HttpTransport(BASE_API_URL);
        httpClient = new FetchingClient(transport, bukkitConfig.getApiKey());

        registry = new GrantRegistry(getLogger());

        queue = new RecipientQueue(this);
        queue.start();

        polling = new PollingService(this, registry, httpClient, queue);
        polling.start();
    }

    @Override
    public void onDisable() {
        if (queue != null) queue.stop();
        if (polling != null) polling.stop();
    }
}
