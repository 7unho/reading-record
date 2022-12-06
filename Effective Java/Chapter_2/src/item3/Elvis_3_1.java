package item3;

import java.util.function.Supplier;

// 싱글턴 생성 방법 1. public static 멤버가 final 필드
public class Elvis_3_1 {
    public static final Elvis_3_1 INSTANCE = new Elvis_3_1();
    private Elvis_3_1(){};

    public void leaveTheBuilding() {
        //...
    }
}
