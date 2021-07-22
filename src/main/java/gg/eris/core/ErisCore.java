package gg.eris.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.eris.commons.bukkit.ErisBukkitCommons;
import gg.eris.commons.bukkit.util.CC;
import gg.eris.commons.core.redis.RedisSubscriber;
import gg.eris.commons.core.redis.RedisWrapper;
import gg.eris.core.command.BroadcastCommand;
import gg.eris.core.command.HubCommand;
import gg.eris.core.command.MessageCommand;
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

import java.util.HashMap;
import java.util.UUID;

public final class ErisCore extends JavaPlugin {

  public static HashMap<Player, BukkitTask> teleportHubList = new HashMap<>();
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private RedisWrapper wrapper;

  @Override
  public void onEnable() {
    ErisBukkitCommons commons =
        Bukkit.getServicesManager().getRegistration(ErisBukkitCommons.class).getProvider();
    wrapper = commons.getRedisWrapper();
    commons.getCommandManager().registerCommands(
        new TeleportCommand(),
        new GamemodeCommand(),
        new GameModeSurvivalCommand(),
        new GameModeCreativeCommand(),
        new GameModeAdventureCommand(),
        new GameModeSpectatorCommand(),
        new BroadcastCommand(),
        new HubCommand(),
        new MessageCommand(wrapper)
    );


    receiveMessages();
  }

  private void receiveMessages(){
    Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
      @Override
      public void run() {
        RedisSubscriber subscriber = RedisSubscriber.builder("message").withCallback(callback -> {
          JsonNode node = callback.getPayload();
          String sender = node.get("sender").asText();
          String receiver = node.get("receiver").asText();
          String message = node.get("message").asText();

          Player receiverPlayer = Bukkit.getPlayer(UUID.fromString(receiver));

          receiverPlayer.sendMessage(CC.GREEN.underline() + "FROM: " + CC.GOLD.underline() + sender + " " + CC.WHITE + message);
          //TODO use text controller for this
        }).build();
        wrapper.subscribe(subscriber);
      }
    });
  }

}
