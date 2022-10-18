package me.scaffus.survivalplus.commands;

import me.scaffus.survivalplus.Helper;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InfoStickCommand implements CommandExecutor {
    private SurvivalPlus plugin;
    private Helper helper;
    private ItemStack infoStick;

    public InfoStickCommand(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.helper = plugin.helper;
        plugin.getCommand("infostick").setExecutor(this);
        infoStick = helper.getItem(new ItemStack(Material.STICK), "§6§lInfoStick", "§eClique droit sur un bloc pour récupérer ses informations");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        if (!p.hasPermission("survplus.infostick")) {
            helper.getMessage("no_permission");
            return true;
        }

        p.getInventory().addItem(infoStick);

        return false;
    }
}
