package gameLogic.game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EventBus {
    private static Map<String, List<Function<Object, Void>>> listeners = new HashMap<>();

    public static void addEventListener(String eventName, Function<Object, Void> listener) {
        if (!listeners.containsKey(eventName)) {
            listeners.put(eventName, new ArrayList<>());
        }

        listeners.get(eventName).add(listener);
    }

    public static void dispatchEvent(String eventName, Object event) {
        final List<Function<Object, Void>> localListeners = listeners.get(eventName);
        if (localListeners != null) {
            localListeners.forEach(listener -> listener.apply(event));
        }

    }
}
