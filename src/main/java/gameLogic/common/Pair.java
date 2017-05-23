package gameLogic.common;

import org.jetbrains.annotations.Nullable;

/**
 * Created by artem on 5/12/17.
 */
public class Pair<T1, T2> {
    private T1 first;
    private T2 second;

    public Pair(@Nullable T1 first, @Nullable T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }
}
