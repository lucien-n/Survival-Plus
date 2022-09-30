package me.scaffus.survivalplus.commands;

import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BankCommand implements CommandExecutor {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;

    public BankCommand(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        plugin.getCommand("banque").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("deposer")) {
                int amount = Integer.parseInt(args[1]);
                if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), amount)) {
                    if (data.addPlayerBalance(p.getUniqueId(), amount)) {
                        int diamondsInInv = getAmountOfItemInventory(p.getInventory(), Material.DIAMOND);
                        if (removeAmountOfItemFromInventory(p.getInventory(), Material.DIAMOND, amount)) {
                            p.sendMessage(plugin.getConfig().getString("bank.deposit.successful").replace("%amount%", String.valueOf(amount)));
                            return true;
                        } else {
                            p.sendMessage(plugin.getConfig().getString("bank.deposit.failed"));
                        }
                    } else {
                        p.sendMessage(plugin.getConfig().getString("bank.deposit.failed"));
                        return true;
                    }
                } else {
                    p.sendMessage(plugin.getConfig().getString("bank.deposit.not_enough").replace("%amount%", String.valueOf(amount)));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("recuperer")) {
                int amount = Integer.parseInt(args[1]);
                int playerBalance = data.getPlayerBalance(p.getUniqueId());
                if (playerBalance >= amount) {
                    data.setPlayerBalance(p.getUniqueId(), playerBalance - amount);
                    p.getInventory().addItem(new ItemStack(Material.DIAMOND, amount));
                    p.sendMessage(plugin.getConfig().getString("bank.withdraw.successful").replace("%amount%", String.valueOf(amount)));
                    return true;
                } else {
                    p.sendMessage(plugin.getConfig().getString("bank.withdraw.not_enough").replace("%amount%", String.valueOf(amount)));
                    return true;
                }
            }
        } else {
            p.sendMessage(plugin.getConfig().getString("bank.balance").replace("%amount%", String.valueOf(data.getPlayerBalance(p.getUniqueId()))));
            return true;
        }

        return false;
    }

    public boolean removeAmountOfItemFromInventory(Inventory inventory, Material material, int amount) {
        if (amount <= 0) return false;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null) return false;
            if (material == item.getType()) {
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

        return true;
    }

    public int getAmountOfItemInventory(Inventory inventory, Material material) {
        int amount = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null) return 0;
            if (item.getType() == material) {
                amount += item.getAmount();
            }
        }
        return amount;
    }
}
