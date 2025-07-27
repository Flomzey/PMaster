package Flomezy.pMaster;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import Flomezy.pMaster.TimedEvents.Events.TimedBackup;
import Flomezy.pMaster.TimedEvents.TimedEventHandler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class PMaster extends JavaPlugin {

    final Logger logger = this.getLogger();
    TimedEventHandler eventHandler = new TimedEventHandler(logger);

    @Override
    public void onEnable() {

        saveDefaultConfig();
        try {
            createFiles();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        logger.info("Setting up running Threads...");

        try {
            eventHandler.add(new TimedBackup(this));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        eventHandler.runTaskTimer(this, 20, 30);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        eventHandler.stop();
        logger.info("Goodbye");
    }

    public void createFiles() throws IOException, InvalidConfigurationException {
        FileConfiguration pluginConfig = getConfig();

        for (String key : pluginConfig.getConfigurationSection("plugin-enable").getKeys(false)) {
            File configF = new File(getDataFolder(), key + "/" + key + "_default.yml");

            if (!configF.exists()) {
                configF.getParentFile().mkdirs();
                saveResource(key + "/" + key + "_default.yml", false);
            }

            FileConfiguration config = new YamlConfiguration();
            config.load(configF);
        }
    }
}
