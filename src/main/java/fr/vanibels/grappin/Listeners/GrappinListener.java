package fr.vanibels.grappin.Listeners;

import fr.vanibels.grappin.Grappin;
import fr.vanibels.grappin.Utils.Colorize;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class GrappinListener implements Listener {
    Location loc;
    @EventHandler
    public void onGrappin(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getItem().getType() == Material.FISHING_ROD) {
                if (!e.getItem().getPersistentDataContainer().getOrDefault(Grappin.getGrappinKey(), PersistentDataType.BOOLEAN, false)) {
                    e.getPlayer().sendMessage(Colorize.prefixed("<red>>Cet item n'est pas un grappin</red>"));
                } else {
                    e.getPlayer().sendMessage(Colorize.prefixed("<lime>Cet item est un grappin</lime>"));
                }
            }
        }
    }

    @EventHandler
    public void onHookHitBlock(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof FishHook hook)) {
            return;
        }
        Block hitBlock = event.getHitBlock();
        if (hitBlock == null) {
            return;
        }
        if (hook.getShooter() instanceof Player player) {
            if (!player.getInventory().getItemInMainHand().getPersistentDataContainer().getOrDefault(Grappin.getGrappinKey(), PersistentDataType.BOOLEAN, false)) return;
            player.sendMessage(Colorize.prefixed("<lime>Cet item est un grappin</lime>"));
            loc = hitBlock.getLocation();
        }
    }

    @EventHandler
    public void OnGrappinShoot(PlayerFishEvent e){
        Player player = e.getPlayer();
        if (!player.getInventory().getItemInMainHand().getPersistentDataContainer().getOrDefault(Grappin.getGrappinKey(), PersistentDataType.BOOLEAN, false)) return;
        player.sendMessage(Colorize.prefixed("<lime>Cet item est un grappin</lime>"));
        if (e.getState() == PlayerFishEvent.State.IN_GROUND){
            if (loc == null){player.sendMessage(Colorize.prefixed("<red>Erreur de calcul de localisation (NullPointerExecption)</red>")); return;}
            loc.add(0.5,1.0,0.5);
            Location loc1 = player.getLocation();
            Vector direction = loc.toVector().subtract(loc1.toVector());
            double speed = 1.8;
            direction.normalize().multiply(speed);
            direction.setY(direction.getY() + 0.3);
            player.setVelocity(direction);

        }
    }
}

