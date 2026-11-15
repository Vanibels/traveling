package fr.vanibels.grappin.Managers;

import fr.vanibels.grappin.Grappin;
import fr.vanibels.grappin.Handlers.GetGrappin;
import fr.vanibels.grappin.Handlers.TravellingHandler;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLampConfig;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class CommandsManager {

    static Lamp handlers;

    public static void init() {
        handlers = Lamp.<BukkitCommandActor>builder()
                .accept(BukkitLampConfig.createDefault(Grappin.getPlugin()))
                .build();
        registerCommand();
    }

    public static void registerCommand(){
        handlers.register(
                new GetGrappin(),
                new TravellingHandler()
        );
    }

}
