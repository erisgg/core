package gg.eris.core.command.gamemode;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

// TODO: messages
public class GameModeCreativeCommand extends SpecificGamemodeCommand {

  @Override
  public String getLabel() {
    return "gmc";
  }

  @Override
  public GameMode getGameMode() {
    return GameMode.CREATIVE;
  }

}
