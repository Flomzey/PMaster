package Flomezy.pMaster;

import org.bukkit.plugin.java.JavaPlugin;
import Flomezy.pMaster.TimedEvents.Events.TimedBackup;
import Flomezy.pMaster.TimedEvents.TimedEventHandler;
import java.io.File;
import java.time.Duration;
import java.util.logging.Logger;

public final class PMaster extends JavaPlugin {

    final Logger logger = this.getLogger();
    TimedEventHandler eventHandler = new TimedEventHandler(logger);

    @Override
    public void onEnable() {

        logger.info("Setting up running Threads...");

        eventHandler.addEvent(
                new TimedBackup(Duration.ofSeconds(10),
                logger,
                new String[] {"world"},
                new File("world/"),
                new File("backups/world.zip")));

        eventHandler.runTaskTimer(this, 0, 200);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        eventHandler.stop();
        logger.info("Goodbye");
    }
}
