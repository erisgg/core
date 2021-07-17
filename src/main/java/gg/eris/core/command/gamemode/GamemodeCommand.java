package gg.eris.core.command.gamemode;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.Locale;

import gg.eris.commons.core.identifier.Identifier;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

// TODO: messages
public final class GamemodeCommand implements CommandProvider {

  public static final Identifier PERMISSION = Identifier.of("eris", "gamemode");

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "gameMode",
        "sets gamemode",
        ErisCoreIdentifiers.GAMEMODE_PERMISSION,
        "gm"
    ).noArgsHandler(handler -> {
      handler.getSenderAsPlayer().sendMessage("Bad usage");
    }).withSubCommand()
        .argument(StringArgument.of("gameMode"))
        .asPlayerOnly()
        .handler(context -> {
          String gameMode = context.getArgument("gameMode");
          GameMode gamemodeType = getType(gameMode);
          context.getSenderAsPlayer().setGameMode(gamemodeType);
        }).finished()
        .withSubCommand()
        .argument(PlayerArgument.of("who"))
        .argument(StringArgument.of("gameMode"))
        .handler(context -> {
          Player player = context.getArgument("who");
          String gameMode = context.getArgument("gameMode");
          GameMode gamemodeType = getType(gameMode);
          player.setGameMode(gamemodeType);
        }).finished();
  }

  private GameMode getType(String arg) {
    switch (arg.toLowerCase(Locale.ROOT)) {
      case "creative":
      case "c":
        return GameMode.CREATIVE;
      case "survival":
      case "s":
        return GameMode.SURVIVAL;
      case "a":
      case "adventure":
        return GameMode.ADVENTURE;
      case "sp":
      case "spectator":
      case "spec":
        return GameMode.SPECTATOR;
      default:
        return null;
    }
  }
}
