package drherbology.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import drguis.guis.GUI;
import drherbology.management.PlantsGUIManager;
import drherbology.plants.PlantsTypes;
import drherbology.utils.messages.MessagesSender;
import drherbology.utils.messages.MessagesStorage;

public class DRHerbologyCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			MessagesSender.getInstance().sendErrorMessage(MessagesStorage.getInstance().getMessage("only_players_command_message"), sender);
		}
		Player player = (Player)sender;
		GUI gui = PlantsGUIManager.getInstance().getGUI();
		if (gui == null) {
			gui = PlantsGUIManager.getInstance().loadGUI(PlantsTypes.getInstance().getPlantTypes());
		}
		player.openInventory(gui.getInventory(player));
		return true;
	}

}
