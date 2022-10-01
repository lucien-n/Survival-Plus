package me.scaffus.survivalplus.menus;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class BankMenu implements Listener {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;
    private Helper helper;
    private String inventoryName = "§lBanque";

    public BankMenu(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.helper = plugin.helper;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(inventoryName)) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        // 11: withdraw | 13: balance | 15: deposit
        if (slot == 11) {
            int diamoundAmount = data.getPlayerBalance(p.getUniqueId());
            p.getInventory().addItem(new ItemStack(Material.DIAMOND, diamoundAmount));
            data.setPlayerBalance(p.getUniqueId(), 0);
            p.sendMessage(plugin.getConfig().getString("bank.withdraw.successful").replace("%amount%", String.valueOf(diamoundAmount)));
        }

        if (slot == 15) {
            int diamondAmount = helper.getAmountOfItemInventory(p.getInventory(), Material.DIAMOND);
            data.addPlayerBalance(p.getUniqueId(), diamondAmount);
            helper.removeAmountOfItemFromInventory(p.getInventory(), Material.DIAMOND, diamondAmount);
            p.sendMessage(plugin.getConfig().getString("bank.deposit.successful").replace("%amount%", String.valueOf(diamondAmount)));
        }
    }


    public Inventory createMenu(Player p) {
        ItemStack backgroundItem = helper.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), "", "");
        Inventory inventory = helper.createInventoryWithBackground(p, "§lBanque", 36, backgroundItem);

        inventory.setItem(11, helper.getItem(
                new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), "§6§lRécupérer", "§eRécupérer une somme de ton compte."
        ));

        inventory.setItem(13, helper.getHead(p,
                "§eTu possèdes §6§l%amount%$§e sur ton compte.".replace("%amount%", String.valueOf(data.getPlayerBalance(p.getUniqueId())))));

        inventory.setItem(15, helper.getItem(
                new ItemStack(Material.LIME_STAINED_GLASS_PANE), "§6§lDéposer", "§eDéposer une somme sur ton compte."
        ));

        return inventory;
    }
}
