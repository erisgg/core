package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayer;
import gg.eris.commons.bukkit.player.ErisPlayerManager;
import gg.eris.commons.bukkit.rank.Rank;
import gg.eris.commons.bukkit.rank.RankRegistry;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCoreIdentifiers;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class SetRankCommand implements CommandProvider {

  private final ErisPlayerManager erisPlayerManager;
  private final RankRegistry registry;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
            "setrank",
            "returns the player to the main hub",
            ErisCoreIdentifiers.SETRANK_PERMISSION
        ).noArgsHandler(context -> {
          if (context.getCommandSender() instanceof Player) {
            TextController.send(
                context.getSenderAsPlayer(),
                TextType.ERROR,
                "Invalid usage. Use /setrank <raw><player> <rank></raw>"
            );
          } else {
            context.getCommandSender().sendMessage("Invalid usage. Use /setrank <player> <rank>");
          }
        }).withSubCommand()
        .argument(PlayerArgument.of("target"))
        .argument(StringArgument.of("rank"))
        .handler(context -> {
          Player target = context.getArgument("target");
          String rankName = context.getArgument("rank");
          Rank rank = this.registry.get(rankName);

          if (target == null) {
            if (context.getCommandSender() instanceof Player) {
              TextController.send(
                  context.getSenderAsPlayer(),
                  TextType.ERROR,
                  "Player <h>{0}</h> is not online.",
                  context.getRawArgs()[0]
              );
            } else {
              context.getCommandSender().sendMessage("Player " + context.getRawArgs()[0]
                  + " is not online.");
            }

            return;
          }

          if (rank == null) {
            if (context.getCommandSender() instanceof Player) {
              TextController.send(
                  context.getSenderAsPlayer(),
                  TextType.ERROR,
                  "Rank <h>{0}</h> does not exist.",
                  rankName
              );
            } else {
              context.getCommandSender().sendMessage("Rank " + rankName + " does not exist.");
            }

            return;
          }

          ErisPlayer player = this.erisPlayerManager.getPlayer(target);
          player.setRank(rank);

          if (context.getCommandSender() instanceof Player) {
            TextController.send(
                context.getSenderAsPlayer(),
                TextType.SUCCESS,
                "Set <h>{0}</h>'s rank to <h>{1}</h>.",
                player.getName(),
                rank.getRawDisplay()
            );
          } else {
            context.getCommandSender()
                .sendMessage("Set " + player.getName() + "'s rank to " + rank.getRawDisplay()
                    + ".");
          }
        }).finished();
  }
}
