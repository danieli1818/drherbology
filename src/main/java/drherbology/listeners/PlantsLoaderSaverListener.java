package drherbology.listeners;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import drherbology.management.PlantsManager;

public class PlantsLoaderSaverListener implements Listener {

	private static PlantsLoaderSaverListener instance;
	
	private PlantsLoaderSaverListener() {
		
	}
	
	public static PlantsLoaderSaverListener getInstance() {
		if (instance == null) {
			instance = new PlantsLoaderSaverListener();
		}
		return instance;
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		PlantsManager.getInstance().loadChunk(chunk);
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		Chunk chunk = event.getChunk();
		PlantsManager.getInstance().saveChunk(chunk);
	}
	
}
