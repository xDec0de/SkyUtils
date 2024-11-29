package net.codersky.skyutils.spigot.builders;

import com.google.common.collect.Multimap;
import net.codersky.skyutils.java.SkyCollections;
import net.codersky.skyutils.java.strings.MCStrings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A class made to provide easy and fast
 * creation of {@link ItemStack ItemStaks}.
 * 
 * @author xDec0de_
 * 
 * @since SkyUtils 1.0.0
 * 
 * @see SkullBuilder
 */
@SuppressWarnings("deprecation")
public class ItemBuilder implements Cloneable {

	final ItemStack item;
	final ItemMeta meta;

	/**
	 * Creates a new {@link ItemBuilder} with the specified {@link Material}.
	 * 
	 * @param material the {@link Material} of the new {@link ItemStack} to create.
	 * 
	 * @throws NullPointerException if {@code material} is {@code null}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	public ItemBuilder(@NotNull Material material) {
		this.item = new ItemStack(Objects.requireNonNull(material));
		this.meta = Bukkit.getItemFactory().getItemMeta(material);
	}

	/**
	 * Creates a new {@link ItemBuilder} with the specified {@link ItemStack}.
	 * 
	 * @param stack the {@link ItemStack} to use.
	 * 
	 * @param clone whether to {@link ItemStack#clone() clone} the {@code stack} and its
	 * {@link ItemMeta} or not.
	 * 
	 * @throws IllegalArgumentException if {@code stack} is {@code null}.
	 * 
	 * @since SkyUtils 1.0.0
	 * 
	 * @see #ItemBuilder(ItemStack)
	 */
	public ItemBuilder(@NotNull ItemStack stack, boolean clone) {
		if (stack == null)
			throw new IllegalArgumentException("Stack cannot be null.");
		if (clone) {
			this.item = stack.clone();
			this.meta = item.hasItemMeta() ? item.getItemMeta().clone() : Bukkit.getItemFactory().getItemMeta(stack.getType());
		} else {
			this.item = stack;
			this.meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(stack.getType());
		}
	}

	/**
	 * Creates a new {@link ItemBuilder} with the specified {@link ItemStack},
	 * {@link ItemStack#clone() cloning} it.
	 * 
	 * @param stack the {@link ItemStack} to {@link ItemStack#clone() clone} and use.
	 * 
	 * @throws IllegalArgumentException if {@code stack} is {@code null}.
	 * 
	 * @since SkyUtils 1.0.0
	 * 
	 * @see #ItemBuilder(ItemStack, boolean)
	 */
	public ItemBuilder(@NotNull ItemStack stack) {
		this(stack, false);
	}

	@NotNull
	@Override
	public ItemBuilder clone() {
		return new ItemBuilder(item);
	}

	/**
	 * Gets the {@link ItemMeta} being used by this {@link ItemBuilder}.
	 * 
	 * @return The {@link ItemMeta} being used, can be modified.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemMeta meta() {
		return meta;
	}

	/**
	 * Applies {@link #meta()} to the {@link ItemStack} being
	 * used by this {@link ItemBuilder} and returns it.
	 * 
	 * @return An {@link ItemStack} with {@link #meta()} applied to it.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemStack build() {
		item.setItemMeta(meta());
		return item;
	}

	/*
	 * ITEMSTACK MODIFIERS
	 */

	// Amount //

