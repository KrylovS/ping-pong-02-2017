package gameLogic.collisionHandling;

import gameLogic.collisionHandling.interfaces.PolygonObstacle;
import org.apache.commons.math3.linear.RealVector;

public class CollisionInfo {
    private double time;
    private RealVector point;
    private RealVector direction;
    private PolygonObstacle obstacle;

    public CollisionInfo(double time, RealVector point, RealVector direction) {
        this.time = time;
        this.point = point;
        this.direction = direction;
        this.obstacle = null;
    }

    CollisionInfo(double time, RealVector point, RealVector direction, PolygonObstacle obstacle) {
        this.time = time;
        this.point = point;
        this.direction = direction;
        this.obstacle = obstacle;
    }

    public double getTime() {
        return time;
    }

    public RealVector getPoint() {
        return point;
    }

    public RealVector getDirection() {
        return direction;
    }

    public PolygonObstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(PolygonObstacle obstacle) {
        this.obstacle = obstacle;
    }

}
