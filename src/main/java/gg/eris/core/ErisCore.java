package gg.eris.core;

import com.fasterxml.jackson.databind.JsonNode;
import gg.eris.commons.bukkit.ErisBukkitCommons;
import gg.eris.commons.bukkit.text.TextColor;
import gg.eris.commons.bukkit.text.TextComponent;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextMessage;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.commons.core.redis.RedisSubscriber;
import gg.eris.commons.core.redis.RedisWrapper;
import gg.eris.core.command.BroadcastCommand;
import gg.eris.core.command.HubCommand;
import gg.eris.core.command.MessageCommand;
import gg.eris.core.command.TeleportCommand;
import gg.eris.core.command.UuidCommand;
import gg.eris.core.command.gamemode.GameModeAdventureCommand;
import gg.eris.core.command.gamemode.GameModeCreativeCommand;
import gg.eris.core.command.gamemode.GameModeSpectatorCommand;
import gg.eris.core.command.gamemode.GameModeSurvivalCommand;
import gg.eris.core.command.gamemode.GamemodeCommand;
import gg.eris.core.command.nick.NickCommand;
import gg.eris.core.command.nick.RealNameCommand;
import gg.eris.core.command.nick.UnnickCommand;
import gg.eris.core.command.permission.AddPermissionCommand;
import gg.eris.core.command.permission.RemovePermissionCommand;
import gg.eris.core.command.permission.ViewPermissionsCommand;
import gg.eris.core.command.punishment.PunishCommand;
import gg.eris.core.command.punishment.UnbanCommand;
import gg.eris.core.command.punishment.UnmuteCommand;
import gg.eris.core.command.rank.AddRankCommand;
import gg.eris.core.command.rank.RemoveRankCommand;
import gg.eris.core.command.rank.SetRankCommand;
import gg.eris.core.command.rank.ViewRanksCommand;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ErisCore extends JavaPlugin {

  private ErisBukkitCommons commons;
  private RedisWrapper wrapper;

  @Override
  public void onEnable() {
    this.commons =
        Bukkit.getServicesManager().getRegistration(ErisBukkitCommons.class).getProvider();
    this.wrapper = this.commons.getRedisWrapper();
    this.commons.getCommandManager().registerCommands(
        new TeleportCommand(),
        new SetRankCommand(this),
        new AddRankCommand(this),
        new NickCommand(this),
        new UnnickCommand(this.commons.getErisPlayerManager()),
        new RealNameCommand(this.commons.getErisPlayerManager()),
        new RemoveRankCommand(this),
        new ViewRanksCommand(this),
        new AddPermissionCommand(this),
        new RemovePermissionCommand(this),
        new ViewPermissionsCommand(this),
        new UuidCommand(this),
        new PunishCommand(this),
        new UnbanCommand(this),
        new UnmuteCommand(this),
        new GamemodeCommand(),
        new GameModeSurvivalCommand(),
        new GameModeCreativeCommand(),
        new GameModeAdventureCommand(),
        new GameModeSpectatorCommand(),
        new MessageCommand(this.getCommons().getErisPlayerManager(), this.wrapper),
        new BroadcastCommand(this.wrapper),
        new HubCommand()
    );

    // hugh code
    receiveMessages();
    broadcastMessages();
  }

  private void receiveMessages() {
    Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
      RedisSubscriber subscriber = RedisSubscriber.builder("message").withCallback(callback -> {
        JsonNode node = callback.getPayload();
        String sender = node.get("sender").asText();
        String receiver = node.get("receiver").asText();
        String message = node.get("message").asText();

        Player to = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
          if (player.getName().equalsIgnoreCase(receiver)) {
            to = player;
            break;
          }
        }

        if (to == null) {
          return;
        }

        TextMessage messageComponent = TextMessage
            .of(TextComponent.builder("FROM ").color(TextColor.GREEN).build(),
                TextComponent.builder(sender + ": ").color(TextColor.YELLOW).build(),
                TextComponent.builder(message).color(TextColor.WHITE).build());

        TextController.send(to, messageComponent);
      }).build();
      wrapper.subscribe(subscriber);
    });
  }

  private void broadcastMessages() {
    Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
      RedisSubscriber subscriber = RedisSubscriber.builder("broadcast").withCallback(callback -> {
        JsonNode node = callback.getPayload();
        String message = node.get("message").asText();
        TextMessage announcement =
            TextController.parse("\n \n<col=yellow><b>Announcement</b> " + TextController.ARROW
                + " <col=gold>{0}</col>\n \n", message);
        TextController.broadcastToServer(announcement);
      }).build();
      this.wrapper.subscribe(subscriber);
    });
  }

}
