package gg.eris.core.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gg.eris.commons.bukkit.command.Command;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.core.redis.RedisPublisher;
import gg.eris.commons.core.redis.RedisWrapper;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import org.bukkit.Bukkit;

public final class BroadcastCommand implements CommandProvider {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  private final RedisWrapper wrapper;

  public BroadcastCommand(RedisWrapper wrapper) {
    this.wrapper = wrapper;
  }


  @Override
  public Command.Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "broadcast",
        "broadcasts a message to all players",
        "broadcast <message>",
        ErisCoreIdentifiers.BROADCAST_PERMISSION,
        "bc"
    ).withSubCommand()
        .variableArgument(StringArgument.of("message"), 1)
        .asPlayerOnly()
        .handler(context -> {
              String message = context.getArgument("message");

              ObjectNode node = MAPPER.createObjectNode()
                  .put("message", message);

              Bukkit.getScheduler()
                  .runTaskAsynchronously(ErisCore.getPlugin(ErisCore.class), () -> {
                    RedisPublisher publisher = RedisPublisher.builder(node, "message").build();
                    wrapper.publish(publisher);
                  });
            }
        ).finished();
  }
}
