package gameLogic.common;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CommonFunctions {
    public static int getCircularOffset(int curr, int offset, int length) {
        final int newId = curr + offset;
        final int rest = newId % length;

        if (rest < 0) {
            return rest + length;
        } else {
            return rest;
        }
    }

    public static <T> T getByCircularIndex(List<T> list, int index) {
        int itemIndex = index % list.size();
        itemIndex = itemIndex >= 0 ? itemIndex : itemIndex + list.size();
        return list.get(itemIndex);
    }

    public static <T> List<T> getCircularTransposition(List<T> list) {
        final List<T> result = new ArrayList<>(list.size());
        IntStream.range(0, list.size()).boxed()
                .forEach(i -> result.set(
                        (i + 1) % result.size(),
                        list.get(i)
                ));
        return result;
    }
}
