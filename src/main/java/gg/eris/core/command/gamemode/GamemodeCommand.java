package gg.eris.core.command.gamemode;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.Locale;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

// TODO: messages
public final class GamemodeCommand implements CommandProvider {

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "gamemode",
        "sets gamemode",
        "gamemode <gamemode>",
        ErisCoreIdentifiers.GAMEMODE_PERMISSION,
        "gm"
    ).withSubCommand()
        .argument(StringArgument.of("gamemode"))
        .asPlayerOnly()
        .handler(context -> {
          String gamemode = context.getArgument("gamemode");
          GameMode gamemodeType = getType(gamemode);
          if (gamemodeType == null) {
            TextController.send(
                context.getSenderAsPlayer(),
                TextType.ERROR,
                "Gamemode <h>{0}</h> could not be found.",
                context.getRawArgs()[0]
            );
          } else {
            context.getSenderAsPlayer().setGameMode(gamemodeType);
          }
        }).finished()
        .withSubCommand()
        .argument(PlayerArgument.of("who"))
        .argument(StringArgument.of("gamemode"))
        .handler(context -> {
          Player player = context.getArgument("who");
          String gamemode = context.getArgument("gamemode");
          GameMode gamemodeType = getType(gamemode);
          if (gamemodeType == null) {
            TextController.send(
                player,
                "Gamemode <h>{0}</h> could not be found.",
                context.getRawArgs()[1]
            );
          } else {
            player.setGameMode(gamemodeType);
          }
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
