# ref. [Effective Java 3/E]()

---

# [`Item5`] 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

---

💡 하나 이상의 자원에 의존하는 클래스의 경우, **자원을 직접 명시하지 말고 의존 객체를 주입하자 !**

## 하나의 자원만을 의존하는 경우
___
>[`CASE`]. 사전에 의존하는 맞춤법 검사 클래스를 구현할 때
### [1]. 정적 유틸리티를 잘못 사용한 예

```java
public class SpellChecker {
    private static final Lexicon dictionary = ...;
    
    private SpellChecker();
    
    public static boolean isVaild(String word) { ... }
    public static List<String> suggestions(String typo) { ... }
}
```

### [2]. 싱글턴을 잘못 사용한 예
```java
public class SpellChecker {
    private final Lexicon dictionary = ...;
    
    private SpellChecker();
    public static SpellChecker INSTANCE = new SpellChecker();
    
    public static boolean isVaild(String word) { ... }
    public static List<String> suggestions(String typo) { ... }
}
```

- 두 방식 모두 사전을 단 하나만 사용한다고 가정한다는 점에서 그리 훌륭하지 않다 !
  - 실전에서는 사전이 언어별로 따로 있을 수 있고, 특수 어휘용 사전을 별도로 두기도 한다.<br>
  
💡 **사용하는 자원에 따라 동작이 달라지는 클래스**에는 `정적 유틸리티 클래스`나 `싱글턴 방식`이 **적합하지 않다.** 


## SpellChecker가 여러 사전을 사용할 수 있도록 개선해보자
___
> ##### 조건
> 1. 클래슥가 여러 자원 인스턴스를 지원해야 한다.
> 2. 클라이언트가 원하는 자원을 사용해야 한다.

### [3]. 의존 객체 주입
```java
public class SpellChecker {
  private final Lexicon dictionary;

  // 인스턴스를 생성할 때, 생성자에 필요한 자원(dictionary)을 넘겨준다
  public SpellChecker(Lexicon dictionary){
    this.dictionary = dictionary;
  }
}
```
#### 👍 장점
1. 자원이 몇 개든 의존 관계가 어떻든 상관없이 작동한다.
2. 불변[`item17`]을 보장하여 ( 같은 자원을 사용하려는 ) 여러 클라이언트가 의존 객체들을 안심하고 공유할 수 있게 된다.
3. 의존 객체 주입은 생성자, 정적 팩터리[`item1`], Builder[`item2`]에 모두 똑같이 응용할 수 있다.

#### 👎 단점
1. 의존성이 수천 개나 되는 큰 프로젝트에서는 코드를 어지럽게 만들기도 한다.<br>
   ➡ `Dagger`, `Guice`, `Spring`과 같은 의존 객체 주입 프레임워크를 사용하면 해소할 수 있다.

### [4]. 의존 객체 주입 변형 - 생성자에 자원 팩터리를 넘겨주기
```java
public class SpellChecker {
  // Lexicon을 상속받는 하위 타입이라면 무엇이든 생성해줄 수 있는 팩터리를 넘겨준다.
  SpellChecker create(Supplier<? extends Lexicon> dictioneryFactory){ ... }
}
```

## 📝 요약
___
1. 클래스가 내부적으로 **하나 이상의 자원에 의존**하고, 그 자원이 **클래스 동작에 영향을 준다면,**<br>
   - [x] 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다.
   - [x] 해당 자원들을 클래스가 직접 만들게 해서도 안된다.<br>
2. 대신 필요한 자원을 생성자에 넘겨주자.<br>
   ➡ 클래스의 `유연성`, `재사용성`, `테스트 용이성`을 기막히게 개선해준다.
