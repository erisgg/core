package gg.eris.core;

import gg.eris.commons.bukkit.ErisBukkitCommons;
import gg.eris.core.command.BroadcastCommand;
import gg.eris.core.command.HubCommand;
import gg.eris.core.command.TeleportCommand;
import gg.eris.core.command.gamemode.GameModeAdventureCommand;
import gg.eris.core.command.gamemode.GameModeCreativeCommand;
import gg.eris.core.command.gamemode.GameModeSpectatorCommand;
import gg.eris.core.command.gamemode.GameModeSurvivalCommand;
import gg.eris.core.command.gamemode.GamemodeCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ErisCore extends JavaPlugin {

  @Override
  public void onEnable() {
    ErisBukkitCommons commons =
        Bukkit.getServicesManager().getRegistration(ErisBukkitCommons.class).getProvider();
    commons.getCommandManager().registerCommands(
        new TeleportCommand(),
        new GamemodeCommand(),
        new GameModeSurvivalCommand(),
        new GameModeCreativeCommand(),
        new GameModeAdventureCommand(),
        new GameModeSpectatorCommand(),
        new BroadcastCommand(),
        new HubCommand()
    );
  }

}
