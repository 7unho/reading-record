# ref. [Effective Java 3/E]()

---

# [`Item10`] equals는 일반 규약을 지켜 재정의하라

---
#### `equals`메서드가 다음과 같은 상황이라면 재정의하지 않는 것이 최선이다.

1. 각 인스턴스가 **본질적으로 고유**하다.
   - 값을 표현하는 게 아니라 **동작하는 개체를 표현하는 클래스**일 경우<br>
     ➡️ `Thread`와 같은 클래스

2. 인스턴스의 **논리적 동치성**(logical equality)을 검사할 일이 없는 경우.
3. 상위 클래스에서 재정의한 equals가 **하위 클래스에서도 사용이 가능**한 경우
4. 클래스가 **private**이거나 **package-private**이고, **equals 메서드를 호출할 일이 없는 경우**
   ```java
   @Override public boolean equals(Object o){
        throw new AssertionError(); // 호출 금지 !
   }
    ```

<br>

### equals는 언제 재정의 되어야 하는가?
> `객체 식별성`이 아니라 `논리적 동치성`을 확인해야 할 때, <br> ❗️상위 클래스의 equals가 논리적 동치성을 비교하도록 재정의 되지 않았다면 **논리적 동치성을 비교할 수 있도록 재정의**해야 한다.
- 주로 **값 클래스**들이 해당된다.
  - **값 클래스** : `Interger`나 `String`처럼 값을 표현하는 클래스
- 두 값 객체를 `equals`로 비교하는 프로그래머는 같은 객체인지(`객체 식별성`)이 아니라 같은 값인지(`논리적 동치성`)를 알고 싶어할 것
  - 값 클래스라 해도, **인스턴스 통제 클래스**[`item1`]나 **enum**[`item34`]이라면 재정의하지 않아도 된다.
  - <u>논리 적으로 같은 인스턴스가 2개 이상 만들어지지 못하므로</u>, `논리적 동치성`과 `객체 식별성`이 **같은 의미**가 되어버린다.

<br>

## equals의 일반 규약
___
> equals 메서드는 동치관계(Equivalence relation)을 구현하며, 다음을 만족한다. ( 모든 참조 값은 null이 아니어야 한다.)
##### [1]. `반사성(Reflexivity)`: x.equals(x) => true이다. 
##### [2]. `대칭성(Symmetry)`: x.equals(y)가 true라면 y.equals(x)도 true이다.
##### [3]. `추이성(Transitivity)`: x.equals(y)가 true이고, y.equals(z)가 true라면 z.equals(x)도 true이다.
##### [4]. `일관성(Consistency)`: x.equals(y)를 반복해서 호출하면 항상 true나 false를 반환한다. => 반환 값이 항상 일정해야 한다.
##### [5]. `not null`: x.equals(null)은 false이다.

<br>

### [1]. 반사성
> x.equals(x) => true이다.
- 객체는 자기 자신과 같아야 한다.

### [2]. 대칭성
> x.equals(y)가 true라면 y.equals(x)도 true이다.
- 두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다는 뜻이다.
    #### before
    ```java
  public final class CaseInsensitiveString {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }
        // 대칭성 위배
        @Override public boolean equals(Object o){
            if(o instanceof CaseInsensitiveString) 
                return s.equalsIgnoreCase(((CaseInsensitiveString) o). s);
    
            // 한 방향으로만 작동한다.
            if(o instanceof String) return s.equalsIgnoreCase((String) o);
            return false;
        }
  }
  
  public static void main(String[] args){
        CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
        String s = "polish";
  }
    ```
  - cis.equals(s) => true
  - s.equals(cis) => false : ❗️대칭성 위반
    - `CaseInsensitiveString.equals`는 일반 String을 알고 있지만, `String.equals`는 `CaseInsensitive`의 존재를 모른다.
  
  #### after
    ```java
  @Override public boolean equals(Object o){
        return o instanceof CaseInsensitiveString && 
                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }
    ```
<br>

### [3]. 추이성(Transitivity)
> x.equals(y)가 true이고, y.equals(z)가 true라면 z.equals(x)도 true이다.
##### 상위 클래스에 없는 새로운 필드를 하위 클래스에 추가하는 상황을 생각해보자.
- 상위 클래스(Point)와 하위 클래스(ColorPoint)의 `equals`메서드를 각각 실행했을 때
  ```java
  // 추이성 위배
  @Override public boolean equals(Object o){
      if(!(o instanceof Point)) return false;

      // o가 일반 Point라면 좌표값만 비교
      if(!(o instanceof ColorPoint)) return o.equals(this);

      // o가 ColorPoint라면 색상까지 비교
      return super.equals(o) && ((ColorPoint)o).color == color;
  }

  ColorPoint cp1 = new ColorPoint(1, 2, Color.RED);
  Point p1 = new Point(1, 2);
  ColorPoint cp2 = new ColorPoint(1, 2, Color.BLUE);
    
  cp1.equals(p1) // true
  p1.equals(cp2) // true
  cp2.equals(cp1) // false
  ```
  - 이는 앞서 정의된 추이성에 위배되는 결과로 나타난다.
  - 또한, 무한 재귀에 빠질 위험도 있다.
  - 해당 현상은 <u>모든 객체 지향 언어의 동치관계</u>에서 나타나는 **근본적인 문제**
    - 구체 클래스를 확장해 새로운 값을 추가하면서 **`equals` 규약을 만족시킬 방법은 존재하지 않음.**
  
