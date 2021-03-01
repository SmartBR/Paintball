package net.smart.paintball.command;

import lombok.AllArgsConstructor;
import net.smart.paintball.Paintball;
import net.smart.paintball.game.Game;
import net.smart.paintball.game.state.GameState;
import net.smart.paintball.game.team.GameTeam;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

@AllArgsConstructor
public class GameCommand implements CommandExecutor {

    private final Paintball plugin;

    private boolean help(CommandSender sender) {
        sender.sendMessage(new String[] {
                "§b§lPaintball §f- Comandos:",
                "§b/game criar <sala> <máximo de players> §f- Criar uma sala..",
                "§b/game setloc <sala> <waiting:blue:red> §f- Setar localização de uma sala.",
                "§b/game entrar <sala> §f- Entrar em uma sala."
        });
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c§lPAINTBALL: §fComando apenas para jogadores.");
            return true;
        }

        if (!sender.hasPermission("paintball.admin")) {
            sender.sendMessage("§cVocê não tem permissão para executar este comando.");
            return true;
        }

        if (args.length == 0) {
            return help(sender);
        }
        Player player = (Player) sender;

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("entrar")) {
                String mapName = args[1];
                Optional<Game> gameOptional;

                if (!(gameOptional = plugin.getGameManager().getGame(mapName)).isPresent()) {
                    sender.sendMessage("§cJogo não encontrado.");
                    return true;
                }
                Game game = gameOptional.get();

                if (game.getState().equals(GameState.INGAME)) {
                    sender.sendMessage("§cEste jogo já está em andamento.");
                    return true;
                }

                if (game.getPlayers().size() == game.getMaxPlayers()) {
                    sender.sendMessage("§cEste jogo está lotado.");
                    return true;
                }

                if (game.getWaitingLocation() != null)
                    player.teleport(game.getWaitingLocation());

                game.addPlayer(player);
            }else {
                help(sender);
            }
        }

        if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "criar":
                    String name = args[1];
                    int maxPlayers;

                    try {
                        maxPlayers = Integer.parseInt(args[2]);
                    }catch (NumberFormatException ignored) {
                        sender.sendMessage("§cDigite um número válido.");
                        return true;
                    }

                    if (plugin.getGameManager().getGame(name).isPresent()) {
                        sender.sendMessage("§cJá existe um jogo com este nome.");
                        return true;
                    }

                    plugin.getGameManager().createGame(name, maxPlayers);
                    sender.sendMessage("§aJogo criado com sucesso!");
                    break;
                case "setloc":
                    String gameName = args[1];
                    String locationName = args[2];

                    Optional<Game> gameOptional;
                    if (!(gameOptional = plugin.getGameManager().getGame(gameName)).isPresent()) {
                        sender.sendMessage("§cEste jogo não existe.");
                        return true;
                    }

                    Game game = gameOptional.get();

                    switch (locationName.toLowerCase()) {
                        case "waiting":
                            game.setWaitingLocation(player.getLocation());
                            plugin.getGameManager().getGameMapController().save(game);
                            sender.sendMessage("§aVocê setou a localização da sala de espera do jogo §f" + game.getName() + "§a.");
                            break;
                        case "blue":
                        case "red":
                            GameTeam team = locationName.equalsIgnoreCase("blue") ? game.getBlueTeam() : game.getRedTeam();
                            team.setLocation(player.getLocation());
                            plugin.getGameManager().getGameMapController().save(game);
                            sender.sendMessage("§aVocê setou a localização do time " + team.getNameColor() + " §ado jogo §f" + game.getName() + "§a.");
                            break;
                        default:
                            sender.sendMessage("§cLocalizações disponíveis: waiting, blue, red");
                    }
                    break;
                default:
                    help(sender);
            }
        }
        return true;
    }
}
