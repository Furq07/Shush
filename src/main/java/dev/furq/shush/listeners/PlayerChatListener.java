package dev.furq.shush.listeners;

import dev.furq.shush.Shush;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerChatListener implements Listener {
    private final Shush plugin;

    public PlayerChatListener(Shush plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("shush.bypass")) {
            return;
        }
        String mutedUuid = event.getPlayer().getUniqueId().toString();
        try (PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT muter_uuid FROM muted_players WHERE muted_uuid = ?")) {
            statement.setString(1, mutedUuid);
            ResultSet resultSet = statement.executeQuery();

            List<UUID> mutedFor = new ArrayList<>();
            while (resultSet.next()) {
                mutedFor.add(UUID.fromString(resultSet.getString("muter_uuid")));
            }

            if (!mutedFor.isEmpty()) {
                event.getRecipients().removeIf(player -> mutedFor.contains(player.getUniqueId()));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error querying muted players: " + e.getMessage());
        }
    }
}