package Flomezy.pMaster.TimedEvents;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class TimedEventHandler extends BukkitRunnable {

    private List<TimedEvent> events = new ArrayList<>();
    public boolean running = false;
    private final Logger logger;

    public TimedEventHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void run() {
        for(TimedEvent event : events){
            if(event.getPeriodsUntilNextEvent() > 0) {
                event.subtractOnePeriod();
                continue;
            }
            event.eventTask();
            event.resetPeriods();
        }
    }

    public void add(TimedEvent event){
        events.add(event);
    }

    public List<TimedEvent> getEvent() {
        return events;
    }

    public void stop() {
        this.running = false;
    }
}
