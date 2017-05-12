package gameLogic.gameComponents.interfaces;


import org.apache.commons.math3.linear.RealVector;

public interface Area {
    boolean containsGlobalPoint(RealVector point);
}
