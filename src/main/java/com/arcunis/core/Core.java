package com.arcunis.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public final class Core extends JavaPlugin {

    public static FileConfiguration globalConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Core.globalConfig = getConfig();
    }

    @Override
    public void onDisable() {

    }

    public Connection getConnection() throws SQLException, RuntimeException {

        if (
                !getConfig().getKeys(true).containsAll(List.of("database.type", "database.database"))
                        || !getConfig().getString("database.type").equalsIgnoreCase("sqlite")
                        && !getConfig().getKeys(true).containsAll(List.of("database.username", "database.password"))
        ) {
            throw new RuntimeException("Could not get database connection as properties are incorrectly configured or missing");
        }

        Connection conn = null;

        String dbString = "jdbc:" + getConfig().getString("database.type") + "://" + getConfig().getString("database.database");

        if (getConfig().getString("database.type").equalsIgnoreCase("sqlite")) {
            conn = DriverManager.getConnection(dbString);
        } else {
            conn = DriverManager.getConnection(
                    dbString,
                    getConfig().getString("database.username"),
                    getConfig().getString("database.password")
            );
        }

        return conn;
    }
}
