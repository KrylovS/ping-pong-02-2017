package gameLogic.event_system.messages;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameLogic.base.GeometryOperations;
import gameLogic.event_system.messages.interfaces.DiscreteRotationInvariant;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class BallState implements DiscreteRotationInvariant<BallState> {
    private RealVector position;
    private RealVector velocity;

    public BallState(RealVector position, RealVector velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    @JsonCreator
    public BallState(@JsonProperty("position") double[] position, @JsonProperty("velocity") double[] velocity) {
        this.position = new ArrayRealVector(position);
        this.velocity = new ArrayRealVector(velocity);
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

    @JsonGetter("position")
    public double[] arrayGetPosition() {
        return position.toArray();
    }

    public RealVector getVelocity() {
        return velocity;
    }

    @JsonGetter("velocity")
    public double[] arrayGetVelocity() {
        return velocity.toArray();
    }

    @Override
    public BallState getDiscreteRotation(int stepNum, int totalSteps) {
        final double angle = 2 * Math.PI / totalSteps * stepNum;
        return new BallState(
                GeometryOperations.rotate(position, angle),
                GeometryOperations.rotate(velocity, angle)
        );
    }
}
