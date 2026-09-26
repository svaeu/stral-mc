package app.stral.bukkit.notification;

public enum NotificationType {
    UPDATE_AVAILABLE("stral.notify.update",
            "A new version is available: %s." +
                    " Use /stral update to download it," + " then restart the server to install the update."
    ),
    NOT_REGISTERED(
            "stral.notify.register",
            "Register your server to start monetizing: %s"
    ),
    LIMITED(
            "stral.notify.network",
            "This server has been limited for violating our Terms of Service. Contact support for assistance."
    ),
    UNAVAILABLE(
            "stral.notify.network",
            "Service is temporarily unavailable." +
                    " Try again later. If the issue persists, check your network connection or contact support."
    );

    private final String permission, message;

    NotificationType(String permission, String message) {
        this.message = message;
        this.permission = permission;
    }

    public String getMessage() {
        return message;
    }

    public String getPermission() {
        return permission;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
