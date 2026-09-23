package app.stral.bukkit.nms;

@FunctionalInterface
public interface PacketBuilder {
    Object build() throws Throwable;
}
