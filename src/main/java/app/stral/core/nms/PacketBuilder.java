package app.stral.core.nms;

@FunctionalInterface
public interface PacketBuilder {
    Object build() throws Throwable;
}
