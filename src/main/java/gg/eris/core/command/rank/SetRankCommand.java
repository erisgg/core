package gg.eris.core.command.rank;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.rank.Rank;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class SetRankCommand implements CommandProvider {

  private final ErisCore plugin;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "setrank",
        "sets the only rank of a player",
        "setrank <player> <rank>",
        ErisCoreIdentifiers.SETRANK_PERMISSION
    ).withSubCommand()
        .argument(StringArgument.of("target"))
        .argument(StringArgument.of("rank"))
        .handler(context -> {
          String target = context.getArgument("target");
          String rankName = context.getArgument("rank");
          Rank rank = this.plugin.getCommons().getRankRegistry().get(rankName);

          if (rank == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Rank <h>{0}</h> does not exist.",
                rankName
            );
            return;
          }

          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Setting rank for <h>{0}</h> to <h>{1}</h>.",
              target,
              rank.getRawDisplay()
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

            this.plugin.getCommons().getErisPlayerManager().getOfflineDataManager()
                .setRank(uuid, rank);

            TextController.send(
                context.getCommandSender(),
                TextType.SUCCESS,
                "Set <h>{0}</h>'s rank to <h>{1}</h>.",
                target,
                rank.getRawDisplay()
            );
          });
        }).finished();
  }
}
