package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.text.ClickEvent;
import gg.eris.commons.bukkit.text.ClickEvent.Action;
import gg.eris.commons.bukkit.text.HoverEvent;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class UuidCommand implements CommandProvider {

  private final ErisCore plugin;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "uuid",
        "get a player's uuid",
        "uuid <player>",
        ErisCoreIdentifiers.UUID_PERMISSION
    ).withSubCommand()
        .argument(StringArgument.of("name"))
        .handler(context -> {
          String name = context.getArgument("name");
          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Looking up player with name <h>{0}</h>.",
              name
          );
          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            UUID uuid =
                this.plugin.getCommons().getErisPlayerManager().getOfflineDataManager()
                    .getUuid(name);

            if (uuid == null) {
              TextController.send(
                  context.getCommandSender(),
                  TextType.ERROR,
                  "Could not find a player with name <h>{0}</h>.",
                  name
              );
            } else {
              TextController.send(
                  context.getCommandSender(),
                  TextType.SUCCESS,
                  Int2ObjectMaps.singleton(0, ClickEvent.of(
                      Action.SUGGEST_TEXT,
                      uuid.toString()
                  )),
                  Int2ObjectMaps.singleton(0, HoverEvent.of(
                      "<col=yellow>Click to suggest</col>")
                  ),
                  "UUID found: <h><event=0>{0}</event></h>.",
                  uuid
              );
            }
          });
        }).finished();
  }
}
