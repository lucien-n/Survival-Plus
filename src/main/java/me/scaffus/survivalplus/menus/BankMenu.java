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

import java.util.UUID;

public class BankMenu implements Listener {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;
    private final Helper helper;
    private final String bankInventoryName = "§lBanque";
    private final String amountInventoryName = "§6§lMontant";
    private final String buyTokenInventoryName = "§6§lAchat De Jeton";
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
            UUID uuid = p.getUniqueId();
            int slot = event.getSlot();

            if (!survivalData.canPlayerClick(uuid)) return;

            // 11: withdraw | 13: balance | 15: deposit | 22: buy tokens
            if (slot == 11) {
                p.openInventory(createAmountMenu(p));
                mode = "withdraw";
            }

            if (slot == 15) {
                p.openInventory(createAmountMenu(p));
                mode = "deposit";
            }

            if (slot == 22) {
                p.openInventory(createBuyTokenMenu(p));
            }

            survivalData.setPlayerLastClicked(uuid);
            if (slot == event.getInventory().getSize() - 1) p.closeInventory();
        }

        if (event.getView().getTitle().equals(buyTokenInventoryName)) {
            event.setCancelled(true);

            Player p = (Player) event.getWhoClicked();
            UUID uuid = p.getUniqueId();

            int slot = event.getSlot();
            if (!survivalData.canPlayerClick(uuid)) return;

            if (slot == 13) {
                if (!(survivalData.getPlayerBalance(uuid) > 32)) {
                    p.sendMessage("§eTu n'as pas assez de §b&ndiamants");
                    return;
                }
                survivalData.incrementPlayerTokens(uuid, 1);
                survivalData.incrementPlayerBalance(uuid, -32);
                p.sendMessage("§eTu as acheté §61 jeton§e pour §b32&ndiamants");
            }

            survivalData.setPlayerLastClicked(uuid);
            if (slot == event.getInventory().getSize() - 9) p.openInventory(createBankMenu(p));
            if (slot == event.getInventory().getSize() - 1) p.closeInventory();
        }

        if (event.getView().getTitle().equals(amountInventoryName)) {
            event.setCancelled(true);

            Player p = (Player) event.getWhoClicked();
            UUID uuid = p.getUniqueId();

            int slot = event.getSlot();
            if (!survivalData.canPlayerClick(uuid)) return;

            if (slot == 10) amount = amount - 64;
            else if (slot == 11) amount = amount - 8;
            else if (slot == 12) amount = amount - 1;
            else if (slot == 13) {
                if (mode.equals("withdraw")) amount = survivalData.getPlayerBalance(uuid);
                else if (mode.equals("deposit"))
                    amount = helper.getAmountOfItemInventory(p.getInventory(), Material.DIAMOND);
            } else if (slot == 14) amount = amount + 1;
            else if (slot == 15) amount = amount + 8;
            else if (slot == 16) amount = amount + 64;
            else if (slot == 22) {
                if (mode.equals("withdraw")) {
                    int playerBalance = survivalData.getPlayerBalance(uuid);
                    if (playerBalance >= amount) {
                        survivalData.incrementPlayerBalance(uuid, -amount);
                        p.getInventory().addItem(new ItemStack(Material.DIAMOND, amount));
                        p.sendMessage(plugin.getConfig().getString("bank.withdraw.successful").replace("%amount%", String.valueOf(amount)));
                    } else {
                        p.sendMessage(plugin.getConfig().getString("bank.withdraw.not_enough").replace("%amount%", String.valueOf(amount)));
                    }
                    return;
                } else if (mode.equals("deposit")) {
                    int diamondInInv = helper.getAmountOfItemInventory(p.getInventory(), Material.DIAMOND);
                    if (diamondInInv >= amount) {
                        survivalData.incrementPlayerBalance(uuid, amount);
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
            survivalData.setPlayerLastClicked(uuid);
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

        inventory.setItem(22, helper.getItem(
                new ItemStack(Material.SUNFLOWER), "§6§lAchat De Jeton", "§eAcheter des jetons"
        ));

        return inventory;
    }

    public Inventory createBuyTokenMenu(Player p) {
        Inventory inventory = helper.createInventoryWithBackground(p, buyTokenInventoryName, 27, true);
        inventory.setItem(13, helper.getItem(new ItemStack(Material.GOLD_BLOCK), "§6§lAcheter", "§61§e jeton pour §b32diamants"));

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
