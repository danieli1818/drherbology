package drherbology.plants.harvest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemStackHarvestRewardFunction extends AbstractHarvestRewardFunction implements HarvestRewardFunction {

	private List<ItemStack> itemStacks;
	
	public ItemStackHarvestRewardFunction(Collection<ItemStack> itemStacks) throws NullArgumentException {
		super();
		this.itemStacks = new ArrayList<>(itemStacks);
	}
	
	@Override
	public void run(Player player) {
		giveItemStacksToPlayer(player);
	}
	
	private boolean giveItemStacksToPlayer(Player player) {
		boolean returnValue = true;
		for (ItemStack itemStack : this.itemStacks) {
			Map<Integer, ItemStack> itemStacksRemainder = player.getInventory().addItem(new ItemStack(itemStack));
			if (!itemStacksRemainder.isEmpty()) {
				for (ItemStack itemStackRemainder : itemStacksRemainder.values()) {
					Location playerLocation = player.getLocation();
					playerLocation.getWorld().dropItemNaturally(playerLocation, new ItemStack(itemStackRemainder));
				}
			}
		}
		return returnValue;
	}

}
