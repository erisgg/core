package gg.eris.core.command.rank;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayer;
import gg.eris.commons.bukkit.rank.Rank;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fusesource.jansi.internal.Kernel32.CONSOLE_SCREEN_BUFFER_INFO;

@RequiredArgsConstructor
public final class ViewRanksCommand implements CommandProvider {

  private final ErisCore plugin;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "viewranks",
        "shows the ranks of a player",
        "viewranks <player>",
        ErisCoreIdentifiers.ADDRANK_PERMISSION
    ).withSubCommand()
        .argument(StringArgument.of("target"))
        .handler(context -> {
          String target = context.getArgument("target");

          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Looking up ranks for <h>{0}</h>.",
              target
          );

          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            UUID uuid = this.plugin.getCommons().getErisPlayerManager().getOfflineDataManager()
                .getUuid(target);
            if (uuid == null) {
              TextController.send(
                  context.getCommandSender(),
                  TextType.ERROR,
                  "Could not find player <h>{0}</h>.",
                  target
              );
              return;
            }

            showSavedRanks(context.getCommandSender(), uuid, target);
          });
        }).finished()
        .withSubCommand()
        .argument(PlayerArgument.of("target"))
        .argument(StringArgument.of("live"))
        .handler(context -> {
          Player target = context.getArgument("target");
          if (target == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Player <h>{0}</h> could not be found.",
                context.getRawArgs()[0]
            );
            return;
          }

          ErisPlayer erisPlayer = this.plugin.getCommons().getErisPlayerManager().getPlayer(target);

          StringBuilder message = new StringBuilder("<h>")
              .append(target)
              .append("'s Ranks</h>");

          for (Rank rank : erisPlayer.getRanks()) {
            message.append("\n - ").append(rank.getRawDisplay());
          }

          TextController.send(
              context.getCommandSender(),
              TextType.SUCCESS,
              message.toString()
          );

        }).finished();
  }

  private void showSavedRanks(CommandSender sender, UUID uuid, String target) {
    List<Rank> ranks =
        this.plugin.getCommons().getErisPlayerManager().getOfflineDataManager()
            .getRanks(uuid);

    Collections.sort(ranks);

    StringBuilder message = new StringBuilder("<h>")
        .append(target)
        .append("'s Ranks</h>");

    for (Rank rank : ranks) {
      message.append("\n - ").append(rank.getRawDisplay());
    }

    TextController.send(
        sender,
        TextType.SUCCESS,
        message.toString()
    );
  }
}
