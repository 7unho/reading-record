package item10;

import java.awt.*;

public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    /*
    // 대칭성 위배
    @Override public boolean equals(Object o){
        if(!(o instanceof ColorPoint)) return false;

        return super.equals(o) && ((ColorPoint) o).color == color;
    }

    1. Point의 equals는 색상을 무시하고,
    2. ColorPoint의 equals는 입력 매개변수의 클래스 종류가 다르다고 판단해 false를 반환한다.
    */

    /*
    // 추이성 위배
    @Override public boolean equals(Object o){
        if(!(o instanceof Point)) return false;

        // o가 일반 Point라면 좌표값만 비교
        if(!(o instanceof ColorPoint)) return o.equals(this);

        // o가 ColorPoint라면 색상까지 비교
        return super.equals(o) && ((ColorPoint)o).color == color;
    }

    */
}
