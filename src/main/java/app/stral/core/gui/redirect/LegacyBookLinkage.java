package app.stral.core.gui.redirect;

import app.stral.core.nms.PacketBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static app.stral.core.nms.ReflectionNms.*;

public class LegacyBookLinkage implements BookLinkage {

    private static final PacketBuilder OPEN_BOOK_PACKET;
    private static final MethodHandle SET_SLOT_PACKET;

    static {
        try {
            SET_SLOT_PACKET = findConstructor(
                    nms("PacketPlayOutSetSlot"), int.class, int.class, nms("ItemStack"));

            if (MINOR >= 14) {
                final Class<?> enumHand;
                final MethodHandle packet;
                final Object mainHand;

                enumHand = nms("EnumHand");
                packet = findConstructor(nms("PacketPlayOutOpenBook"), enumHand);
                mainHand = enumHand.getEnumConstants()[0];

                OPEN_BOOK_PACKET = () -> packet.invoke(mainHand);
            } else {
                final Class<?> payload, serializer,
                        byteBuf, unpooled, namespaced;
                final MethodHandle packetSerializer,
                        buffer, ctor;
                final Object key;

                payload = nms("PacketPlayOutCustomPayload");
                serializer = nms("PacketDataSerializer");
                byteBuf = Class.forName("io.netty.buffer.ByteBuf");
                unpooled = Class.forName("io.netty.buffer.Unpooled");

                packetSerializer = findConstructor(serializer, byteBuf);
                buffer = findMethod(unpooled, "buffer");

                if (MINOR == 13) {
                    namespaced = nms("MinecraftKey");
                    key = namespaced.getConstructor(String.class).newInstance("minecraft:book_open");
                    ctor = MethodHandles.insertArguments(
                            findConstructor(payload, namespaced, serializer), 0, key);
                } else {
                    ctor = MethodHandles.insertArguments(
                            findConstructor(payload, String.class, serializer), 0, "MC|BOpen");
                }
                OPEN_BOOK_PACKET = () -> ctor.invoke(packetSerializer.invoke(buffer.invoke()));
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "LegacyBookLinkage failed to resolve for " + NMS_VERSION + ": " + e);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void prompt(Player player, ItemStack book) {
        try {
            final Object conn;
            final int slot;

            conn = getConnection(getHandle(player));
            slot = player.getInventory().getHeldItemSlot();

            sendPacket(conn, SET_SLOT_PACKET.invoke(0, slot + 36, asCopy(book)));
            sendPacket(conn, OPEN_BOOK_PACKET.build());
            sendPacket(conn, SET_SLOT_PACKET.invoke(
                    0, slot + 36, asCopy(player.getInventory().getItemInHand())));
        } catch (Throwable t) {
            throw new RuntimeException("Failed to prompt legacy book for " + player.getName(), t);
        }
    }
}