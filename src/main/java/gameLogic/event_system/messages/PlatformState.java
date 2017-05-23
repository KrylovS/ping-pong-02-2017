package gameLogic.event_system.messages;


import org.apache.commons.math3.linear.RealVector;

public class PlatformState {
    private RealVector position;
    private double angle;
    private RealVector velocity;
    private boolean isActive;

    public PlatformState(RealVector position, double angle, RealVector velocity, boolean isActive) {
        this.position = position;
        this.angle = angle;
        this.velocity = velocity;
        this.isActive = isActive;
    }

    public RealVector getPosition() {
        return position;
    }

    public double getAngle() {
        return angle;
    }

    public RealVector getVelocity() {
        return velocity;
    }

    public boolean isActive() {
        return isActive;
    }
}
