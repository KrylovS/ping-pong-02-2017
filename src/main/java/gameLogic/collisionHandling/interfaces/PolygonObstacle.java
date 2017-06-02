package gameLogic.collisionHandling.interfaces;

import gameLogic.base.interfaces.ISolidBody;
import org.apache.commons.math3.linear.RealVector;


public interface PolygonObstacle extends ISolidBody {
    RealVector getClosestPoint(RealVector origin);
    RealVector getNormDirection(RealVector colliderPosition, RealVector collisionPoint);
}
