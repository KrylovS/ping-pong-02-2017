package gameLogic.gameComponents.interfaces;


public interface Statefull<T> {
    T getState();
    void setState(T state);
}
