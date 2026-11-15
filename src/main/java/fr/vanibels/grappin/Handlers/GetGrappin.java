package fr.vanibels.grappin.Handlers;

import fr.vanibels.grappin.Grappin;
import fr.vanibels.grappin.Utils.Colorize;
import fr.vanibels.grappin.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;

@Command("grappin")
public class GetGrappin {

    @CommandPlaceholder
    void grappin(Player player) {
        ItemStack grap = new ItemBuilder(Material.FISHING_ROD).setName("&6Grappin").setLore("&cGrappin","&cItem magique").toItemStack();
        grap.editPersistentDataContainer(pdc -> {
            pdc.set(Grappin.getGrappinKey(), PersistentDataType.BOOLEAN,true);
        });
        player.getInventory().addItem(grap);
        player.sendMessage(Colorize.prefixed("Grappin ajouté a votre inventaire, click droit pour l'utiliser"));
    }
}
