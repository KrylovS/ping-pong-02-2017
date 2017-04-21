package gameLogic.game;


import gameLogic.game.events.SectorCollisionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
public class GameEventListener {
    @Autowired
    private ApplicationEventPublisher publisher;

    @EventListener
    public void emitEvent(double angle) {
        final SectorCollisionEvent event = new SectorCollisionEvent();
        event.setPlatformAngle(angle);
        publisher.publishEvent(event);
    }

    @EventListener
    public void handleSectorCollisionEvent(SectorCollisionEvent event) {
        System.out.print("Caught event! Angle: ");
        System.out.println(event.getPlatformAngle());
    }

}
