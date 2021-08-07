package gg.eris.core.command.rank;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayerManager;
import gg.eris.commons.bukkit.rank.Rank;
import gg.eris.commons.bukkit.rank.RankRegistry;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class AddRankCommand implements CommandProvider {

  private final ErisPlayerManager erisPlayerManager;
  private final RankRegistry registry;

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
          Rank rank = this.registry.get(rankName);

          UUID uuid = this.erisPlayerManager.getOfflineDataManager().getUuid(target);

          if (uuid == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Could not find player <h>{0}</h>.",
                target
            );
            return;
          }

          if (rank == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Rank <h>{0}</h> does not exist.",
                rankName
            );
            return;
          }

          this.erisPlayerManager.getOfflineDataManager().addRank(uuid, rank);

          TextController.send(
              context.getCommandSender(),
              TextType.SUCCESS,
              "Added the <h>{0}</h> rank to <h>{1}</h>.",
              rank.getRawDisplay(),
              target
          );
        }).finished();
  }
}
