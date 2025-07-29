package events.listeners;

import events.EventPublisher;
import events.GameEvent;
import events.IEventListener;
import events.SoundManager;


public class MovesLogger implements IEventListener {

    public MovesLogger(){
        EventPublisher.getInstance().subscribe(GameEvent.PIECE_MOVED, this);
    }

    @Override
    public void onEvent(GameEvent event) {
        SoundManager.playSound("move.wav");
    }
}
