package gameLogic.event_system.messages;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameLogic.base.GeometryOperations;
import gameLogic.event_system.messages.interfaces.DiscreteRotationInvariant;
import org.apache.commons.math3.linear.ArrayRealVector;
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

    @JsonCreator
    public PlatformState(
            @JsonProperty("position") double[] position,
            @JsonProperty("angle") double angle,
            @JsonProperty("velocity") double[] velocity,
            @JsonProperty("active") boolean isActive
    ) {
        this.position = new ArrayRealVector(position);
        this.angle = angle;
        this.velocity = new ArrayRealVector(velocity);
        this.isActive = isActive;
    }

    public RealVector getPosition() {
        return position;
    }

    @JsonGetter("position")
    public double[] arrayGetPosition() {
        return position.toArray();
    }

    public double getAngle() {
        return angle;
    }

    public RealVector getVelocity() {
        return velocity;
    }

    @JsonGetter("velocity")
    public double[] arrayGetVelocity() {
        return velocity.toArray();
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