<br>

#### 구체 클래스의 하위 클래스에서 값을 추가하는 우회 방법
> [`item18`] 상속 대신 컴포지션을 사용하라
- Point를 상속하는 대신 
  1. Point를 ColorPoint의 **private 필드**로 두고,
  2. ColorPoint와 같은 위치의 <u>일반 Point를 반환하는 view 메서드[`item6`]를</u> **public으로 추가하는 방식.**
  ```java
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
  }
  ```
#### ✅ check
<aside style="background-color: lightgrey">💡 상위 클래스를 직접 인스턴스로 만드는게 불가능하다면, 이러한 문제들은 일어나지 않는다.</aside>
- 추상 클래스의 하위 클래스에서는 equals의 규악을 지키면서도 값을 추가할 수 있다.

<br>

### [4]. 일관성(Consistency)
> x.equals(y)를 반복해서 호출하면 항상 true나 false를 반환한다. => 반환 값이 항상 일정해야 한다.
- 가변 객체는 비교 시점에 따라 서로 다르거나 같을 수도 있지만, **불변 객체는 항상 같은 값을 유지해야 한다.**
- 클래스를 작성할 때는 불변 클래스로 만드는게 나을지 충분한 고민이 필요[`item17`]
- 또한, 클래스가 불변이든 가변이든 **equals의 판단에 신뢰할 수 없는 resource가 끼어들면 안된다**
  - 예를 들어, java.net.URL의 equals는 주어진 URL과 매핑된 호스트의 IP주소를 이용해 비교한다.<br>
    <u>호스트 이름을 IP주소로 바꿀 때 네트워크를 통해야 하는데</u>, **그 결과가 항상 같다고 보장되지 않음.**
<aside>따라서, equals는 항상 메모리에 존재하는 객체만을 사용한 <b>결정적 계산(Deterministic)만 수행</b>해야 한다 </aside>

<br>

### [5]. not-null
> 모든 객체가 null과 같지 않아야 한다.
- 다음 코드처럼 입력이 null인지를 확인하여 자신을 보호하자.
##### [5-1]. 명시적 형변환 (필요 없다...)
```java
@Override public boolean equals(Object o){
    if(o == null) return false;
}
 ```
##### [5-2]. 묵시적 형변환 (5-1 보다 나은 방법)
```java
@Override public boolean equals(Object o){
    if(!(o instanceof MyType)) return false;
    
        ...
}
```
- instanceof는 첫 번째 피연산자가 null이면 false를 반환한다.
- 따라서, 입력이 null이라면 타입 확인 시 false를 반환하므로 null 검사를 명시적으로 하지 않아도 됨.

<br>

## equals 메서드 구현 방법 정리 ( PhoneNumber 코드 참고 )
___
#### [1]. '==' 연산자를 통해 입력이 자기 자신의 참조인지 확인한다.
| 타입                       | 비교 방법                                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------------------|
| 기본 타입( float, double 제외) | '==' 연산자로 비교                                                                                                       |
| 참조 타입                    | 각각의 equals 메서드로 비교                                                                                                 |
| float, double 타입| 각각 정적 메서드인 (Float, Double).compare(type, type)으로 비교 <br/>Float와 Double도 equals 메서드가 있지만, 오토박싱을 수반할 수 있어 성능상 좋지 않다. |
#### [2]. instanceof 연산자로 입력이 올바른 타입인지 확인한다.
#### [3]. 입력을 올바른 타입으로 형변환한다.
#### [4]. 입력 객체와 자기 자신의 대응되는 핵심 필드들이 모두 일치하는지 하나씩 검사한다.

<br>

<h3>❗️equals를 다 구현했다면 세 가지만 자문해보자.</h3>
1. 대칭적인가?
2. 추이성이 있는가?
3. 일관적인가?

<br>

## equals를 재정의할 땐 hashcode도 반드시 재정의하자[`item11`]
___
- **필드들의 동치성만 검사**해도 equals 규악을 어렵지 않게 지킬 수 있다 -> 너무 복잡하게 해결하려 하지 말자.
- 일반적으로 별칭(alias)는 사용하지 않는게 좋다.
- equals의 매개변수로 Object이외의 타입을 선언하지 말자.
  ```java
  // 입력 타입은 반드시 Object여야 한다 !
  public boolean equals(TestType o){
                    ...
  }
  ```
  - 해당 메서서드는 equals를 재정의한 것이 아닌 다중정의(overloading)[`item52`]한 것이다.

<br>

## 📝 요약
___
1. 꼭 필요한 경우가 아니라면 **equals를 재정의하지 말자.**
2. 그럼에도 equals를 재정의해야 한다면 **아래 사항들을 확실히 지켜가며 비교해야 한다.**
   - 그 클래스의 핵심 필드를 모두 빠짐없이
   - equals의 다섯 규약을 확실히