	/**
	 * Sets the amount of items on the {@link ItemStack} being
	 * used by this {@link ItemBuilder}. If the amount is higher
	 * than the max stack size, the max stack size will be used,
	 * setting the amount on zero or any negative number will result
	 * in the item being removed (Not the {@link ItemBuilder}).
	 * 
	 * @param amount the new amount of items on the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setAmount(int amount) {
		if (amount > item.getMaxStackSize())
			item.setAmount(item.getMaxStackSize());
		else
			item.setAmount(amount);
		return this;
	}

	/**
	 * Adds {@code amount} to the current amount the {@link ItemStack} being
	 * used by this {@link ItemBuilder} has and calls {@link #setAmount(int)}
	 * with said amount.
	 * 
	 * @param amount the amount of items to add to the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder addAmount(int amount) {
		setAmount(item.getAmount() + amount);
		return this;
	}

	/**
	 * Removes {@code amount} from the current amount the {@link ItemStack} being
	 * used by this {@link ItemBuilder} has and calls {@link #setAmount(int)}
	 * with said amount.
	 * 
	 * @param amount the amount of items to remove from the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder removeAmount(int amount) {
		setAmount(item.getAmount() - amount);
		return this;
	}

	// Data //

	/**
	 * @deprecated All usage of {@link MaterialData} is deprecated and subject to removal. Use {@link BlockData}.
	 * 
	 * @param data the new material data for this item
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	@Deprecated
	public ItemBuilder setData(@Nullable MaterialData data) {
		item.setData(data);
		return this;
	}

	// META MODIFIERS //

	// Enchantment //

	/**
	 * Adds the specified {@link Enchantment} to the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}
	 * <p>
	 * If the meta contained the given enchantment (at any level), it will be replaced.
	 *
	 * @param ench The {@link Enchantment} to add.
	 * @param level The level of the {@link Enchantment} to add.
	 * @param ignoreLevelRestriction Whether to ignore Minecraft's enchantment level
	 * restrictions or not.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws IllegalArgumentException if {@code ench} is null, or not applicable.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder addEnchant(@NotNull Enchantment ench, int level, boolean ignoreLevelRestriction) {
		meta.addEnchant(ench, level, ignoreLevelRestriction);
		return this;
	}

	/**
	 * Removes the specified {@link Enchantment} to the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}
	 * 
	 * @param enchantments the {@link Enchantment Enchantments} to remove.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 * 
	 * @see #clearEnchants()
	 */
	@NotNull
	public ItemBuilder removeEnchantment(@NotNull Enchantment... enchantments) {
		for (Enchantment ench : enchantments)
			meta.removeEnchant(ench);
		return this;
	}

	/**
	 * Clears the {@link Enchantment Enchantments} of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 * 
	 * @see #removeEnchantment(Enchantment...)
	 */
	@NotNull
	public ItemBuilder clearEnchants() {
		final Set<Enchantment> enchants = new HashSet<>(meta.getEnchants().keySet());
		enchants.forEach(meta::removeEnchant);
		return this;
	}

	// Names //

	/**
	 * Sets the localized name of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @param name the name to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setLocalizedName(@Nullable String name) {
		meta.setLocalizedName(name);
		return this;
	}

	/**
	 * Sets the display name of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}, applying colors to
	 * it by using {@link MCStrings#applyColor(String)}.
	 * 
	 * @param name the name to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setDisplayName(@Nullable String name) {
		meta.setDisplayName(MCStrings.applyColor(name));
		return this;
	}

	// Lore //

	/**
	 * Sets the lore of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}, applying colors to
	 * it by using {@link MCStrings#applyColor(List)}.
	 * 
	 * @param lore the lore to set, if null, the lore will be removed.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 * 
	 * @see #setLore(List)
	 */
	@NotNull
	public ItemBuilder setLore(@Nullable List<String> lore) {
		meta.setLore(SkyCollections.map(lore, MCStrings::applyColor));
		return this;
	}

