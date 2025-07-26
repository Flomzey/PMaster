package Flomezy.pMaster.TimedEvents.Events;

import Flomezy.pMaster.TimedEvents.TimedEvent;
import Flomezy.pMaster.TimedEvents.Util.ZipUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author flomzey
 */
public class TimedBackup implements TimedEvent {

    //TODO: add properties file to edit variables

    private final int sleepPeriodCount;
    private final Logger logger;
    public final ZipUtil zipUtil = new ZipUtil();
    private int periodsUntilEventTask;
    private final String eventName = this.getClass().getSimpleName();
    private final List<World> worldsToSave = new ArrayList<>();
    private final File backupDest, backupZipName;

    public TimedBackup(int sleepTimeS, Logger logger, String[] worldNames, File saveBackupPath) {
        this.logger = logger;
        for (String name : worldNames) worldsToSave.add(Bukkit.getWorld(name));
        this.sleepPeriodCount = sleepTimeS;
        periodsUntilEventTask = sleepTimeS;
        this.backupDest = saveBackupPath;
        this.backupZipName = new File(backupDest + "/current_backup.zip");
    }

    @Override
    public void eventTask() {
        File worldContainer = Bukkit.getWorldContainer();
        File toBeZipped = new File(backupDest + "/backup");

        printToConsole("Saving worlds...");

        worldsToSave.forEach(World::save);

        new Thread(() -> {
            worldsToSave.forEach(world -> {
                File copyPath = new File(worldContainer + "/" + world.getName());
                File destOfCopy = new File(toBeZipped + "/" + world.getName());
                printToConsole("Copying "+world.getName()+"...");
                try {
                    zipUtil.copy(copyPath, destOfCopy);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            printToConsole("Zipping backup...");
            try{
                zipUtil.zip(toBeZipped, backupZipName);
                printToConsole("Deleting temporary files...");
                zipUtil.del(toBeZipped);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            printToConsole("Backup finished successfully!");
        }).start();
    }

    @Override
    public int getDurationPeriodCount() {
        return sleepPeriodCount;
    }

    @Override
    public int getPeriodsUntilNextEvent() {
        return periodsUntilEventTask;
    }

    @Override
    public void subtractOnePeriod() {
        periodsUntilEventTask--;
    }


    @Override
    public void printToConsole(String msg) {
        logger.info("[" + eventName + "] " + msg);
    }

    @Override
    public void resetPeriods() {
        periodsUntilEventTask = sleepPeriodCount;
    }
}
