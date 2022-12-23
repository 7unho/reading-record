package item10;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override public boolean equals(Object o){
        // Point 클래스의 인스턴스가 아니라면 false
        if(!(o instanceof Point)) return false;

        // Point 클래스의 인스턴스일 때, 값 일치 여부 반환
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
}
