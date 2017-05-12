package gameLogic.collisionHandling;

import gameLogic.base.ISolidBody;
import org.apache.commons.math3.linear.RealVector;


public interface PolygonObstacle extends ISolidBody {
    RealVector getClosestPoint(RealVector origin);
}
