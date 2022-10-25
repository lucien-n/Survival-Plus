package me.scaffus.survivalplus.commands;

import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.menus.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {
    private final SurvivalPlus plugin;
    private final MainMenu mainMenu;

    public MenuCommand(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.mainMenu = new MainMenu(plugin);
        plugin.getCommand("menu").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        ((Player) sender).openInventory(mainMenu.createMenu((Player) sender));
        return true;
    }
}
