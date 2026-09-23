package app.stral.bukkit.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public final class ItemBuilder {

    private static final Enchantment unbreaking;

    static { unbreaking = resolveUnbreaking(); }

    private final ItemStack item;
    private final ItemMeta meta;

    private ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder name(String name) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes(
                '&',
                name
        ));
        return this;
    }

    public ItemBuilder lore(String... lines) {
        return lore(Arrays.asList(lines));
    }

    @SuppressWarnings("deprecation")
    private ItemBuilder lore(List<String> lines) {
        final List<String> translated;

        translated = new java.util.ArrayList<>();
        for (String line : lines) {
            translated.add(ChatColor.translateAlternateColorCodes(
                    '&', line
            ));
        }
        meta.setLore(translated);
        return this;
    }

    public ItemBuilder glow() {
        if(unbreaking == null) return this;

        meta.addEnchant(
                unbreaking,
                1,
                true
        );
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    @SuppressWarnings({"deprecation"})
    private static Enchantment resolveUnbreaking() {
        try {
            return Enchantment.getByKey(NamespacedKey.minecraft("unbreaking"));
        } catch (Throwable t) {
            final String getById;
            try {
                getById = "getById";
                return (Enchantment) Enchantment.class
                        .getMethod(getById, int.class)
                        .invoke(null, 34);
            } catch (Exception ignored) {}
        }
        return null;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
