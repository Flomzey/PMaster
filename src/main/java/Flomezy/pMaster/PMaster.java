package Flomezy.pMaster;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import Flomezy.pMaster.TimedEvents.Events.TimedBackup;
import Flomezy.pMaster.TimedEvents.TimedEventHandler;
import java.io.File;
import java.util.logging.Logger;

public final class PMaster extends JavaPlugin {

    final Logger logger = this.getLogger();
    TimedEventHandler eventHandler = new TimedEventHandler(logger);

    @Override
    public void onEnable() {

        saveDefaultConfig();

        logger.info("Setting up running Threads...");

        FileConfiguration config = this.getConfig();


        logger.info(config.getString("backup.worlds"));

        logger.info(config.getString("backup.save-dir"));

        eventHandler.addEvent(
                new TimedBackup(10,
                logger,
                new String[] {"world", "world_the_end", "world_nether"},
                new File("backups/")));

        eventHandler.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        eventHandler.stop();
        logger.info("Goodbye");
    }
}
