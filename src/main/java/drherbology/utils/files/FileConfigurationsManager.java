package drherbology.utils.files;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class FileConfigurationsManager {

	private Set<String> configurationFilesNames;
	private JavaPlugin plugin;
	private Map<String, FileConfiguration> configurations;
	
	private static FileConfigurationsManager instance = null;
	
	private FileConfigurationsManager(JavaPlugin plugin) {
		this.configurationFilesNames = new HashSet<String>();
		this.configurations = new HashMap<String, FileConfiguration>();
		this.plugin = plugin;
	}
	
	public static FileConfigurationsManager getInstance(JavaPlugin plugin) {
		if (instance == null) {
			instance = new FileConfigurationsManager(plugin);
		}
		return instance;
	}
	
	public static FileConfigurationsManager getInstance() {
		return instance;
	}
	
	public void reloadFiles(Collection<String> filesNames) {
		for (String fileName : filesNames) {
			reloadFile(fileName);
		}
	}
	
	public FileConfiguration reloadFile(String fileName) {
		if (!doesFileExist(fileName)) {
			return null;
		}
		File configFile = getFileOrDirectory(fileName);
		return loadFile(configFile, true);
	}
	
	private FileConfiguration loadFile(File file, boolean isReloading) {
		if (file == null) {
			return null;
		}
		String fileName = getRelativePath(file);
		if (!isReloading) {
			configurationFilesNames.add(fileName);
		}
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
			this.configurations.put(fileName, config);
			return config;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getRelativePath(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		String absoluteFilePath = file.getAbsolutePath();
		System.out.println("Absolute File Path: " + absoluteFilePath);
		System.out.println("Replace String: " + this.plugin.getDataFolder().getAbsolutePath() + File.separator);
		System.out.println("Relative File Path: " + absoluteFilePath.replace(this.plugin.getDataFolder().getAbsolutePath() + File.separator, ""));
		return absoluteFilePath.replace(this.plugin.getDataFolder().getAbsolutePath() + File.separator, "");
	}
	
	public void createConfigurationFiles(Collection<String> fileNames) {
		for (String fileName : fileNames) {
			createConfigurationFile(fileName);
		}
	}
	
	public boolean createConfigurationFile(String name) {
		File configFile = getFileOrDirectory(name);
		this.configurationFilesNames.add(name);
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			if (this.plugin.getResource(name) != null) {
				this.plugin.saveResource(name, false);
			} else {
				createFile(configFile);
			}
			return false;
		}
		return true;
	}
	
	private boolean createFile(File file) {
		if (file == null) {
			return false;
		}
		if (file.exists()) {
			return true;
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean doesFileExist(String name) {
		return getFileOrDirectory(name).exists();
	}
	
	public void reloadAllFiles() {
		reloadFiles(this.configurationFilesNames);
		System.out.println(this.configurationFilesNames);
	}
	
	public FileConfiguration getFileConfiguration(String name) {
		return this.configurations.get(name);
	}
	
	public void registerConfigurationSerializables(Collection<Class<? extends ConfigurationSerializable>> classes) {
		for (Class<? extends ConfigurationSerializable> class1 : classes) {
			ConfigurationSerialization.registerClass(class1);
		}
	}
	
	public boolean saveFileConfigurationToFile(String filename) {
		FileConfiguration conf = getFileConfiguration(filename);
		try {
			conf.save(getFileOrDirectory(filename));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void clearFileConfigurationOfFile(String filename) {
		FileConfiguration conf = getFileConfiguration(filename);
		for (String key : conf.getKeys(false)) {
			conf.set(key, null);
		}
	}
	
	private File getFileOrDirectory(String path) {
		return new File(this.plugin.getDataFolder(), path);
	}
	
	public boolean loadDirectory(String directoryPath, boolean isRecursive) {
		File directory = getFileOrDirectory(directoryPath);
		if (!directory.exists() || !directory.isDirectory()) {
			return false;
		}
		loadDirectory(directory, isRecursive);
		return true;
	}
	
	/**
	 * Loads the directory recursively or not recursively
	 * @param directory   a valid directory
	 * @param isRecursive whether to run recursively or not
	 */
	private void loadDirectory(File directory, boolean isRecursive) {
		if (isRecursive) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					loadDirectory(file, isRecursive);
				} else {
					loadFile(file, false);
				}
			}
		} else {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					continue;
				} else {
					loadFile(file, false);
				}
			}
		}
	}
	
	public Set<String> getFilenamesUnderDirectory(String directoryPath, boolean isRecursive) {
		File directory = getFileOrDirectory(directoryPath);
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			return null;
		}
		return getFilenamesUnderDirectory(directory, isRecursive);
	}
	
	private Set<String> getFilenamesUnderDirectory(File directory, boolean isRecursive) {
		return getFilesUnderDirectory(new HashSet<>(), directory, isRecursive)
				.stream().map((File file) -> getRelativePath(file)).collect(Collectors.toSet());
	}
	
	public Set<File> getFilesUnderDirectory(String directoryPath, boolean isRecursive) {
		File directory = getFileOrDirectory(directoryPath);
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			return null;
		}
		return getFilesUnderDirectory(new HashSet<>(), directory, isRecursive);
	}
	
	private Set<File> getFilesUnderDirectory(Set<File> files, File directory, boolean isRecursive) {
		if (isRecursive) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					getFilesUnderDirectory(files, file, isRecursive);
				} else {
					files.add(file);
				}
			}
		} else {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()) {
					continue;
				} else {
					files.add(file);
				}
			}
		}
		return files;
	}
	
	
}
