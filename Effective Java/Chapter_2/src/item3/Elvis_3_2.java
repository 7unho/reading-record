package item3;

import java.util.function.Supplier;

public class Elvis_3_2 {
    private static final Elvis_3_2 INSTANCE = new Elvis_3_2();
    private Elvis_3_2() {}
    public static Elvis_3_2 getInstance() { return INSTANCE; }
    public static void main(String[] args) {
        Supplier<Elvis_3_2> eivisSupplier = Elvis_3_2::getInstance;
        System.out.println(eivisSupplier.get());
    }
}
