package app.stral.bukkit.httpclient;

import java.io.IOException;

public final class TransportException extends IOException {
    public final int status;

    public TransportException(int status) {
        super("HTTP " + status);
        this.status = status;
    }
}
