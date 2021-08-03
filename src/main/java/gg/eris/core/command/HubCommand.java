package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.util.CC;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HubCommand implements CommandProvider {

    @Override
    public Builder getCommand(CommandManager manager) {
        return manager.newCommandBuilder(
                "hub",
                "returns the player to the main hub",
                ErisCoreIdentifiers.HUB_PERMISSION,
                "lobby"
        ).noArgsHandler(context -> {
            Player player = context.getSenderAsPlayer();
            if(ErisCore.teleportHubList.containsKey(player)){
                player.sendMessage(CC.RED.bold() + "Teleport cancelled!");
                ErisCore.teleportHubList.get(player).cancel();
                ErisCore.teleportHubList.remove(player);
            } else{
                player.sendMessage(CC.RED.bold() + "Teleporting to hub in 3 seconds....");
                BukkitTask timer = new BukkitRunnable() {
                    @Override
                    public void run() {
                        //teleport player to hub
                    }
                }.runTaskLater(ErisCore.getPlugin(ErisCore.class), 20*3);
                ErisCore.teleportHubList.put(player, timer);
            }
        });
    }
}
