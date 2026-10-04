package app.stral.bukkit.notification;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Level;

@SuppressWarnings("deprecation")
public enum NotificationType {
    UPDATE_AVAILABLE("stral.notify.update",
            "A new version is available: %s. Use /stral update to download it, then restart the server to install the update.",
            Level.INFO,
            ChatColor.YELLOW,
            NotificationTarget.CONSOLE,
            NotificationTarget.PLAYER
    ),
    NOT_REGISTERED(
            "stral.notify.register",
            "No API key set. Get one at: %s. Run /stral link <key>, or paste it under 'api-key' in config.yml",
            Level.WARNING,
            ChatColor.YELLOW,
            NotificationTarget.CONSOLE,
            NotificationTarget.PLAYER
    ),
    INVALID_KEY(
            "stral.notify.register",
            "The configured API key was rejected. Check your config.yml or generate a new key at: %s.",
            Level.SEVERE,
            ChatColor.RED,
            NotificationTarget.CONSOLE,
            NotificationTarget.PLAYER
    ),
    VALIDATED(
            null,
            "Connected to publisher '%s' (status: %r)",
            Level.INFO,
            ChatColor.BOLD,
            NotificationTarget.CONSOLE
    ),
    LIMITED(
            "stral.notify.network",
            "This server has been limited for violating our Terms of Service. Contact support for assistance.",
            Level.WARNING,
            ChatColor.RED,
            NotificationTarget.CONSOLE,
            NotificationTarget.PLAYER
    ),
    UNAVAILABLE(
            "stral.notify.network",
            "Service is temporarily unavailable. Try again later. If the issue persists, check your network connection or contact support.",
            Level.SEVERE,
            ChatColor.RED,
            NotificationTarget.CONSOLE,
            NotificationTarget.PLAYER
    );

    private final String permission, message;
    private final Level level;
    private final ChatColor color;
    private final EnumSet<NotificationTarget> targets;

    NotificationType(
            String permission,
            String message,
            Level level,
            ChatColor color,
            NotificationTarget... targets
    ) {
        this.message = message;
        this.permission = permission;
        this.level = level;
        this.color = color;
        this.targets = EnumSet.copyOf(Arrays.asList(targets));
    }

    public String getPermission() {
        return permission;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    public Level getLevel() {
        return level;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean target(NotificationTarget target) {
        return targets.contains(target);
    }
}
