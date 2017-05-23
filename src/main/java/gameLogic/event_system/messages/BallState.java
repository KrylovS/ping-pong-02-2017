package gameLogic.event_system.messages;


import org.apache.commons.math3.linear.RealVector;

public class BallState {
    private RealVector position;
    private RealVector velocity;

    public BallState(RealVector position, RealVector velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public RealVector getPosition() {
        return position;
    }

    public RealVector getVelocity() {
        return velocity;
    }
}
