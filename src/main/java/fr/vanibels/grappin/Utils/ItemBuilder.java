package fr.vanibels.grappin.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag; // Import for ItemFlags
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // For Objects.requireNonNull

public class ItemBuilder {

    private ItemStack is;

    /**
     * Initializes the ItemBuilder with a Material and an amount of 1.
     * @param m The Material of the ItemStack.
     */
    public ItemBuilder(@NotNull Material m) {
        this(m, 1);
    }

    /**
     * Initializes the ItemBuilder with an existing ItemStack.
     * @param is The ItemStack to start with.
     */
    public ItemBuilder(@NotNull ItemStack is) {
        // Create a copy of the ItemStack to prevent external modifications
        this.is = is.clone();
    }

    /**
     * Initializes the ItemBuilder with a Material and a specified amount.
     * @param m The Material of the ItemStack.
     * @param amount The amount of items in the stack.
     */
    public ItemBuilder(@NotNull Material m, int amount) {
        Objects.requireNonNull(m, "Material cannot be null"); // Ensure material is not null
        this.is = new ItemStack(m, amount);
    }

    /**
     * Initializes the ItemBuilder with a Material, amount, and a durability (meta).
     * This constructor is mostly for older Minecraft versions or specific uses like dyed wool.
     * For modern Minecraft, use CustomModelData or ItemMeta properties.
     * @param m The Material of the ItemStack.
     * @param amount The amount of items in the stack.
     * @param durability The durability value.
     */
    @SuppressWarnings("deprecation") // Durability directly on ItemStack is deprecated in modern API
    public ItemBuilder(@NotNull Material m, int amount, short durability){
        Objects.requireNonNull(m, "Material cannot be null");
        this.is = new ItemStack(m, amount, durability);
    }

    /**
     * Creates a new ItemBuilder instance that is a clone of the current ItemStack.
     * This is useful if you want to create a new builder based on an existing item
     * without modifying the original item or builder.
     * @return A new ItemBuilder instance with a cloned ItemStack.
     */
    public ItemBuilder clone() {
        return new ItemBuilder(this.is); // Already clones in the constructor
    }

    /**
     * Sets the durability of the ItemStack.
     * Note: Direct durability setting is largely deprecated for many items in modern Minecraft
     * in favor of Unbreakable property or CustomModelData for custom items.
     * @param dur The durability value.
     * @return The ItemBuilder instance.
     */
    @SuppressWarnings("deprecation") // setDurability is deprecated for most items
    public ItemBuilder setDurability(short dur) {
        this.is.setDurability(dur);
        return this;
    }

    /**
     * Sets the display name of the ItemStack.
     * Supports color codes using ChatColor.translateAlternateColorCodes('&', name).
     * @param name The display name.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setName(@Nullable String name) {
        ItemMeta im = is.getItemMeta();
        if (im != null) { // Always check for null ItemMeta
            im.setDisplayName(name != null ? ChatColor.translateAlternateColorCodes('&', name) : null);
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Adds an unsafe enchantment to the ItemStack. This bypasses level limits.
     * @param ench The Enchantment to add.
     * @param level The level of the enchantment.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder addUnsafeEnchantment(@NotNull Enchantment ench, int level) {
        Objects.requireNonNull(ench, "Enchantment cannot be null");
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    /**
     * Removes an enchantment from the ItemStack.
     * @param ench The Enchantment to remove.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder removeEnchantment(@NotNull Enchantment ench) {
        Objects.requireNonNull(ench, "Enchantment cannot be null");
        is.removeEnchantment(ench);
        return this;
    }

    /**
     * Sets the owner of a player head.
     * @param owner The name of the player whose skin the head should have.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setSkullOwner(@NotNull String owner) {
        Objects.requireNonNull(owner, "Skull owner name cannot be null");
        if (is.getItemMeta() instanceof SkullMeta) {
            SkullMeta sm = (SkullMeta) is.getItemMeta();
            sm.setOwner(owner);
            is.setItemMeta(sm);
        }
        return this;
    }

    /**
     * Adds a safe enchantment to the ItemStack. This respects level limits.
     * @param ench The Enchantment to add.
     * @param level The level of the enchantment.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder addEnchant(@NotNull Enchantment ench, int level) {
        Objects.requireNonNull(ench, "Enchantment cannot be null");
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            im.addEnchant(ench, level, true); // true allows for unsafe levels if needed, but addEnchant generally respects limits
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Makes the item unbreakable. In modern Minecraft, this is generally preferred over setting durability to MAX_VALUE.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) { // Renamed from UnBreak to setUnbreakable and added boolean param
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            im.setUnbreakable(unbreakable);
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Sets the lore (description lines) of the ItemStack.
     * Supports color codes using ChatColor.translateAlternateColorCodes('&', line).
     * @param lore A variable argument list of strings for the lore.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setLore(@Nullable String... lore) {
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            List<String> coloredLore = new ArrayList<>();
            if (lore != null) {
                for (String line : lore) {
                    if (line != null) {
                        coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                    }
                }
            }
            im.setLore(coloredLore);
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Sets the lore (description lines) of the ItemStack from a List.
     * Supports color codes using ChatColor.translateAlternateColorCodes('&', line).
     * @param loreList A list of strings for the lore.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setLore(@Nullable List<String> loreList) {
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            List<String> coloredLore = new ArrayList<>();
            if (loreList != null) {
                for (String line : loreList) {
                    if (line != null) {
                        coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                    }
                }
            }
            im.setLore(coloredLore);
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Sets the color of leather armor.
     * @param color The Color to apply.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setLeatherArmorColor(@NotNull Color color) {
        Objects.requireNonNull(color, "Color cannot be null");
        if (is.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Hides specific item flags (e.g., enchantments, attributes, durability).
     * @param flags The ItemFlags to hide.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder hideFlags(@NotNull ItemFlag... flags) {
        Objects.requireNonNull(flags, "ItemFlags cannot be null");
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            im.addItemFlags(flags);
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Makes the item glow (as if enchanted) without actually adding enchantments.
     * This is typically done by adding a dummy unsafe enchantment and then hiding its flag.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setGlow() {
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            im.addEnchant(Enchantment.UNBREAKING, 1, true); // Add a dummy enchantment
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Hide the enchantment tag
            is.setItemMeta(im);
        }
        return this;
    }

    /**
     * Builds and returns the final ItemStack.
     * @return The constructed ItemStack.
     */
    public ItemStack toItemStack() {
        return is;
    }

    /**
     * Sets the amount of items in the stack.
     * @param amount The new amount.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setAmount(int amount) {
        is.setAmount(amount);
        return this;
    }

    /**
     * Sets the CustomModelData of the ItemStack. This is used for custom textures/models
     * in modern Minecraft versions (1.14+).
     * @param customModelData The CustomModelData integer.
     * @return The ItemBuilder instance.
     */
    public ItemBuilder setCustomModelData(@Nullable Integer customModelData) {
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            im.setCustomModelData(customModelData); // Can be null to remove
            is.setItemMeta(im);
        }
        return this;
    }
}