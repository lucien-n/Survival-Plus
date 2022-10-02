package me.scaffus.survivalplus.commands;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.BankMenu;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankCommand implements CommandExecutor {
    private SurvivalPlus plugin;

    public BankCommand(SurvivalPlus plugin) {
        this.plugin = plugin;
        plugin.getCommand("banque").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        BankMenu menu = new BankMenu(plugin);
        p.openInventory(menu.createBankMenu(p));
        return true;

//        if (args.length >= 2) {
//            if (args[0].equalsIgnoreCase("deposer")) {
//                int amount = Integer.parseInt(args[1]);
//                if (p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), amount)) {
//                    if (data.addPlayerBalance(p.getUniqueId(), amount)) {
//                        int diamondsInInv = getAmountOfItemInventory(p.getInventory(), Material.DIAMOND);
//                        if (removeAmountOfItemFromInventory(p.getInventory(), Material.DIAMOND, amount)) {
//                            p.sendMessage(plugin.getConfig().getString("bank.deposit.successful").replace("%amount%", String.valueOf(amount)));
//                            return true;
//                        } else {
//                            p.sendMessage(plugin.getConfig().getString("bank.deposit.failed"));
//                        }
//                    } else {
//                        p.sendMessage(plugin.getConfig().getString("bank.deposit.failed"));
//                        return true;
//                    }
//                } else {
//                    p.sendMessage(plugin.getConfig().getString("bank.deposit.not_enough").replace("%amount%", String.valueOf(amount)));
//                    return true;
//                }
//            } else if (args[0].equalsIgnoreCase("recuperer")) {
//                int amount = Integer.parseInt(args[1]);
//                int playerBalance = data.getPlayerBalance(p.getUniqueId());
//                if (playerBalance >= amount) {
//                    data.setPlayerBalance(p.getUniqueId(), playerBalance - amount);
//                    p.getInventory().addItem(new ItemStack(Material.DIAMOND, amount));
//                    p.sendMessage(plugin.getConfig().getString("bank.withdraw.successful").replace("%amount%", String.valueOf(amount)));
//                    return true;
//                } else {
//                    p.sendMessage(plugin.getConfig().getString("bank.withdraw.not_enough").replace("%amount%", String.valueOf(amount)));
//                    return true;
//                }
//            }
//        } else {
//            p.sendMessage(plugin.getConfig().getString("bank.balance").replace("%amount%", String.valueOf(data.getPlayerBalance(p.getUniqueId()))));
//            return true;
//        }
    }


}
