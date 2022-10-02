package me.scaffus.survivalplus;

import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    private DatabaseGetterSetter data;

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

    public boolean buyUpgrade(Player p, String upgrade, int upgradeCost) {
        if (!(data.getPlayerTokens(p.getUniqueId()) > upgradeCost))
            return false;

        data.setPlayerUpgrade(p.getUniqueId(), upgrade, true);
        data.incrementPlayerTokens(p.getUniqueId(), -upgradeCost);
        return true;
    }

    public void sendUpgradeBoughtMessage(Player p, String upgrade, int cost) {
        p.sendMessage(Bukkit.getPluginManager().getPlugin("SurvivalPlus").getConfig().getString("skills.upgrade_bought").replace("%upgrade%", upgrade).replace("%cost%", String.valueOf(cost)));
    }

    public void sendNotEnoughTokensMessage(Player p) {
        p.sendMessage(Bukkit.getPluginManager().getPlugin("SurvivalPlus").getConfig().getString("skills.not_enough"));
    }
}
