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

@RequiredArgsConstructor
public final class AddRankCommand implements CommandProvider {

  private final ErisCore plugin;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "addrank",
        "returns the player to the main hub",
        "addrank <player> <rank>",
        ErisCoreIdentifiers.ADDRANK_PERMISSION
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
              "Adding rank <h>{0}</h> to <h>{1}</h>.",
              rank.getRawDisplay(),
              target
          );

          UUID uuid =
              this.plugin.getCommons().getErisPlayerManager().getOfflineDataManager()
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

          boolean modified = this.plugin.getCommons().getErisPlayerManager()
              .getOfflineDataManager().addRank(uuid, rank);

          if (modified) {
            TextController.send(
                context.getCommandSender(),
                TextType.SUCCESS,
                "Added the <h>{0}</h> rank to <h>{1}</h>.",
                rank.getRawDisplay(),
                target
            );
          } else {
            TextController.send(
                context.getCommandSender(),
                TextType.SUCCESS,
                "No ranks have been updated. They may already have it. If not, try again.",
                rank.getRawDisplay(),
                target
            );
          }

        }).finished();
  }
}
