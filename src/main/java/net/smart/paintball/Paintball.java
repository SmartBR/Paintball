package net.smart.paintball;

import lombok.Getter;
import net.smart.paintball.command.GameCommand;
import net.smart.paintball.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Paintball extends JavaPlugin {

    public static Paintball getInstance() {
        return getPlugin(Paintball.class);
    }

    @Getter private GameManager gameManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.gameManager = new GameManager(this);
        getCommand("game").setExecutor(new GameCommand(this));

        Bukkit.getConsoleSender().sendMessage("§a§lPAINTBALL: §fPlugin habilitado!");
    }
}
