package gg.eris.core.command.gamemode;

import org.bukkit.GameMode;

// TODO: messages
public final class GameModeCreativeCommand extends SpecificGamemodeCommand {

  @Override
  public String getLabel() {
    return "gmc";
  }

  @Override
  public GameMode getGameMode() {
    return GameMode.CREATIVE;
  }

}
