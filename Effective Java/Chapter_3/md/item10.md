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
