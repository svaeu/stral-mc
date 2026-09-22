package app.stral.core.httpclient;

import java.io.IOException;

public final class TransportException extends IOException {
    public final int status;

    public TransportException(int status) {
        super("HTTP " + status);
        this.status = status;
    }
}
