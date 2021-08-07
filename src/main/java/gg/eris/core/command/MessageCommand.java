package gg.eris.core.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.core.redis.RedisPublisher;
import gg.eris.commons.core.redis.RedisWrapper;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public final class MessageCommand implements CommandProvider {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  private final RedisWrapper wrapper;

  public MessageCommand(RedisWrapper wrapper) {
    this.wrapper = wrapper;
  }

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "message",
        "sends a message to a player",
        "message <raw><player> <message></raw>",
        ErisCoreIdentifiers.MESSAGE_PERMISSION,
        "msg"
    ).noArgsHandler(context -> {
      System.out.println("no args handler is called");
      context.getCommandSender().sendMessage("Bad usage");
    }).withSubCommand()
        .argument(PlayerArgument.of("player"))
        .variableArgument(StringArgument.of("message"),
            1) //label arg will be removed soon, ignore error
        .handler(context -> {
          List<String> messageStrings = context.getArgument("message");
          String message = "";
          for (String messageObject : messageStrings) {
            message = message + messageObject + " ";
          }
          Player sender = context.getSenderAsPlayer();
          Player receiver = context.getArgument("player");

          ObjectNode node = MAPPER.createObjectNode();
          node.put("sender", sender.getName());
          node.put("receiver", String.valueOf(receiver.getUniqueId()));
          node.put("message", message);

          Bukkit.getScheduler()
              .runTaskAsynchronously(ErisCore.getPlugin(ErisCore.class), new Runnable() {
                @Override
                public void run() {
                  RedisPublisher publisher = RedisPublisher.builder(node, "message").build();
                  wrapper.publish(publisher);
                }
              });

        }).finished();
  }
}
