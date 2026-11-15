package fr.vanibels.grappin.Handlers;

import fr.vanibels.grappin.Grappin;
import fr.vanibels.grappin.Utils.Colorize;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NonNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Subcommand;

@Command({"travel","travelling"})
public class TravellingHandler {
    Location loc1;
    Location loc2;

    @CommandPlaceholder
    public void travel(Player sender, String[] args) {}

    @Subcommand("pos1")
    public void pos1(Player sender) {
        loc1 = sender.getLocation();
        sender.sendMessage(Colorize.prefixed("<green>Première position définis</green>"));
    }

    @Subcommand("pos2")
    public void pos2(Player sender) {
        loc2 = sender.getLocation();
        sender.sendMessage(Colorize.prefixed("<green>Séconde position définis</green>"));
    }

    @Subcommand("travel")
    void travel(Player sender) {
        if (loc1 == null || loc1.getWorld() != sender.getWorld()){
            sender.sendMessage(Colorize.prefixed("<red>Localisation 1 invalide : /travel pos1</red>"));
            return;
        }
        if (loc2 == null || loc2.getWorld() != sender.getWorld()){
            sender.sendMessage(Colorize.prefixed("<red>Localisation 2 invalide : /travel pos2</red>"));
            return;
        }
        sender.sendMessage(Colorize.prefixed("<yellow>Début du </yellow><orange>travelling</orange>"));
        sender.setGameMode(GameMode.SPECTATOR);
        sender.teleport(loc1);
        smoothTransition(Grappin.getPlugin(),sender, loc1, loc2,() -> {
            sender.sendMessage(Colorize.prefixed("<yellow>Fin du </yellow><orange>travelling</orange>"));
            sender.playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        } );
        /*Location start = loc1;
        Location end = loc2;
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double dz = end.getZ() - start.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);
        double speed = Math.min(0.5, distance * 0.18);
        double arcHeight = 0.5 + (distance * 0.05);
        Vector velocity = new Vector(dx, 0, dz).normalize().multiply(speed);
        velocity.setY((dy * 0.15) + arcHeight);
        sender.setVelocity(velocity);*/

    }

    public void smoothTransition(Plugin plugin, Player player, Location start, Location end, Runnable onFinish) {
        World world = start.getWorld();
        if (world == null || !world.equals(end.getWorld())) {
            player.teleport(end);
            if (onFinish != null) onFinish.run();
            return;
        }

        final Location target = end.clone().add(0.5, 1.0, 0.5);
        final double totalDistance = start.distance(target);
        final int durationTicks = Math.max(10, (int) (totalDistance * 2.0));
        final double arcHeight = 1.5 + (totalDistance * 0.1);
        final double maxSpeed = 3.5;

        final boolean hadGravity = player.hasGravity();
        player.setGravity(false);

        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (!player.isOnline() || player.isDead() || !player.getWorld().equals(world)) {
                    finish(false);
                    return;
                }

                if (tick >= durationTicks) {
                    finish(true);
                    return;
                }
                Vector velocity = getVelocity();

                if (velocity.lengthSquared() > maxSpeed * maxSpeed) {
                    velocity.normalize().multiply(maxSpeed);
                }

                player.setVelocity(velocity);
                player.setFallDistance(0);
                tick++;
            }

            private @NonNull Vector getVelocity() {
                double linear = (double) tick / durationTicks;
                double progress = linear * linear * (3.0 - 2.0 * linear);

                double x = start.getX() + (target.getX() - start.getX()) * progress;
                double y = start.getY() + (target.getY() - start.getY()) * progress
                        + Math.sin(linear * Math.PI) * arcHeight;
                double z = start.getZ() + (target.getZ() - start.getZ()) * progress;

                Location current = player.getLocation();
                Vector velocity = new Vector(x - current.getX(), y - current.getY(), z - current.getZ());
                return velocity;
            }

            private void finish(boolean completed) {
                cancel();
                player.setGravity(hadGravity);
                player.setVelocity(new Vector(0, 0, 0));
                player.setFallDistance(0);

                if (completed && player.isOnline() && !player.isDead()) {
                    // Position finale exacte, on garde le regard du joueur
                    Location finalLoc = target.clone();
                    finalLoc.setYaw(player.getLocation().getYaw());
                    finalLoc.setPitch(player.getLocation().getPitch());
                    player.teleport(finalLoc);
                }

                if (onFinish != null) onFinish.run();
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

}
