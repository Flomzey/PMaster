package Flomezy.pMaster;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import Flomezy.pMaster.TimedEvents.Events.TimedBackup;
import Flomezy.pMaster.TimedEvents.TimedEventHandler;

import java.io.IOException;
import java.util.logging.Logger;

public final class PMaster extends JavaPlugin {

    final Logger logger = this.getLogger();
    TimedEventHandler eventHandler = new TimedEventHandler(logger);
    final Initialize initializer = new Initialize(this);

    @Override
    public void onEnable() {

        saveDefaultConfig();

        try {

            initializer.createConfigFiles();
            eventHandler.add(new TimedBackup(this));
        } catch (IOException | InvalidConfigurationException e) {
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
}
