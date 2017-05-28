package gameLogic.event_system.messages.interfaces;


public interface DiscreteRotationInvariant<T> {
    T getDiscreteRotation(int stepNum, int totalSteps);
}
