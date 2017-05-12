package gameLogic.geometryShapes;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by artem on 5/12/17.
 */
public class Rectangle {
    private double width;
    private double length;

    public Rectangle(double length, double width) {
        this.width = width;
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    /*
    Coordinate system origin is in the rectangle center
     */
    public boolean contains(RealVector point) {
        final double x = point.getEntry(0);
        final double y = point.getEntry(1);

        final boolean insideX = -length / 2 <= x && x <= length / 2;
        final boolean insideY = -width / 2 <= y && y <= width / 2;

        return insideX && insideY;
    }

    public List<RealVector> getPointArray() {
        final double[][] points = new double[][] {
                {-length / 2, -width / 2},
                {length / 2, -width / 2},
                {length / 2, width / 2},
                {-length / 2, width / 2}
        };

        return Arrays.stream(points)
                .map(ArrayRealVector::new)
                .collect(Collectors.toList());
    }
}
