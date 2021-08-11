package gg.eris.core.command.punishment;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.IntegerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayer;
import gg.eris.commons.bukkit.player.punishment.Punishment;
import gg.eris.commons.bukkit.player.punishment.PunishmentType;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.commons.core.util.UUIDUtil;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.Locale;
import java.util.UUID;
import javax.print.DocFlavor.READER;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class PunishCommand implements CommandProvider {

  private final ErisCore plugin;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "punish",
        "punishment command",
        "punish [player] [type] [severity]",
        ErisCoreIdentifiers.PUNISHMENT_PERMISSION,
        "p"
    ).withSubCommand()
        .argument(StringArgument.of("target"))
        .argument(StringArgument.of("type"))
        .argument(IntegerArgument.of("severity"))
        .handler(context -> {
          String targetName = context.getArgument("target");
          String typeRaw = context.getArgument("type");
          PunishmentType type = PunishmentType.fromLabel(typeRaw);
          int severity = context.getArgument("severity");

          if (type == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Punishment type <h>{0}</h> is unknown. Please use <h>\\\"chat\\\"</h> or "
                    + "<h>\\\"ingame\\\"</h>.",
                typeRaw
            );
            return;
          }

          if (severity <= 0 || severity > 4) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Invalid severity. Severity must be <h>between or equal to 1</h> and <h>4</h>."
            );
            return;
          }

          if (severity == 4) {
            if (context.getCommandSender() instanceof Player) {
              Player player = context.getSenderAsPlayer();
              ErisPlayer erisPlayer =
                  this.plugin.getCommons().getErisPlayerManager().getPlayer(player);
              if (!erisPlayer.hasPermission(ErisCoreIdentifiers.PUNISHMENT_PERMISSION_UPPER)) {
                TextController.send(
                    context.getCommandSender(),
                    TextType.ERROR,
                    "Invalid severity. Severity must be <h>between or equal to 1</h> and <h>3</h>."
                );
                return;
              }
            }
          }

          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Attempting to punish <h>{0}</h>.",
              targetName
          );

          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            UUID uuid = this.plugin.getCommons().getOfflineDataManager().getUuid(targetName);
            if (uuid == null) {
              TextController.send(
                  context.getCommandSender(),
                  TextType.ERROR,
                  "Player <h>{0}</h> could not be found.",
                  targetName
              );
              return;
            }

            Punishment punishment = Punishment.newPunishment(
                context.getCommandSender() instanceof Player ?
                    context.getSenderAsPlayer().getUniqueId() : UUIDUtil.CONSOLE_UUID,
                uuid,
                type,
                severity
            );

            this.plugin.getCommons().getOfflineDataManager().addPunishment(uuid, punishment);

            ErisPlayer erisPlayer =
                this.plugin.getCommons().getErisPlayerManager().getPlayer(uuid);

            if (erisPlayer != null) {
              erisPlayer.getPunishmentProfile().addPunishment(punishment);
            }

            TextController.send(
                context.getCommandSender(),
                TextType.SUCCESS,
                "Successfully punished <h>{0}</h> (type is <h>{1}</h>, severity is <h>{2}</h>).",
                erisPlayer == null ? targetName : erisPlayer.getName(),
                type,
                severity
            );
          });
        }).finished();
  }
}
