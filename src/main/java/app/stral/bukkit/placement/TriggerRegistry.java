package app.stral.bukkit.placement;

import app.stral.bukkit.placement.entry.TriggerEntry;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TriggerRegistry {

    private final EnumMap<TriggerType, Boolean> master = new EnumMap<>(TriggerType.class);
    private final EnumMap<TriggerType, LinkedHashMap
            <String, TriggerEntry>> entries = new EnumMap<>(TriggerType.class);

    public TriggerRegistry() {
        for(TriggerType type : TriggerType.values()) {
            master.put(type, false);
            entries.put(type, new LinkedHashMap<>());
        }
    }

    public Collection<TriggerEntry> getEntries(TriggerType type) {
        return Collections.unmodifiableCollection(entries.get(type).values());
    }

    public <T extends TriggerEntry> Optional<T> pickOne(TriggerType type, Class<T> entryClass) {
        final List<T> active;

        active = getActiveEntries(type, entryClass);
        return Optional.of(active.get(ThreadLocalRandom.current().nextInt(active.size())));
    }

    private <T extends TriggerEntry> List<T> getActiveEntries(
            TriggerType type, Class<T> entryClass) {
        final List<T> result;

        if (!isMasterEnabled(type)) return Collections.emptyList();
        result = new ArrayList<>();

        for (TriggerEntry entry : entries.get(type).values())
            if (entry.isEnabled()) result.add(entryClass.cast(entry));

        return result;
    }

    private boolean hasEntry(TriggerType type, String id) {
        return entries.get(type).containsKey(id);
    }

    public boolean addEntry(TriggerEntry entry) {
        return entries.get(entry.getType()).putIfAbsent(entry.getId(), entry) == null;
    }

    public TriggerEntry createEntry(TriggerType type, String id) {
        final TriggerEntry entry;

        if (hasEntry(type, id)) return null;
        entry = type.createEntry(id);

        addEntry(entry);
        return entry;
    }

    public boolean removeEntry(TriggerType type, String id) {
        return entries.get(type).remove(id) != null;
    }

    public boolean isMasterEnabled(TriggerType type) {
        return master.get(type);
    }

    public void setMaster(TriggerType type) {
        master.put(type, true);
    }

    public void clearAll() {
        entries.values().forEach(LinkedHashMap::clear);
    }
}
