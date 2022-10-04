package me.scaffus.survivalplus;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    private SurvivalData playersData;

    public void removeAmountOfItemFromInventory(Inventory inventory, Material material, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item != null && material == item.getType()) {
                int newAmount = item.getAmount() - amount;
                if (newAmount > 0) {
                    item.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    public int getAmountOfItemInventory(Inventory inventory, Material material) {
        int amount = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == material) {
                amount += item.getAmount();
            }
        }
        return amount;
    }

    public ItemStack getItem(ItemStack item, String name, String... lore) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> lores = new ArrayList<>();
        for (String s : lore) {
            lores.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(lores);

        item.setItemMeta(meta);
        return item;
    }

    public Inventory createInventoryWithBackground(Player p, String inventoryName, int inventorySize, ItemStack backgroundItem, Boolean backButton) {
        Inventory inventory = Bukkit.createInventory(p, inventorySize, inventoryName);
        for (int i = 0; i < inventorySize; i++) {
            inventory.setItem(i, backgroundItem);
        }
        if (backButton) inventory.setItem(inventorySize - 9, getItem(new ItemStack(Material.ARROW), "§fRetour", ""));
        inventory.setItem(inventorySize - 1, getItem(new ItemStack(Material.BARRIER), "§cFermer", ""));
        return inventory;
    }

    public ItemStack getHead(Player p, String lore) {
        ItemStack skullItem = getItem(
                new ItemStack(Material.PLAYER_HEAD), "§6§l" + p.getDisplayName(), lore);
        SkullMeta playerSkull = (SkullMeta) skullItem.getItemMeta();
        playerSkull.setOwningPlayer(p);
        skullItem.setItemMeta(playerSkull);
        return skullItem;
    }

    public Double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public String upgradeBoughtMessage(String message, String upgrade, int cost) {
        return message.replace("%upgrade%", upgrade).replace("%cost%", String.valueOf(cost));
    }

    public void sendNotEnoughTokensMessage(Player p, String message) {
        p.sendMessage(message);
    }

    public void sendActionBar(Player p, String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public void applyDamage(ItemStack item, Integer damageToDeal) {
        ItemMeta meta = item.getItemMeta();
        Damageable damageable = (Damageable) meta;
        damageable.setDamage(damageable.getDamage() - damageToDeal);
        item.setItemMeta(meta);
    }
}
