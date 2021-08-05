package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.util.CC;
import gg.eris.commons.bukkit.util.PlayerUtil;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HubCommand implements CommandProvider {

    private static final String LOBBY_HANDLE = "lobby";

    @Override
    public Builder getCommand(CommandManager manager) {
        return manager.newCommandBuilder(
                "hub",
                "returns the player to the main hub",
                ErisCoreIdentifiers.HUB_PERMISSION,
                "lobby"
        ).noArgsHandler(context -> {
            PlayerUtil.sendToServer(context.getSenderAsPlayer(), LOBBY_HANDLE);
        }, true);
    }
}
