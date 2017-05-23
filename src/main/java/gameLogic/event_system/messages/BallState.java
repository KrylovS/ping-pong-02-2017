package gameLogic.event_system.messages;


import gameLogic.base.GeometryOperations;
import org.apache.commons.math3.linear.RealVector;

public class BallState {
    private RealVector position;
    private RealVector velocity;

    public BallState(RealVector position, RealVector velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public BallState rotate(double angle) {
        return new BallState(
                GeometryOperations.rotate(position, angle),
                GeometryOperations.rotate(velocity, angle)
        );
    }

    public RealVector getPosition() {
        return position;
    }

    public RealVector getVelocity() {
        return velocity;
    }
}
