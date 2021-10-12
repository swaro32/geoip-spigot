package me.swaro32.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class About implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§aGeoIP by §fswaro32" +
                "\n§aVersion: §f1.0" +
                "\n§aMe on Spigot: §fhttps://www.spigotmc.org/members/swaro32.943238/" +
                "\n§aSource code: §fhttps://github.com/swaro32/geoip-spigot");
        return true;
    }
}
