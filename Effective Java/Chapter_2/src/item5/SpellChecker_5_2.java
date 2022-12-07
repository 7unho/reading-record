package item5;

import java.util.ArrayList;
import java.util.List;

// 싱글턴을 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다.
public class SpellChecker_5_2 {
    private final Lexicon dictionary = null;

    private SpellChecker_5_2() {}
    public static SpellChecker_5_2 INSTANCE = new SpellChecker_5_2();

    public static boolean isValid(String word) { /*... */ return true; }
    public static List<String> suggestions(String typo){ return new ArrayList<String>(); }
}
