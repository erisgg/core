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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ErisCore extends JavaPlugin {

  public static HashMap<Player, BukkitTask> teleportHubList = new HashMap<>();

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
