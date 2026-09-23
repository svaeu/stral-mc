package app.stral.bukkit.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

public final class ReflectionNms {

    public static final String NMS_VERSION = Bukkit.getServer().getClass()
            .getPackage().getName().split("\\.")[3];
    public static final int MINOR = Integer.parseInt(NMS_VERSION.split("_")[1]);

    private static final MethodHandles.Lookup L = MethodHandles.lookup();

    private static final MethodHandle GET_HANDLE;
    private static final MethodHandle GET_CONNECTION;
    private static final MethodHandle SEND_PACKET;
    private static final MethodHandle AS_COPY;

    static {
        try {
            GET_HANDLE = L.unreflect(
                    obc("entity.CraftPlayer").getMethod("getHandle"));
            GET_CONNECTION = L.unreflectGetter(
                    nms("EntityPlayer").getField("playerConnection"));
            SEND_PACKET = L.unreflect(
                    nms("PlayerConnection").getMethod("sendPacket", nms("Packet")));
            AS_COPY = L.unreflect(
                    obc("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "(NMS) failed to resolve base internals for " + NMS_VERSION + ": " + e);
        }
    }

    public static Class<?> nms(String simpleName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + NMS_VERSION + "." + simpleName);
    }

    public static Class<?> obc(String relativePath) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + "." + relativePath);
    }

    public static Object getHandle(Player player) throws Throwable {
        return GET_HANDLE.invoke(player);
    }

    public static Object getConnection(Object nmsPlayer) throws Throwable {
        return GET_CONNECTION.invoke(nmsPlayer);
    }

    public static void sendPacket(Object connection, Object packet) throws Throwable {
        SEND_PACKET.invoke(connection, packet);
    }

    public static Object asCopy(ItemStack item) throws Throwable {
        return AS_COPY.invoke(item);
    }

    public static MethodHandle findConstructor(Class<?> owner, Class<?>... signature) {
        try {
            return L.unreflectConstructor(owner.getConstructor(signature));
        } catch (Exception e) {
            throw new RuntimeException("No constructor " + owner.getSimpleName() +
                    Arrays.toString(signature) + " on " + NMS_VERSION, e);
        }
    }

    public static MethodHandle findMethod(Class<?> owner, String name, Class<?>... signature) {
        try {
            return L.unreflect(owner.getMethod(name, signature));
        } catch (Exception e) {
            throw new RuntimeException("No method " + owner.getSimpleName() + "." + name +
                    Arrays.toString(signature) + " on " + NMS_VERSION, e);
        }
    }
    private ReflectionNms() {}
}
