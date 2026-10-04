package app.stral.bukkit.httpclient;

import java.io.IOException;
import java.util.concurrent.*;

public class NetworkService {

    private static final int THREADS = 4;
    private static final long INITIAL_DELAY = 100L;
    private static final long MAX_DELAY = 6000L;
    private static final int MAX_RETRIES = 6;

    private final ScheduledExecutorService executor =
            Executors.newScheduledThreadPool(THREADS, runnable -> {
                final Thread worker;

                worker = new Thread(runnable, "http-stral");
                worker.setDaemon(true);

                return worker;
            });

    public NetworkService() {}

    public <T> CompletableFuture<T> execute(Callable<T> action) {
        final CompletableFuture<T> future;

        future = new CompletableFuture<>();
        attempt(action, future, 0);
        return future;
    }

    private <T> void attempt(
            Callable<T> action,
            CompletableFuture<T> future,
            int retries
    ) {
        if (future.isCancelled()) return;

        executor.execute(() -> {
            try { future.complete(action.call()); }

            catch (Throwable error) {
                final Throwable cause;

                cause = unwrap(error);
                if (!isRetryable(cause) || retries >= MAX_RETRIES) {
                    future.completeExceptionally(cause);
                    return;
                }

                executor.schedule(
                        () -> attempt(
                                action, future, retries + 1
                        ),
                        Math.min(
                                INITIAL_DELAY * (1L << retries), MAX_DELAY
                        ),
                        TimeUnit.MILLISECONDS
                );
            }
        });
    }

    private boolean isRetryable(Throwable error) {
        final int status;

        if (error instanceof TransportException) {
            status = ((TransportException) error).status;

            return status == 408
                    || status == 429
                    || status >= 500;
        }
        return error instanceof IOException;
    }

    public Throwable unwrap(Throwable error) {
        while (error instanceof CompletionException)
            error = error.getCause();

        return error;
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}
