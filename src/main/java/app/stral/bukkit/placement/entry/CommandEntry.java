package app.stral.bukkit.placement.entry;

import app.stral.bukkit.placement.TriggerType;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class CommandEntry extends TriggerEntry {

    private String command = "";

    public CommandEntry(String id) {
        super(id);
    }

    @Override
    public TriggerType getType() {
        return TriggerType.COMMAND;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(@NonNull String command) {
        this.command = command.startsWith("/") ? command.substring(1) : command;
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        final Map<String, Object> data;

        data = super.serialize();
        data.put("command", command);
        return data;
    }

    @Override
    protected void load(@NonNull Map<String, Object> data) {
        super.load(data);
        setCommand(readString(data, "command", ""));
    }

    public static CommandEntry deserialize(Map<String, Object> data) {
        final CommandEntry entry;

        entry = new CommandEntry(requireId(data));
        entry.load(data);
        return entry;
    }
}
