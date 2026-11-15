package fr.vanibels.grappin.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

/**
 * Classe utilitaire pour colorizer les text en minimessages (components)
 */

public class Colorize {
    private static final MiniMessage minimessage = MiniMessage.miniMessage();
    private static final String PREFIX = "<b><color:#A5E60A>Grapin</color></b> <gray>»</gray> ";

    public static Component colorize(String message) {
        return minimessage.deserialize(message);
    }

    /**
     * Message standard préfixé par "hAppolon" (modèle utilisé pour tous les messages joueur).
     * ex: Colorize.prefixed("<red>Joueur introuvable</red>")
     */
    public static Component prefixed(String message) {
        return minimessage.deserialize(PREFIX + message);
    }

}
