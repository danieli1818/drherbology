package drherbology.plants.harvest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import drherbology.utils.items.ItemsUtils;

public class ItemStackHarvestRewardFunction extends AbstractHarvestRewardFunction implements HarvestRewardFunction {

	private List<ItemStack> itemStacks;
	
	public ItemStackHarvestRewardFunction(Collection<ItemStack> itemStacks) throws NullArgumentException {
		super();
		this.itemStacks = new ArrayList<>(itemStacks);
	}
	
	@Override
	public void run(Player player) {
		ItemsUtils.giveItemStacksToPlayer(player, this.itemStacks);
	}
	
}
