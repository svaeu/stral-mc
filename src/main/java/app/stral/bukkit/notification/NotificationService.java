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
        if(type.target(NotificationTarget.CONSOLE))
            logger.log(type.getLevel(), message);

        if(type.target(NotificationTarget.PLAYER))
            synchronized (active) { active.put(type, message); }
    }

    public void notify(Player player) {
        synchronized (active) {
            for (Map.Entry<NotificationType, String> entry : active.entrySet()) {
                if (player.isOp()
                        || (entry.getKey().getPermission() != null
                        && player.hasPermission(entry.getKey().getPermission())))
                    player.sendMessage(
                            entry.getKey().getColor() + "[!] " + entry.getValue()
                    );
            }
        }
    }

    public void clear(NotificationType type) {
        synchronized (active) {
            active.remove(type);
        }
    }
}

