package gg.eris.core.command.nick;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayer;
import gg.eris.commons.bukkit.player.nickname.PlayerNicknamePipeline;
import gg.eris.commons.bukkit.player.nickname.PlayerNicknameSkinsList;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.commons.core.redis.RedisWrapper;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class NickCommand implements CommandProvider {

  private final ErisCore plugin;
  private final RedisWrapper wrapper;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "nickname",
        "nick a player",
        "nick <name>",
        ErisCoreIdentifiers.NICK_PERMISSION,
        "nick"
    ).withSubCommand()
        .argument(StringArgument.of("name"))
        .asPlayerOnly()
        .handler(context -> {
          Player sender = context.getSenderAsPlayer();
          String name = context.getArgument("name");
          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (!PlayerNicknamePipeline.isValidNickName(name)) {
              TextController.send(
                  sender,
                  TextType.ERROR,
                  "The name '<h>{0}</h>' is not a valid name.",
                  name
              );
              return;
            }

            for (ErisPlayer player : this.plugin.getCommons().getErisPlayerManager().getPlayers()) {
              if (player.getDisplayName().equalsIgnoreCase(name)) {
                TextController.send(
                    context.getCommandSender(),
                    TextType.ERROR,
                    "The name '<h>{0}</h>' is not a valid name.",
                    name
                );
                return;
              }
            }

            Bukkit.getScheduler().runTask(this.plugin, () -> {

              if (this.plugin.getCommons().getRedisWrapper().setContainsValue(PlayerNicknamePipeline.NICKNAME_SET, name.toLowerCase(
                  Locale.ROOT))) {
                TextController.send(
                    context.getCommandSender(),
                    TextType.ERROR,
                    "The name '<h>{0}</h>' is not a valid name.",
                    name
                );
              } else {
                ErisPlayer player = this.plugin.getCommons().getErisPlayerManager().getPlayer(sender);
                player.getNicknameProfile().setNickName(name, PlayerNicknameSkinsList.getRandomSkin());
                TextController.send(
                    player,
                    TextType.SUCCESS,
                    "You have nicked as '<h>{0}</h>'.",
                    name
                );
              }
            });
          });
        }).finished();
  }
}