	/**
	 * Sets the lore of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}, applying colors to
	 * it by using {@link MCStrings#applyColor(List)}.
	 * 
	 * @param lore the lore to set, if null, the lore will be removed.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setLore(@Nullable String... lore) {
		return setLore(Arrays.asList(lore));
	}

	/**
	 * Clears the lore of the {@link ItemMeta} being used
	 * by this {@link ItemBuilder}. This method exists becasue
	 * calling either {@link #setLore(String...)} or {@link #setLore(List)}
	 * with a {@code null} parameter is ambiguous.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder clearLore() {
		meta.setLore(null);
		return this;
	}

	/**
	 * Adds the specified {@code lines} to the lore of the
	 * {@link ItemMeta} being used by this {@link ItemBuilder},
	 * applying colors to them by using {@link MCStrings#applyColor(String)}.
	 * 
	 * @param lines the lines to add, if null, nothing will be done.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	public ItemBuilder addLore(@Nullable List<String> lines) {
		if (lines == null || lines.isEmpty())
			return this;
		final List<String> lore = meta.getLore() == null ? new ArrayList<>(lines.size()) : meta.getLore();
		for (String line : lines)
			lore.add(MCStrings.applyColor(line));
		meta.setLore(lore);
		return this;
	}

	/**
	 * Adds the specified {@code lines} to the lore of the
	 * {@link ItemMeta} being used by this {@link ItemBuilder},
	 * applying colors to them by using {@link MCStrings#applyColor(String)}.
	 * 
	 * @param lines the lines to add, if null, nothing will be done.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder addLore(@Nullable String... lines) {
		return lines == null ? this : addLore(Arrays.asList(lines));
	}

	// Durability //

	/**
	 * Sets the damage of the {@link ItemMeta} being used by
	 * this {@link ItemBuilder}, this method does nothing if
	 * said {@link ItemMeta} isn't an instance of {@link Damageable}.
	 * 
	 * @param damage the item damage to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setDamage(short damage) {
		if (meta instanceof Damageable)
			((Damageable)meta).setDamage(damage);
		return this;
	}

	/**
	 * Sets the durability of the {@link ItemMeta} being used by
	 * this {@link ItemBuilder}, this method does nothing if
	 * said {@link ItemMeta} isn't an instance of {@link Damageable}.
	 * 
	 * @param durability the item durability to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setDurability(short durability) {
		if (meta instanceof Damageable)
			((Damageable)meta).setDamage(item.getType().getMaxDurability() - durability);
		return this;
	}

	/**
	 * Sets the unbreakable tag of the {@link ItemMeta} being used by
	 * this {@link ItemBuilder}. An unbreakable item will not lose durability.
	 * 
	 * @param unbreakable true set the item unbreakable, false to disable said property.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		meta.setUnbreakable(unbreakable);
		return this;
	}

	// Persistent data //

	/**
	 * Stores a metadata value on the {@link PersistentDataHolder} instance
	 * of the {@link ItemMeta} being used by this {@link ItemBuilder}.
	 * <p>
	 * This API cannot be used to manipulate Minecraft data,
	 * as the values will be stored using your {@link NamespacedKey}.
	 * This method will override any existing value the {@link PersistentDataHolder}
	 * may have stored under the provided key.
	 * 
	 * @param <T> the generic java type of the tag value.
	 * @param <Z> the generic type of the object to store.
	 * @param key the key this value will be stored under.
	 * @param type the type this tag uses.
	 * @param value the value stored in the tag.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if the {@code key} is null.
	 * @throws NullPointerException if the {@code type} is null.
	 * @throws NullPointerException if the {@code value} is null. Removing a tag should
	 * be done using {@link #removePersistentData(NamespacedKey)}.
	 * @throws IllegalArgumentException if no suitable adapter is found for
	 * the {@link PersistentDataType#getPrimitiveType()}
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public <T, Z> ItemBuilder setPersistentData(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
		meta.getPersistentDataContainer().set(key, type, value);
		return this;
	}

	/**
	 * Removes a custom {@code key} from the {@link PersistentDataHolder} instance
	 * of the {@link ItemMeta} being used by this {@link ItemBuilder}.
	 * 
	 * @param key the key to remove.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if the provided {@code key} is null.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder removePersistentData(@NotNull NamespacedKey key) {
		meta.getPersistentDataContainer().remove(key);
		return this;
	}

	// Attribute modifiers //

	/**
	 * Add an Attribute and it's Modifier.
	 * <p>
	 * {@link AttributeModifier AttributeModifiers} support {@link EquipmentSlot EquipmentSlots}.
	 * If not set, the {@link AttributeModifier} will be active in ALL slots.
	 * <p>
	 * Two {@link AttributeModifier AttributeModifiers} that have the same {@link UUID}
	 * cannot exist on the same Attribute.
	 *
	 * @param attribute the {@link Attribute} to modify
	 * @param modifier the {@link AttributeModifier} specifying the modification
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if either {@code attribute} or {@code modifier} are null.
	 * @throws IllegalArgumentException if {@code modifier} already exists.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
		meta.addAttributeModifier(attribute, modifier);
		return this;
	}

	/**
	 * Set all {@link AttributeModifier AttributeModifiers} and their
	 * {@link AttributeModifier AttributeModifiers}. To clear all currently
	 * set Attributes and AttributeModifiers use null or an empty {@link Multimap}.
	 * If not null nor empty, this will filter all entries that are not-null
	 * and add them to the ItemStack.
	 *
	 * @param attributeModifiers the new {@link Multimap} containing the {@link Attribute Attributes}
	 * and their {@link AttributeModifier AttributeModifiers}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
		meta.setAttributeModifiers(attributeModifiers);
		return this;
	}

	/**
	 * Remove a specific {@link Attribute} and {@link AttributeModifier}.
	 * AttributeModifiers are matched according to their {@link UUID}.
	 * 
	 * @param attribute the {@link Attribute} to remove.
	 * @param modifier the {@link AttributeModifier} to remove.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if either {@code attribute} or {@code modifier} are null.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
		meta.removeAttributeModifier(attribute, modifier);
		return this;
	}

	/**
	 * Remove all {@link Attribute Attributes} associated with the given
	 * {@link AttributeModifier AttributeModifiers}.
	 * 
	 * @param attribute the {@link Attribute} to remove.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder removeAttributeModifier(@NotNull Attribute attribute) {
		meta.removeAttributeModifier(attribute);
		return this;
	}

	/**
	 * Remove all {@link Attribute Attributes} and {@link AttributeModifier AttributeModifiers}
	 * for a given {@link EquipmentSlot}.
	 * <p>
	 * If the given {@link EquipmentSlot} is null, this will remove all
	 * {@link AttributeModifier AttributeModifiers} that do not have an EquipmentSlot set.
	 * 
	 * @param slot the {@link EquipmentSlot} to clear all Attributes and
	 * their modifiers for.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder removeAttributeModifier(@NotNull EquipmentSlot slot) {
		meta.removeAttributeModifier(slot);
		return this;
	}

	// Item flags //

	/**
	 * Adds a specific set of {@link ItemFlag ItemFlags} which should be ignored by the client when
	 * rendering an {@link ItemStack}. This Method does silently ignore double removed {@link ItemFlag ItemFlags}.
	 * 
	 * @param flags {@link ItemFlag ItemFlags} which should be removed
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder addItemFlags(@NotNull ItemFlag... flags) {
		meta.addItemFlags(flags);
		return this;
	}

	/**
	 * Remove a specific set of {@link ItemFlag ItemFlags}. This tells the client it
	 * should render the item again. This Method does silently ignore double removed {@link ItemFlag ItemFlags}.
	 * 
	 * @param flags {@link ItemFlag ItemFlags} which should be removed
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder removeitemFlags(@NotNull ItemFlag... flags) {
		meta.removeItemFlags(flags);
		return this;
	}

	// Model data //

	/**
	 * Sets the custom model data.
	 * <p>
	 * CustomModelData is an integer that may be associated client side with a
	 * custom item model.
	 * 
	 * @param data the data to set, or {@code null} to clear.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since SkyUtils 1.0.0
	 */
	@NotNull
	public ItemBuilder setCustomModelData(@Nullable Integer data) {
		meta.setCustomModelData(data);
		return this;
	}
}
