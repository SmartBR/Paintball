package net.smart.paintball.game.controller;

import net.smart.paintball.Paintball;
import net.smart.paintball.game.Game;
import net.smart.paintball.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class GameMapController {

    private final GameManager gameManager;
    private final FileConfiguration config;

    public GameMapController(GameManager gameManager, FileConfiguration config) {
        this.gameManager = gameManager;
        this.config = config;
        load();
    }

    public void save(Game game) {
        config.set("maps." + game.getName() + ".max-players", game.getMaxPlayers());

        if (game.getWaitingLocation() != null) {
            String waitingLocationSerialized = serializeLocation(game.getWaitingLocation());
            config.set("maps." + game.getName() + ".waiting", waitingLocationSerialized);
        }
        if (game.getBlueTeam().getLocation() != null) {
            String blueLocationSerialized = serializeLocation(game.getBlueTeam().getLocation());
            config.set("maps." + game.getName() + ".blue", blueLocationSerialized);
        }
        if (game.getRedTeam().getLocation() != null) {
            String redLocationSerialized = serializeLocation(game.getRedTeam().getLocation());
            config.set("maps." + game.getName() + ".red", redLocationSerialized);
        }
        Paintball.getInstance().saveConfig();
    }

    public void load() {
        try {
            ConfigurationSection section;
            if ((section = config.getConfigurationSection("maps")) != null) {
                section.getKeys(false).forEach(mapName -> {
                    int maxPlayers = section.getInt(mapName + ".max-players");
                    Location waitingLocation = deserializeLocation(section.getString(mapName + ".waiting"));
                    Location blueLocation = deserializeLocation(section.getString(mapName + ".blue"));
                    Location redLocation = deserializeLocation(section.getString(mapName + ".red"));
                    gameManager.createGame(mapName, maxPlayers, waitingLocation, blueLocation, redLocation);
                });
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Location deserializeLocation(String serialized) {
        String[] serializedArr = serialized.split(" : ");
        return new Location(
                Bukkit.getWorld(serializedArr[0]),
                Double.parseDouble(serializedArr[1]),
                Double.parseDouble(serializedArr[2]),
                Double.parseDouble(serializedArr[3]),
                Float.parseFloat(serializedArr[4]),
                Float.parseFloat(serializedArr[5])
        );
    }

    private String serializeLocation(Location location) {
        return location.getWorld().getName() + " : " + location.getX() + " : " + location.getY() + " : " + location.getZ() +
                " : " + location.getYaw() + " : " + location.getPitch();
    }
}
