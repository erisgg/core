package gg.eris.core.command.gamemode;

import org.bukkit.GameMode;

public final class GameModeSpectatorCommand extends SpecificGamemodeCommand {

  @Override
  public String getLabel() {
    return "gmsp";
  }

  @Override
  public GameMode getGameMode() {
    return GameMode.SPECTATOR;
  }

}
