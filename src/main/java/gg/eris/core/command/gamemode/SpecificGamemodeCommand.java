package gg.eris.core.command.gamemode;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.Locale;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

// TODO: messages
public abstract class SpecificGamemodeCommand implements CommandProvider {

  public abstract String getLabel();

  public abstract GameMode getGameMode();

  @Override
  public Builder getCommand(CommandManager manager) {
    GameMode gamemode = getGameMode();
    return manager.newCommandBuilder(
        getLabel(),
        "shorthand gamemode " + gamemode.name().toLowerCase(Locale.ROOT),
        getLabel(),
        ErisCoreIdentifiers.GAMEMODE_PERMISSION
    ).noArgsHandler(context -> {
      context.getSenderAsPlayer().setGameMode(gamemode);
      TextController.send(context.getSenderAsPlayer(), TextType.SUCCESS, "Gamemode updated.");
    }, true)
        .withSubCommand()
        .argument(PlayerArgument.of("who"))
        .handler(context -> {
          Player player = context.getArgument("who");
          if (player == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Player <h>{0}</h> could not be found.",
                context.getRawArgs()[0]
            );
          } else {
            player.setGameMode(gamemode);
            TextController.send(
                context.getCommandSender(),
                TextType.SUCCESS,
                "Gamemode updated for <h>{0}</h>.",
                player.getName()
            );
          }

        }).finished();
  }
}
