package net.smart.paintball.game;

import lombok.Data;
import net.smart.paintball.game.exception.PaintballException;
import net.smart.paintball.game.runnable.GameRunnable;
import net.smart.paintball.game.state.GameState;
import net.smart.paintball.game.team.GameTeam;
import net.smart.paintball.game.team.provider.BlueTeam;
import net.smart.paintball.game.team.provider.RedTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class Game {

    private final String name;
    private final int maxPlayers;
    private final GameRunnable runnable;
    private int time;
    private GameState state;
    private Location waitingLocation;
    private GameTeam blueTeam, redTeam;

    private final List<GamePlayer> players;

    public Game(String name, int maxPlayers) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.runnable = new GameRunnable(this);
        this.time = 15;
        this.state = GameState.WAITING;
        this.players = new ArrayList<>();
        this.blueTeam = new BlueTeam();
        this.redTeam = new RedTeam();
    }

    public List<GamePlayer> getPlayers(GameTeam team) {
        return players.stream().filter(gamePlayer -> gamePlayer.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    public void broadcast(String message) {
        players.forEach(gamePlayer -> gamePlayer.getPlayer().sendMessage(message));
    }

    public void playSound(Sound sound) {
        players.forEach(gamePlayer -> gamePlayer.getPlayer().playSound(
                gamePlayer.getPlayer().getLocation(), sound, 10.0f, 10.0f)
        );
    }

    public void playSound(GameTeam team, Sound sound) {
        getPlayers(team).forEach(gamePlayer -> gamePlayer.getPlayer().playSound(
                gamePlayer.getPlayer().getLocation(), sound, 10.0f, 10.0f));
    }

    public Optional<GamePlayer> getPlayer(String name) {
        return players.stream().filter(gamePlayer -> gamePlayer.getPlayer().getName().equalsIgnoreCase(name)).findFirst();
    }

    public boolean equalsTeam(String name, String name2) {
        Optional<GamePlayer> gamePlayer = getPlayer(name);
        Optional<GamePlayer> gamePlayer2 = getPlayer(name2);
        return (gamePlayer.isPresent() && gamePlayer2.isPresent()
                && gamePlayer.get().getTeam().equals(gamePlayer2.get().getTeam()));
    }

    public void setItems(String name) {
        getPlayer(name).ifPresent(gamePlayer -> {
            ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
            chestplateMeta.setDisplayName(gamePlayer.getTeam().getNameColor());
            chestplateMeta.setColor(gamePlayer.getTeam().getColor());
            chestplate.setItemMeta(chestplateMeta);

            ItemStack snowball = new ItemStack(Material.SNOWBALL, 64);
            ItemMeta snowballMeta = snowball.getItemMeta();
            snowballMeta.setDisplayName(gamePlayer.getTeam().getNameColor());
            snowball.setItemMeta(snowballMeta);

            gamePlayer.getPlayer().getInventory().setChestplate(chestplate);

            for (int i = 0; i < 3; i++) {
                gamePlayer.getPlayer().getInventory().addItem(snowball);
            }
        });
    }

    public void respawn(String name) {
        getPlayer(name).ifPresent(gamePlayer -> gamePlayer.getPlayer().teleport(gamePlayer.getTeam().getLocation()));
    }

    public void addPlayer(Player player) {
        if (players.size() >= maxPlayers)
            throw new PaintballException("A sala " + name + " está lotada!");

        players.add(new GamePlayer(player, null));
    }
}
