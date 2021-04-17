package gg.eris.core.command.gamemode;

import org.bukkit.GameMode;

public class GameModeSurvivalCommand extends SpecificGamemodeCommand {

  @Override
  public String getLabel() {
    return "gms";
  }

  @Override
  public GameMode getGameMode() {
    return GameMode.SURVIVAL;
  }

}
