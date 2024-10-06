package dev.furq.shush.commands;

import dev.furq.shush.Shush;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShushCommand implements CommandExecutor {
    private final Shush plugin;

    public ShushCommand(Shush plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("shush")) {
            if (args.length == 0) {
                sender.sendMessage(plugin.colorize(getMessage("usage")));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("shush.reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage(plugin.colorize(getMessage("config_reloaded")));
                } else {
                    sender.sendMessage(plugin.colorize(getMessage("no_permission")));
                }
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.colorize(getMessage("only_players")));
                return true;
            }

            Player targetPlayer = sender.getServer().getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(plugin.colorize(getMessage("player_not_found")));
                return true;
            }

            toggleMute((Player) sender, targetPlayer);
            return true;
        }
        return false;
    }

    private void toggleMute(Player muter, Player muted) {
        if (muted.hasPermission("shush.bypass")) {
            muter.sendMessage(plugin.colorize(getMessage("cannot_mute_bypass")));
            return;
        }

        String muterUuid = muter.getUniqueId().toString();
        String mutedUuid = muted.getUniqueId().toString();

        try (PreparedStatement checkStatement = plugin.getConnection().prepareStatement("SELECT * FROM muted_players WHERE muter_uuid = ? AND muted_uuid = ?")) {
            checkStatement.setString(1, muterUuid);
            checkStatement.setString(2, mutedUuid);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                try (PreparedStatement deleteStatement = plugin.getConnection().prepareStatement("DELETE FROM muted_players WHERE muter_uuid = ? AND muted_uuid = ?")) {
                    deleteStatement.setString(1, muterUuid);
                    deleteStatement.setString(2, mutedUuid);
                    deleteStatement.executeUpdate();
                    muter.sendMessage(plugin.colorize(getMessage("unmuted").replace("{player}", muted.getName())));
                }
            } else {
                try (PreparedStatement insertStatement = plugin.getConnection().prepareStatement("INSERT INTO muted_players (muter_uuid, muted_uuid) VALUES (?, ?)")) {
                    insertStatement.setString(1, muterUuid);
                    insertStatement.setString(2, mutedUuid);
                    insertStatement.executeUpdate();
                    muter.sendMessage(plugin.colorize(getMessage("muted").replace("{player}", muted.getName())));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error toggling mute status: " + e.getMessage());
        }
    }

    private String getMessage(String key) {
        String prefix = plugin.getConfig().getString("prefix", "&7[&bShush&7]");
        return prefix + " " + plugin.getConfig().getString("messages." + key, "");
    }
}