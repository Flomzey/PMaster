package Flomezy.pMaster.TimedEvents.Events;

import Flomezy.pMaster.TimedEvents.TimedEvent;
import Flomezy.pMaster.TimedEvents.Util.ZipUtil;
import java.io.File;
import java.time.Duration;
import java.util.logging.Logger;


/**
 * @author flomzey
 */
public class TimedBackup implements TimedEvent {

    //TODO: add properties file to edit variables

    private final int sleepTimeS;
    private final Logger logger;
    private final ZipUtil zipUtil;
    private int timeUntilEventTask;
    private final String eventName = this.getClass().getSimpleName();

    public TimedBackup(Duration sleepTimeS, Logger logger, File pathToBackup, File saveBackupPath){
        this.sleepTimeS = sleepTimeS.toSecondsPart();
        timeUntilEventTask = sleepTimeS.toSecondsPart();
        this.logger = logger;
        zipUtil = new ZipUtil(pathToBackup, saveBackupPath);
    }

    @Override
    public boolean eventTask() {
        printToConsole("Backing up files");

        //Zip directory here

        resetTimeUnitlEventTaskS();
        return true;
    }

    @Override
    public int getDurationS() {
        return sleepTimeS;
    }

    @Override
    public int getTimeUntilEventTaskS() {
        return timeUntilEventTask;
    }

    @Override
    public void subtractTimeUntilEventTaskS() {
        timeUntilEventTask--;
    }

    @Override
    public void printToConsole(String msg) {
        logger.info("["+eventName+"] " + msg);
    }

    @Override
    public void resetTimeUnitlEventTaskS() {
        timeUntilEventTask = sleepTimeS;
    }
}
