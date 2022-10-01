package me.scaffus.survivalplus.commands;

import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.SkillMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillCommand implements CommandExecutor {
    private SurvivalPlus plugin;

    public SkillCommand(SurvivalPlus plugin) {
        this.plugin = plugin;
        plugin.getCommand("skills").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        SkillMenu menu = new SkillMenu(plugin);
        p.openInventory(menu.createSkillMenu(p));
        return true;
    }
}
