package gameLogic.gameComponents;

import static org.junit.Assert.*;

import gameLogic.TestHelper;
import gameLogic.event_system.messages.PlatformState;
import gameLogic.geometryShapes.Line;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class PlatormTest extends TestHelper {
    @Test
    public void testPointArray() {
        System.out.println("Testing Platform pointArray");

        final double length = 10;
        final double width = 1;
        final Platform platform = new Platform(length, width);

        final double verticalOffset = 1;
        platform.moveTo(new ArrayRealVector(new double[] {0, verticalOffset}));

        final List<RealVector> points = platform.getPointArray();
        final List<RealVector> correctPoints = Arrays.stream(new double[][]{
                {-length / 2, verticalOffset - width / 2},
                {length / 2, verticalOffset - width / 2},
                {length / 2, verticalOffset + width / 2},
                {-length / 2, verticalOffset + width / 2}
        })
                .map(ArrayRealVector::new)
                .collect(Collectors.toList());

        for (int i = 0; i != points.size(); ++i) {
            System.out.println(String.format("i = %d;", i));
            assertTrue(compare(points.get(i), correctPoints.get(i)));
        }

        System.out.println("OK");
    }

    @Test
    public void testLineArray() {
        System.out.println("Testing Platform lineArray");

        final double length = 10;
        final double width = 1;
        final Platform platform = new Platform(length, width);

        final double verticalOffset = 1;
        platform.moveTo(new ArrayRealVector(new double[] {0, verticalOffset}));

        final List<RealVector> points = platform.getPointArray();
        final List<Line> lines = platform.getLineArray();
        final List<Line> correctLines = IntStream.range(0, points.size()).boxed()
                .map(i -> new Line(
                        points.get(i % points.size()),
                        points.get((i + 1) % points.size()))
                )
                .collect(Collectors.toList());

        for (int i = 0; i != points.size(); ++i) {
            System.out.println(String.format("i = %d;", i));
            assertTrue(compare(
                    lines.get(i).getStartPoint(),
                    correctLines.get(i).getStartPoint()
            ));

            assertTrue(compare(
                    lines.get(i).getEndPoint(),
                    correctLines.get(i).getEndPoint()
            ));
        }

        System.out.println("OK");
    }

    @Test
    public void testGetClosestPoint() {
        System.out.println("Testing Platform getClosestPoint");

        final double length = 10;
        final Platform platform = new Platform(length, length);

        final double coord = 100;
        final List<RealVector> refPoints = Arrays.stream(new double[][]{
                {-coord, -coord}, {0, -coord}, {coord, -coord},
                {-coord, 0}, {coord, 0},
                {-coord, coord}, {0, coord}, {coord, coord}

        })
                .map(ArrayRealVector::new)
                .collect(Collectors.toList());

        final List<RealVector> closestPoints = refPoints.stream()
                .map(platform::getClosestPoint)
                .collect(Collectors.toList());

        final double halfLength = length / 2;
        final List<RealVector> correctClosestPoints = Arrays.stream(new double[][]{
                {-halfLength, -halfLength}, {0, -halfLength}, {halfLength, -halfLength},
                {-halfLength, 0}, {halfLength, 0},
                {-halfLength, halfLength}, {0, halfLength}, {halfLength, halfLength}
        })
                .map(ArrayRealVector::new)
                .collect(Collectors.toList());

        assertEquals(closestPoints.size(), correctClosestPoints.size());
        IntStream.range(0, closestPoints.size()).boxed()
                .forEach(
                        i -> assertTrue(compare(closestPoints.get(i), correctClosestPoints.get(i)))
                );

        System.out.println("OK");
    }

    @Test
    public void testStateLoading() {
        System.out.println("Testing Platform stateLoading");
        final double length = 10;
        final Platform platform = new Platform(length, length);

        final PlatformState platformState = new PlatformState(
                new ArrayRealVector(new double[]{100, 100}),
                2,
                new ArrayRealVector(new double[]{67, 98}),
                true
        );

        platform.setState(platformState);

        assertTrue(compare(platform.getPosition(), platformState.getPosition()));
        assertTrue(compare(platform.getRotation(), platformState.getAngle()));
        assertTrue(compare(platform.getVelocity(), platformState.getVelocity()));
        assertEquals(platform.getActive(), platformState.isActive());
        System.out.println("OK");
    }

    @Test
    public void testStateExtraction() {
        System.out.println("Testing Platform stateExtraction");
        final double length = 10;
        final Platform platform = new Platform(length, length);

        final PlatformState platformState = new PlatformState(
                new ArrayRealVector(new double[]{100, 100}),
                2,
                new ArrayRealVector(new double[]{67, 98}),
                true
        );

        platform.setState(platformState);

        final PlatformState actualState = platform.getState();

        assertTrue(compare(actualState.getPosition(), platformState.getPosition()));
        assertTrue(compare(actualState.getAngle(), platformState.getAngle()));
        assertTrue(compare(actualState.getVelocity(), platformState.getVelocity()));
        assertEquals(actualState.isActive(), platformState.isActive());
        System.out.println("OK");
    }

}
