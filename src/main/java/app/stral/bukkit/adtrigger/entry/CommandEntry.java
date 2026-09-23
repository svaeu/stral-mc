package app.stral.bukkit.adtrigger.entry;

import app.stral.bukkit.adtrigger.TriggerType;
import org.jspecify.annotations.NonNull;

public class CommandEntry extends TriggerEntry {

    private String command = "";

    protected CommandEntry(String id) {
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
}
