package app.stral.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static app.stral.bukkit.StralBukkitPlugin.BASE_API_URL;
import static app.stral.bukkit.StralBukkitPlugin.missingSecret;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(missingSecret && event.getPlayer().isOp()) {
            event.getPlayer().sendMessage(
                    "⚠ No API key set!, Run /stral link <api-key> to configure." +
                            " Get a key at: " + BASE_API_URL + ""
            );
        }
    }
}
