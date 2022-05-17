package drherbology;

import org.bukkit.plugin.java.JavaPlugin;

import drherbology.commands.DRHerbologyCommands;
import drherbology.listeners.GeneralPlantsListener;
import drherbology.listeners.ModelPlantsListener;
import drherbology.listeners.PlantsLoaderSaverListener;
import drherbology.management.PlantsManager;
import drherbology.plants.PlantsTypes;
import drherbology.plants.conditions.harvest.HarvestConditionsManager;
import drherbology.utils.SchedulerUtils;
import drherbology.utils.files.ConfigurationsHelper;
import drherbology.utils.files.FileConfigurationsManager;
import drherbology.utils.messages.MessagesSender;
import drherbology.utils.messages.MessagesStorage;
import drherbology.utils.reloader.ReloaderManager;

public class DRHerbology extends JavaPlugin {

	@Override
	public void onEnable() {
		super.onEnable();
		
		String configFilename = "config.yml";
		String messagesFilename = "messages.yml";
		String typesFolder = "types";
		FileConfigurationsManager fcm = FileConfigurationsManager.getInstance(this);
		fcm.createConfigurationFile(configFilename);
		fcm.createConfigurationFile(messagesFilename);
		fcm.createConfigurationFile(typesFolder + "/example_plant.yml");
		fcm.loadDirectory("types", true);
		fcm.reloadAllFiles();
		SchedulerUtils.getInstance(this);
		String messagesPrefix = ConfigurationsHelper.getInstance().getString(configFilename, "messages_prefix");
		String errorMessagesPrefix = ConfigurationsHelper.getInstance().getString(configFilename, "error_messages_prefix");
		MessagesSender.getInstance(messagesPrefix, errorMessagesPrefix);
		MessagesStorage.getInstance().setConfigFilename(messagesFilename);
		
		getServer().getPluginManager().registerEvents(GeneralPlantsListener.getInstance(), this);
		
		getServer().getPluginManager().registerEvents(HarvestConditionsManager.getInstance(), this);
		
		getServer().getPluginManager().registerEvents(ModelPlantsListener.getInstance(), this);
		
		getServer().getPluginManager().registerEvents(PlantsLoaderSaverListener.getInstance(), this);
		
		ReloaderManager.getInstance().registerReloadable(PlantsTypes.getInstance());
		
//		FileConfiguration examplePlantTypeFileConfiguration = fcm.getFileConfiguration(typesFolder + "/example_plant.yml");
//		
//		Object examplePlantType = examplePlantTypeFileConfiguration.get(examplePlantTypeFileConfiguration.getKeys(false).iterator().next());
//		
//		if (examplePlantType instanceof MemorySection) {
//			System.out.println("ExamplePlantType is MemorySection!");
//			PlantsTypesParser.getInstance().parse((MemorySection)examplePlantType);
//		}
		
//		PlantsTypes.getInstance().loadExamplePlant();
		
		System.out.println(FileConfigurationsManager.getInstance());
		System.out.println(FileConfigurationsManager.getInstance().getFilenamesUnderDirectory(typesFolder, true));
		for (String typeFilename : FileConfigurationsManager.getInstance().getFilenamesUnderDirectory(typesFolder, true)) {
			PlantsTypes.getInstance().loadPlant(typeFilename);
		}
		
		getCommand("drherbology").setExecutor(new DRHerbologyCommands());
		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		PlantsManager.getInstance().saveAll();
		
	}
	
}
