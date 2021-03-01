package net.smart.paintball.game;

import lombok.Getter;
import net.smart.paintball.Paintball;
import net.smart.paintball.game.controller.GameMapController;
import net.smart.paintball.game.listener.*;
import net.smart.paintball.game.runnable.GameRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameManager {

    private final Paintball plugin;
    @Getter private final List<Game> games;
    @Getter private final GameMapController gameMapController;

    public GameManager(Paintball plugin) {
        this.plugin = plugin;
        this.games = new ArrayList<>();
        this.gameMapController = new GameMapController(this, plugin.getConfig());

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new GameSnowballListener(this), plugin);
        pluginManager.registerEvents(new GameUtilityListener(this), plugin);
        pluginManager.registerEvents(new GamePlayerRespawnListener(), plugin);
        pluginManager.registerEvents(new GameStartListener(), plugin);
        pluginManager.registerEvents(new GameWinListener(), plugin);
    }

    public Game createGame(String name, int maxPlayers) {
        Game game = new Game(name, maxPlayers);
        new GameRunnable(game).runTaskTimer(plugin, 0, 20L);
        games.add(game);
        return game;
    }

    public void createGame(String name, int maxPlayers, Location waitingLocation, Location blueLocation, Location redLocation) {
        Game game = createGame(name, maxPlayers);
        game.setWaitingLocation(waitingLocation);
        game.getBlueTeam().setLocation(blueLocation);
        game.getRedTeam().setLocation(redLocation);
    }

    public Optional<Game> getGame(String name) {
        return games.stream().filter(game -> game.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Game> getPlayerGame(String name) {
        return games.stream().filter(game -> game.getPlayer(name).isPresent()).findFirst();
    }

    public Optional<GamePlayer> getPlayer(String name) {
        return games.stream().filter(game -> game.getPlayer(name).isPresent())
                .map(game -> game.getPlayer(name).get()).findFirst();
    }
}
