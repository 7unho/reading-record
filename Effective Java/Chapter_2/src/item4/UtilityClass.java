package item4;

// 인스턴스를 만들지 않는 클래스
public class UtilityClass{
    // 생성자가 존재하는데 호출할 수 없다 -> 직관적이지 않으므로, 아래와 같은 주석 달아주기

    // 기본 생성자가 만들어지는 것을 막는다(인스턴스화 방지)
    private UtilityClass() {
        // 꼭 AssertionError를 던질 필요는 없지만,
        // 클래스 안에서 실수로라도 생성자를 호출하지 않도록 해준다.
        throw new AssertionError();
    }
}