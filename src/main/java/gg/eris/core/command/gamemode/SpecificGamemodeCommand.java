package gg.eris.core.command.gamemode;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
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
    GameMode gameMode = getGameMode();
    return manager.newCommandBuilder(
        getLabel(),
        "shorthand gamemode " + gameMode.name().toLowerCase(Locale.ROOT),
        ErisCoreIdentifiers.GAMEMODE_PERMISSION
    ).noArgsHandler(context -> context.getSenderAsPlayer().setGameMode(gameMode), true)
        .withSubCommand().argument(PlayerArgument.of("who"))
        .handler(context -> {
          Player player = context.getArgument("who");
          player.setGameMode(gameMode);
        }).finished();
  }
}
