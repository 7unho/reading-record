package item6;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KeySetTest {
    public static void main(String[] args) {
        Map<String, Object> origin = new HashMap<>();
        origin.put("name", "7unho");

        Set<String> copy1 = origin.keySet();
        Set<String> copy2 = origin.keySet();

        System.out.println((copy1 == copy2)? "같은 객체": "다른 객체");
    }
}
