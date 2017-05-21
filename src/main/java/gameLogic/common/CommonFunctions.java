package gameLogic.common;


import java.util.List;

public class CommonFunctions {
    public static <T> T getByCircularIndex(List<T> list, int index) {
        int itemIndex = index % list.size();
        itemIndex = itemIndex >= 0 ? itemIndex : itemIndex + list.size();
        return list.get(itemIndex);
    }
}
