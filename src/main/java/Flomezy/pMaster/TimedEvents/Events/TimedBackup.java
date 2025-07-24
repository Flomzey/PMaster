package Flomezy.pMaster.TimedEvents.Events;

import Flomezy.pMaster.TimedEvents.TimedEvent;
import Flomezy.pMaster.TimedEvents.Util.ZipUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
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
    private final String[] worldNames;
    private final File pathToBackup, destOfBackup;

    public TimedBackup(Duration sleepTimeS, Logger logger, String[] worldNames, File pathToBackup, File saveBackupPath){
        this.logger = logger;
        this.worldNames = worldNames;
        this.sleepPeriodCount = sleepTimeS.toSecondsPart();
        periodsUntilEventTask = sleepTimeS.toSecondsPart();
        this.pathToBackup = pathToBackup;
        this.destOfBackup = saveBackupPath;
    }

    @Override
    public void eventTask() {
        File worldContainer = Bukkit.getWorldContainer();

        for(String name : worldNames){
            World currentWorld = Bukkit.getWorld(name);
            if(currentWorld == null) {
                printToConsole( name + " doesn't exist or is already unloaded | not updating");
                continue;
            }

            printToConsole("Saving: " + name);

            currentWorld.save();

            printToConsole("Backing up files of: " + name);

            new Thread(() -> { //to not impact server performance -> Thread
                try {
                    File copyPath = new File(worldContainer + "/" +name);
                    File destOfCopy = new File( "backups/" + name +"_copy");

                    printToConsole("Copying files...");
                    zipUtil.copy(copyPath, destOfCopy);

                    printToConsole("Zipping files...");
                    zipUtil.zip(destOfCopy, destOfBackup);

                    printToConsole("Deleting copies...");
                    zipUtil.del(destOfCopy);

                    printToConsole("Backup of " + name + " successful!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        resetPeriods();
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
        logger.info("["+eventName+"] " + msg);
    }

    @Override
    public void resetPeriods() {
        periodsUntilEventTask = sleepPeriodCount;
    }
}
