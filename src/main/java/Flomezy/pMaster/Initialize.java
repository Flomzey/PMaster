package Flomezy.pMaster;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Initialize {
    private final Plugin plugin;
    private final FileConfiguration pluginConfig;
    private final Logger logger;

    public Initialize(Plugin plugin) {
        this.plugin = plugin;
        pluginConfig = plugin.getConfig();
        this.logger = plugin.getLogger();
    }

    public void createConfigFiles() throws IOException, InvalidConfigurationException {
        for (String key : pluginConfig.getConfigurationSection("plugin-enable").getKeys(false)) {
            if (!plugin.getConfig().getBoolean("plugin-enable." + key)) {
                logger.info("Feature " + key + " disabled");
                continue;
            }
            File configF = new File(plugin.getDataFolder(), key + "/" + key + "_default.yml");
            if (!configF.exists()) {
                configF.getParentFile().mkdirs();
                plugin.saveResource(key + "/" + key + "_default.yml", false);
            }
            FileConfiguration config = new YamlConfiguration();
            config.load(configF);
        }
    }
}
