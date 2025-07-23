package Flomezy.pMaster;

import Flomezy.pMaster.TimedEvents.Events.TimedBackup;
import Flomezy.pMaster.TimedEvents.TimedEventHandler;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.Duration;
import java.util.logging.Logger;

public final class PMaster extends JavaPlugin {

    final Logger logger = this.getLogger();
    Thread thread;
    TimedEventHandler eventHandler = new TimedEventHandler(logger);

    @Override
    public void onEnable() {

        logger.info("setting up running Threads . . .");

        eventHandler.addEvent(
                new TimedBackup(Duration.ofSeconds(10),
                logger,
                new File("world/"),
                new File("backups/world.zip")));

        thread = new Thread(eventHandler);

        thread.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        eventHandler.stop();
        Bukkit.getLogger().info("This is a test..................");
    }
}
