package dev.furq.shush.listeners;

import dev.furq.shush.Shush;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandListener implements Listener {
    private final Shush plugin;

    public CommandListener(Shush plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        String command = args[0].toLowerCase();

        if (command.equals("/msg") || command.equals("/tell") || command.equals("/w")) {
            Player sender = event.getPlayer();
            
            if (sender.hasPermission("shush.bypass")) {
                return;
            }
            
            if (args.length < 3) {
                return;
            }

            Player recipient = plugin.getServer().getPlayer(args[1]);

            if (recipient == null) {
                return;
            }

            if (isMuted(recipient.getUniqueId().toString(), sender.getUniqueId().toString())) {
                event.setCancelled(true);
                sender.sendMessage(plugin.colorize(plugin.getConfig().getString("messages.cannot_message_muted", "&cYou can't message this player because you have been muted by them.")));
            }
        }
    }

    private boolean isMuted(String muterUuid, String mutedUuid) {
        try (PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM muted_players WHERE muter_uuid = ? AND muted_uuid = ?")) {
            statement.setString(1, muterUuid);
            statement.setString(2, mutedUuid);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error checking mute status: " + e.getMessage());
            return false;
        }
    }
}