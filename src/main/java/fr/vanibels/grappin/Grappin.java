package fr.vanibels.grappin;

import fr.vanibels.grappin.Listeners.GrappinListener;
import fr.vanibels.grappin.Managers.CommandsManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Grappin extends JavaPlugin {
    private static Grappin instance;
    public static NamespacedKey grappinKey;

    @Override
    public void onEnable() {
        instance = this;
        grappinKey = new NamespacedKey(this, "isgrappin");
        CommandsManager.init();
        getServer().getPluginManager().registerEvents(new GrappinListener(),this);
    }

    @Override
    public void onDisable() {
    }

    public static @NotNull JavaPlugin getPlugin() {
        return instance;
    }

    public static NamespacedKey getGrappinKey() {
        return grappinKey;
    }
}
