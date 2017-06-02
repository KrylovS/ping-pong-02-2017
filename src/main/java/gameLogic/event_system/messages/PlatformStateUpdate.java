package gameLogic.event_system.messages;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameLogic.base.GeometryOperations;
import gameLogic.event_system.messages.interfaces.DiscreteRotationInvariant;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;


public class PlatformStateUpdate implements DiscreteRotationInvariant<PlatformStateUpdate> {
    private RealVector offset;
    private RealVector velocity;

    public PlatformStateUpdate(RealVector offset, RealVector velocity) {
        this.offset = offset;
        this.velocity = velocity;
    }

    @JsonCreator
    public PlatformStateUpdate(
            @JsonProperty("offset") double[] offset,
            @JsonProperty("velocity") double[] velocity
    ) {
        this(
                new ArrayRealVector(offset),
                new ArrayRealVector(velocity)
        );
    }

    public RealVector getOffset() {
        return offset;
    }

    public RealVector getVelocity() {
        return velocity;
    }

    @JsonGetter("offset")
    public double[] arrayGetOffset() {
        return offset.toArray();
    }

    @JsonGetter("velocity")
    public double[] arrayGetVelocity() {
        return velocity.toArray();
    }


    @Override
    public PlatformStateUpdate getDiscreteRotation(int stepNum, int totalSteps) {
        final double rotationAngle = 2 * Math.PI / totalSteps * stepNum;
        return new PlatformStateUpdate(
                GeometryOperations.rotate(offset, rotationAngle),
                GeometryOperations.rotate(velocity, rotationAngle)
        );
    }
}
