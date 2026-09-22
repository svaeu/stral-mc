package app.stral.core.impression;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class GrantRegistry {

    private final Map<String, Grant> pending;
    private final Logger logger;

    public static class Grant {
        private final String command;
        private final int slots;
        private final UUID playerId;
        private final boolean requiresOnline;
        private final long createdAtMillis;

        public Grant(UUID playerId,
                     String command, boolean requiresOnline, int slots) {
            this.playerId = playerId;
            this.requiresOnline = requiresOnline;
            this.command = command;
            this.slots = slots;
            this.createdAtMillis = System.currentTimeMillis();
        }

        public UUID getPlayerId() { return playerId; }
        public String getCommand() { return command; }
        public int getSlots() { return slots; }

        public boolean requiresOnline() { return requiresOnline;}

        public long ageMillis() { return System.currentTimeMillis() - createdAtMillis; }
    }

    public GrantRegistry(final Logger logger) {
        this.logger = logger;
        pending = new ConcurrentHashMap<>();
    }

    public void add(String token, UUID uuid, String cmd, boolean online, int slots) {
        pending.put(token, new Grant(uuid, cmd, online, slots));
    }

    public Grant remove(String token) {
        return pending.remove(token);
    }

    public void sweepStale(long ttlMillis) {
        for(Map.Entry<String, Grant> entry : pending.entrySet())
            if(entry.getValue().ageMillis() >= ttlMillis)
                if(pending.remove(entry.getKey(), entry.getValue()))
                    logger.fine("Swept stale grant: " + entry.getKey() +
                            " (recipient=" + entry.getValue().getPlayerId() + ", age=" +
                            entry.getValue().ageMillis() + "ms)");
    }
}
