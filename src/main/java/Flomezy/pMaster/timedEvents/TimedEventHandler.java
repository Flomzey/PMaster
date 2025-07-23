package Flomezy.pMaster.timedEvents;

public class TimedEventHandler implements Runnable{

    private TimedEvent event;
    private boolean running = true;

    public TimedEventHandler(TimedEvent event){
        this.event = event;
    }

    @Override
    public void run() {
        while (running){
            try{
                event.eventTask();
                Thread.sleep(event.getDurationMs());
            }catch(Exception e){
                //TODO: add exeption handling
            }
        }
    }

    public TimedEvent getEvent(){
        return event;
    }
}
