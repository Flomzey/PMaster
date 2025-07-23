package Flomezy.pMaster.timedEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class TimedEventHandler implements Runnable{

    private List<TimedEvent> events = new ArrayList<>();
    public boolean running = true;
    private final Logger logger;

    public TimedEventHandler(Logger logger){
        this.logger = logger;
    }

    @Override
    public void run(){
        while(running){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for(TimedEvent event : events){
                event.subtractTimeUntilEventTaskS();
                if(event.getTimeUntilEventTaskS() != 0) continue;
                event.eventTask();
            }
        }
    }

    public void addEvent(TimedEvent newEvent){
        events.add(newEvent);
    }

    public List<TimedEvent> getEvent(){
        return events;
    }

    public void stop(){
        this.running = false;
    }
}
