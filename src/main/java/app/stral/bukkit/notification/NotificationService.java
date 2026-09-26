package app.stral.bukkit.notification;

import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

public final class NotificationService {

    private final Logger logger;

    private final Map<NotificationType, String> active =
            new EnumMap<>(NotificationType.class);

    public NotificationService(Logger logger) {
        this.logger = logger;
    }

    public void notify(NotificationType type, Object... args) {
        final String message;

        message = type.format(args);
        if (active.put(type, message) == null) logger.warning(message);
    }

    public void notify(Player player) {
        synchronized (active) {
            for (Map.Entry<NotificationType, String> entry : active.entrySet())
                notify(player, entry.getKey(), entry.getValue());
        }
    }

    private void notify(
            Player player,
            NotificationType type,
            String message
    ) {
        if (player.isOp() || player.hasPermission(type.getPermission())) {
            player.sendMessage(message);
        }
    }

    public void clear(NotificationType type) {
        synchronized (active) {
            active.remove(type);
        }
    }
}

