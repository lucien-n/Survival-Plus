package me.scaffus.survivalplus.menus;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BankMenu implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final Helper helper;
    private final String bankInventoryName = "§lBanque";
    private final String amountInventoryName = "§6§lMontant";
    private String mode = "none";
    private int amount = 0;

    public BankMenu(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        this.helper = plugin.helper;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(bankInventoryName)) {
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();
            int slot = event.getSlot();

            // 11: withdraw | 13: balance | 15: deposit
            if (slot == 11) {
                p.openInventory(createAmountMenu(p));
                mode = "withdraw";
            }

            if (slot == 15) {
                p.openInventory(createAmountMenu(p));
                mode = "deposit";
            }
            if (slot == event.getInventory().getSize() - 1) p.closeInventory();
        }

        if (event.getView().getTitle().equals(amountInventoryName)) {
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();
            int slot = event.getSlot();

            if (slot == 10) amount = amount - 64;
            else if (slot == 11) amount = amount - 8;
            else if (slot == 12) amount = amount - 1;
            else if (slot == 13) {
                if (mode.equals("withdraw")) amount = survivalData.getPlayerBalance(p.getUniqueId());
                else if (mode.equals("deposit"))
                    amount = helper.getAmountOfItemInventory(p.getInventory(), Material.DIAMOND);
            } else if (slot == 14) amount = amount + 1;
            else if (slot == 15) amount = amount + 8;
            else if (slot == 16) amount = amount + 64;
            else if (slot == 22) {
                if (mode.equals("withdraw")) {
                    int playerBalance = survivalData.getPlayerBalance(p.getUniqueId());
                    if (playerBalance >= amount) {
                        survivalData.incrementPlayerBalance(p.getUniqueId(), -amount);
                        p.getInventory().addItem(new ItemStack(Material.DIAMOND, amount));
                        p.sendMessage(plugin.getConfig().getString("bank.withdraw.successful").replace("%amount%", String.valueOf(amount)));
                    } else {
                        p.sendMessage(plugin.getConfig().getString("bank.withdraw.not_enough").replace("%amount%", String.valueOf(amount)));
                    }
                    return;
                } else if (mode.equals("deposit")) {
                    int diamondInInv = helper.getAmountOfItemInventory(p.getInventory(), Material.DIAMOND);
                    if (diamondInInv >= amount) {
                        survivalData.incrementPlayerTokens(p.getUniqueId(), amount);
                        helper.removeAmountOfItemFromInventory(p.getInventory(), Material.DIAMOND, amount);
                        p.sendMessage(plugin.getConfig().getString("bank.deposit.successful").replace("%amount%", String.valueOf(amount)));
                    } else {
                        p.sendMessage(plugin.getConfig().getString("bank.deposit.not_enough").replace("%amount%", String.valueOf(amount)));
                    }
                    return;
                }
            } else if (slot == event.getInventory().getSize() - 1) p.closeInventory();
            else if (slot == event.getInventory().getSize() - 9) p.openInventory(createBankMenu(p));

            event.getInventory().setItem(4, helper.getItem(new ItemStack(Material.GOLD_BLOCK), "§6§lMontant", "§e" + amount));
        }

    }


    public Inventory createBankMenu(Player p) {
        Inventory inventory = helper.createInventoryWithBackground(p, bankInventoryName, 27, false);

        inventory.setItem(11, helper.getItem(
                new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "§6§lRécupérer", "§eRécupérer une somme de ton compte."
        ));

        inventory.setItem(13, helper.getHead(p,
                "§eTu possèdes §6§l%amount%$§e sur ton compte.".replace("%amount%", String.valueOf(
                        survivalData.getPlayerBalance(p.getUniqueId())))));

        inventory.setItem(15, helper.getItem(
                new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§6§lDéposer", "§eDéposer une somme sur ton compte."
        ));

        return inventory;
    }

    public Inventory createAmountMenu(Player p) {
        Inventory inventory = helper.createInventoryWithBackground(p, amountInventoryName, 27, true);

        inventory.setItem(4, helper.getItem(new ItemStack(Material.GOLD_BLOCK), "§6§lMontant", "§e0"));

        inventory.setItem(10, helper.getItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c-64"));
        inventory.setItem(11, helper.getItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c-8"));
        inventory.setItem(12, helper.getItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c-1"));
        inventory.setItem(13, helper.getItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§aTout"));
        inventory.setItem(14, helper.getItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§a+1"));
        inventory.setItem(15, helper.getItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§a+8"));
        inventory.setItem(16, helper.getItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§a+64"));

        inventory.setItem(22, helper.getItem(new ItemStack(Material.LIME_DYE), "§a§lValider"));

        return inventory;
    }
}
