package gameLogic.event_system.messages;


import gameLogic.base.GeometryOperations;
import gameLogic.event_system.messages.interfaces.DiscreteRotationInvariant;
import org.apache.commons.math3.linear.RealVector;

public class PlatformState implements DiscreteRotationInvariant<PlatformState> {
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

    @Override
    public PlatformState getDiscreteRotation(int stepNum, int totalSteps) {
        final double rotationAngle = 2 * Math.PI / totalSteps * stepNum;
        return new PlatformState(
                GeometryOperations.rotate(position, rotationAngle),
                angle + rotationAngle,
                GeometryOperations.rotate(velocity, rotationAngle),
                isActive
        );
    }
}
