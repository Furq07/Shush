package dev.furq.shush;

import dev.furq.shush.commands.ShushCommand;
import dev.furq.shush.listeners.PlayerChatListener;
import dev.furq.shush.listeners.CommandListener;
import dev.furq.shush.utils.TabCompleter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Shush extends JavaPlugin {
    private Connection connection;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        initDatabase();
        getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
        getServer().getPluginManager().registerEvents(new CommandListener(this), this); // Add this line
        getCommand("shush").setExecutor(new ShushCommand(this));
        getCommand("shush").setTabCompleter(new TabCompleter());
    }

    @Override
    public void onDisable() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            getLogger().severe("Failed to close database connection: " + e.getMessage());
        }
    }

    private void initDatabase() {
        try {
            File dbFile = new File(getDataFolder(), "shush.db");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            try (var statement = connection.createStatement()) {
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS muted_players (" +
                                "muter_uuid TEXT," +
                                "muted_uuid TEXT," +
                                "PRIMARY KEY (muter_uuid, muted_uuid)" +
                                ")"
                );
            }
        } catch (SQLException e) {
            getLogger().severe("Failed to initialize database: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public Connection getConnection() {
        return connection;
    }
}
