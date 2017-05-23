package gameLogic.gameComponents;


import gameLogic.base.GeometryOperations;
import gameLogic.collisionHandling.interfaces.CircleCollider;
import gameLogic.event_system.messages.BallState;
import gameLogic.gameComponents.interfaces.Shapefull;
import gameLogic.gameComponents.interfaces.Statefull;
import gameLogic.geometryShapes.Circle;
import org.apache.commons.math3.linear.RealVector;


public class Ball extends GameComponent implements CircleCollider, Shapefull<Circle>, Statefull<BallState> {
    private Circle circle;

    public Ball(double radius) {
        super();
        circle = new Circle(radius);
    }

    @Override
    public double getRadius() {
        return this.circle.getRadius();
    }


    @Override
    public Circle getShape() {
        return circle;
    }

    public void bouncePoint(RealVector point, RealVector transportVelocity) {
        bounceNorm(getPosition().subtract(point), transportVelocity);
    }

    public void bounceNorm(RealVector normVec, RealVector transportVelocity) {
        final RealVector normSurfaceVelocity = GeometryOperations
                .getProjectionMatrix(normVec)
                .preMultiply(transportVelocity);
        final RealVector relativeVelocity = velocity.subtract(normSurfaceVelocity);
        final RealVector newRelativeVelocity = GeometryOperations
                .getReflectionMatrix(normVec)
                .preMultiply(relativeVelocity);
        velocity = transportVelocity.add(newRelativeVelocity);
    }

    @Override
    public BallState getState() {
        return new BallState(getPosition(), velocity);
    }

    @Override
    public void setState(BallState state) {
        velocity = state.getVelocity();
        origin = state.getPosition();
    }
}
