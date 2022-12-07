# ref. [Effective Java 3/E]()

---

# [`Item6`] 불필요한 객체 생성을 피하라

---

#### 들어가기에 앞서...
똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다. 재사용은 빠르고 효율적이다.<br>
특히, 불변 객체[`item17`]는 언제든 재사용할 수 있다.

<br>

### [1]. 생성자와 팩터리 메서드
___
> 생성자 대신 정적 팩터리 메서드[`item`]를 제공하는 불변 클래스에서는 정적 팩터리 메서드를 통해 불필요한 객체 생성을 피할 수 있다. 

`ex)` Boolean(String) -> Boolean.valueOf(String)
- 생성자는 호출할 때 마다 새로운 객체를 만들지만, 팩터리 메서드는 그렇지 않다.
- 가변 객체라 해도 사용 중에 변경되지 않을 것임을 안다면 재사용 가능하다.

<br>

### [2]. `비싼 객체`가 반복해서 필요하다면 캐싱하여 재사용하라.
___
  #### [1]. 자주 쓰는 값이라면 static final로 잡아놓고 재사용하기
```java
    // before
    static boolean isRomanNumeral(String s){
      return s.matches(regex);
    }
```
- `String.matches()`는 성능이 중요한 상황에서 반복해 사용하기 부적절하다.
  - 내부에서 만드는 정규표현식용 `Pattern` 인스턴스를 생성
    - 해당 인스턴스는 한 번 쓰고 버려져서 `GC`대상이 된다.
    - `Pattern`은 입력받은 정규표현식에 해당하는 유한 상태 머신(`finite state machine`)을 만들기 때문에 **인스턴스 생성 비용이 높다.**
```java
    // after
    public class RomanNumeral {
        // static final로 필드에 캐싱
        private static final Pattern ROMAN = Pattern.compile(regex);
        static boolean isRomanNumeral(String s) {
            return ROMAN.matcher(s).matches();
        }
    }   
```
<br>

### [3]. 모두가 똑같은 인스턴스를 대변하는 여러 인스턴스를 생성하지 말자
___
`ex)` Map 인터페이스의 `KeySet` 메서드
```java
    /*
     Map.KeySet {
        output : Map 인스턴스 안의 키 전부를 담은 Set View
    }
    */
    
    Map<String, Object> origin = new HashMap<>();
    origin.put("name", "7unho");

    Set<String> copy1 = origin.keySet();
    Set<String> copy2 = origin.keySet();

    System.out.println((copy1 == copy2)? "같은 객체": "다른 객체");
    // print "같은 객체"
```
- `Map.KeySet`을 호출할 때마다 새로운 Set인스턴스가 아닌 **매번 같은 Set 인스턴스를 반환**한다.
- 반환된 `Set`인스턴스가 일반적으로 가변이더라도, 반환된 인스턴스들은 기능적으로 모두 똑같다.<br>
  ❗️ 즉, 반환한 객체 중 하나를 수정하면 **다른 모든 객체가 같이 수정된다.**


### [4]. 오토박싱
___
> 박싱된 기본 타입 보다는 기본 타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자


## 📝 요약
___
1. 기존 객체를 재사용해야 한다면, 새로운 객체를 만들지 말자
##### 주의점
2. 무작정 `객체 생성은 비싸니까 피하자`로 오해하지 않기.
   - `JVM`자체적으로 부담되지 않는 작업들이 많다.
   - 프로그램의 명확성, 간결성, 기능을 위해 객체를 추가로 생성하는 것이라면 일반적으로 좋은 일이다.
3. 아주 무거운 객체가 아닌 이상, 단순히 객체 생성을 피하고자 자기만의 객체 Pool을 만들지 말자.
