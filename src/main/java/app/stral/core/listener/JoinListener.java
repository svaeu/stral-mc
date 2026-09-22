package app.stral.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static app.stral.core.StralPluginPlatform.BASE_API_URL;
import static app.stral.core.StralPluginPlatform.missingSecret;

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
