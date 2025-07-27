package Flomezy.pMaster;

import org.bukkit.plugin.java.JavaPlugin;
import Flomezy.pMaster.TimedEvents.Events.TimedBackup;
import Flomezy.pMaster.TimedEvents.TimedEventHandler;
import java.util.logging.Logger;

public final class PMaster extends JavaPlugin {

    final Logger logger = this.getLogger();
    TimedEventHandler eventHandler = new TimedEventHandler(logger);

    @Override
    public void onEnable() {

        saveDefaultConfig();

        logger.info("Setting up running Threads...");

        eventHandler.add(new TimedBackup(this));

        eventHandler.runTaskTimer(this, 20, 30);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        eventHandler.stop();
        logger.info("Goodbye");
    }
}
