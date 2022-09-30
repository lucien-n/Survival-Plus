package me.scaffus.survivalplus.commands;

import me.scaffus.survivalplus.SurvivalPlus;
import me.scaffus.survivalplus.sql.DatabaseGetterSetter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {
    private SurvivalPlus plugin;
    private DatabaseGetterSetter data;

    public LevelCommand(SurvivalPlus plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
        plugin.getCommand("level").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;
        Double farmingLevel = data.getPlayerSkillLevel(p.getUniqueId(), "farming");
        Double miningLevel = data.getPlayerSkillLevel(p.getUniqueId(), "mining");
        Double combatLevel = data.getPlayerSkillLevel(p.getUniqueId(), "combat");
        Double runningLevel = data.getPlayerSkillLevel(p.getUniqueId(), "running");
        Double deathLevel = data.getPlayerSkillLevel(p.getUniqueId(), "death");
        Double archeryLevel = data.getPlayerSkillLevel(p.getUniqueId(), "archery");
        Double swimmingLevel = data.getPlayerSkillLevel(p.getUniqueId(), "swimming");
        Double flyingLevel = data.getPlayerSkillLevel(p.getUniqueId(), "flying");
        p.sendMessage("§eVoici tes points:");
        p.sendMessage("  §e- §6§n" + String.valueOf(farmingLevel) + "§e fermier");
        p.sendMessage("  §e- §6§n" + String.valueOf(miningLevel) + "§e mineur");
        p.sendMessage("  §e- §6§n" + String.valueOf(combatLevel) + "§e combattant");
        p.sendMessage("  §e- §6§n" + String.valueOf(runningLevel) + "§e coureur");
        p.sendMessage("  §e- §6§n" + String.valueOf(deathLevel) + "§e mourant");
        p.sendMessage("  §e- §6§n" + String.valueOf(archeryLevel) + "§e archer");
        p.sendMessage("  §e- §6§n" + String.valueOf(swimmingLevel) + "§e nageur");
        p.sendMessage("  §e- §6§n" + String.valueOf(flyingLevel) + "§e ange");

        return false;
    }
}
