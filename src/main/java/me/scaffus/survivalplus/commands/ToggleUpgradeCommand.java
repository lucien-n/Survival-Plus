package me.scaffus.survivalplus.commands;

import me.scaffus.survivalplus.SurvivalData;
import me.scaffus.survivalplus.SurvivalPlus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ToggleUpgradeCommand implements CommandExecutor {
    private final SurvivalPlus plugin;
    private final SurvivalData survivalData;

    public ToggleUpgradeCommand(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.survivalData = plugin.survivalData;
        plugin.getCommand("toggle_upgrade").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();
        if (args.length == 1) {
            Integer upgradeLevel = survivalData.getPlayerUpgrade(uuid, args[0]);
//            Player has upgrade and it's ON
            if (upgradeLevel > 0) {
                survivalData.setPlayerUpgrade(uuid, args[0], -upgradeLevel);
                p.sendMessage(plugin.getConfig().getString("skills.upgrade_disabled").replace("%upgrade%", args[0]));
            }
//            Player has upgrade and it's OFF
            else if (upgradeLevel < 0) {
                survivalData.setPlayerUpgrade(uuid, args[0], -upgradeLevel);
                p.sendMessage(plugin.getConfig().getString("skills.upgrade_enabled").replace("%upgrade%", args[0]));
            }
//            Player doesn't have upgrade
            else {
                p.sendMessage(plugin.getConfig().getString("skills.upgrade_not_possessed"));
            }
        }

        return false;
    }
}
