package drherbology.utils.items;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemsUtils {

	public static boolean giveItemStacksToPlayer(Player player, Collection<ItemStack> itemStacks) {
		if (itemStacks == null) {
			return false;
		}
		for (ItemStack itemStack : itemStacks) {
			Map<Integer, ItemStack> itemStacksRemainder = player.getInventory().addItem(new ItemStack(itemStack));
			if (!itemStacksRemainder.isEmpty()) {
				for (ItemStack itemStackRemainder : itemStacksRemainder.values()) {
					Location playerLocation = player.getLocation();
					playerLocation.getWorld().dropItemNaturally(playerLocation, new ItemStack(itemStackRemainder));
				}
			}
		}
		return true;
	}
	
}
