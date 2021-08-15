package gg.eris.core.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayer;
import gg.eris.commons.bukkit.player.ErisPlayerManager;
import gg.eris.commons.bukkit.player.punishment.PunishmentDurations;
import gg.eris.commons.bukkit.text.TextColor;
import gg.eris.commons.bukkit.text.TextComponent;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextMessage;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.commons.core.redis.RedisPublisher;
import gg.eris.commons.core.redis.RedisWrapper;
import gg.eris.commons.core.util.Time;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public final class MessageCommand implements CommandProvider {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final ErisPlayerManager erisPlayerManager;
  private final RedisWrapper wrapper;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "message",
        "sends a message to a player",
        "message <player> <message>",
        ErisCoreIdentifiers.MESSAGE_PERMISSION,
        "msg", "w", "whisper"
    ).withSubCommand()
        .argument(StringArgument.of("player"))
        .variableArgument(StringArgument.of("message"), 1)
        .asPlayerOnly()
        .handler(context -> {
          List<String> messages = context.getArgument("message");
          String message = StringUtils.join(messages, " ");

          Player sender = context.getSenderAsPlayer();
          ErisPlayer player = this.erisPlayerManager.getPlayer(sender);

          long muteDuration = player.getPunishmentProfile().getMuteDuration();
          if (muteDuration != 0L) {
            if (muteDuration == PunishmentDurations.INDEFINITE) {
              TextController.send(
                  player,
                  TextType.ERROR,
                  "You are <h>permanently muted</h>."
              );
            } else {
              TextController.send(
                  player,
                  TextType.ERROR,
                  "You are currently <h>muted</h>. Your mute will expire in <h>{0}</h>.",
                  Time.toLongDisplayTime(muteDuration, TimeUnit.MILLISECONDS)
              );
            }
            return;
          }

          String receiver = context.getArgument("player");

          ObjectNode node = MAPPER.createObjectNode()
          .put("sender", sender.getName())
          .put("receiver", receiver)
          .put("message", message);


          TextMessage messageComponent = TextMessage
              .of(TextComponent.builder("TO ").color(TextColor.GREEN).build(),
                  TextComponent.builder(receiver + ": ").color(TextColor.YELLOW).build(),
                  TextComponent.builder(message).color(TextColor.WHITE).build());
          TextController.send(sender, messageComponent);

          Bukkit.getScheduler()
              .runTaskAsynchronously(ErisCore.getPlugin(ErisCore.class), () -> {
                RedisPublisher publisher = RedisPublisher.builder(node, "message").build();
                this.wrapper.publish(publisher);
              });
        }).finished();
  }
}
