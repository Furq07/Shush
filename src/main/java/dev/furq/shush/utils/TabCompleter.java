package dev.furq.shush.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("shush")) {
            if (args.length == 1) {
                List<String> options = new ArrayList<>();
                options.add("reload");
                options.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
                return options;
            }
        }
        return null;
    }
}