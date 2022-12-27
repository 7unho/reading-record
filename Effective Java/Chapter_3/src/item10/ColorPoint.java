package item10;

import java.awt.*;
import java.util.Objects;

public class ColorPoint {
     // 1. Point를 ColorPoint의 private 필드로 선언
    private final Point point;
    private final Color color;


    public ColorPoint(int x, int y, Color color){
        point = new Point(x, y);
        this.color = Objects.requireNonNull(color);
    }


    // 이 ColorPoint의 Point view를 반환한다.
    // 2. ColorPoint와 같은 위치의 일반 Point를 반환하는 view 메서드[asPoint]를 public으로 추가하는 방식.
    public Point asPoint(){
        return point;
    }

    @Override public boolean equals(Object o){
        if(!(o instanceof ColorPoint)) return false;

        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }

    //    public ColorPoint(int x, int y, Color color) {
//        super(x, y);
//        this.color = color;
//    }
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

    /*
    // 리스코프 치환 원칙(P58) 위배
    @Override public boolean equals(Object o){
        // 매개변수가 null이 아니면서, 매개변수와 클래스의 타입이 다르다면 false
        if (o == null || o.getClass() != getClass()) return false;

        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
    */
}
