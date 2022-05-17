package drherbology.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import drherbology.management.PlantsManager;
import drherbology.utils.messages.MessagesSender;
import drherbology.utils.messages.MessagesStorage;

public class DRHerbologyCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			MessagesSender.getInstance().sendErrorMessage(MessagesStorage.getInstance().getMessage("only_players_command_message"), sender);
		}
		Player player = (Player)sender;
		Block lookingAtBlock = player.getTargetBlock(null, 10);
		if (lookingAtBlock == null || lookingAtBlock.getType() == Material.AIR) {
			return false;
		}
		Block block = lookingAtBlock.getRelative(BlockFace.UP);
		if (block == null || block.getType() != Material.AIR) {
			return false;
		}
		System.out.println("Before creating plant!");
		PlantsManager.getInstance().createPlant(block.getLocation(), "example_plant");
		System.out.println("After creating plant!");
		return true;
	}

}
