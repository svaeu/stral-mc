package app.stral.bukkit.listener;

import app.stral.bukkit.notification.NotificationService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final NotificationService notifications;

    public JoinListener(NotificationService notifications) {
        this.notifications = notifications;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        notifications.notify(event.getPlayer());
    }
}
