package gg.eris.core.command.gamemode;

import org.bukkit.GameMode;

public class GameModeAdventureCommand extends SpecificGamemodeCommand {

  @Override
  public String getLabel() {
    return "gma";
  }

  @Override
  public GameMode getGameMode() {
    return GameMode.ADVENTURE;
  }

}
