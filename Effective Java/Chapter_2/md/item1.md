# ref. [Effective Java 3/E]()

## Item1. 생성자 대신 정적 팩터리 메서드를 고려하라
___

> 클래스는 생성자와 별도로 정적 팩터리 메서드(Static Factory Method, 디자인 패턴에서의 Factory Method가 아닌)를 제공할 수 있다.

___
#### 정적 팩터리 메서드
```java
public static Boolean valueOf(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
}
```
### ❗️정적 팩터리 메서드를 제공해보자
___
### 장점
1. #### 이름을 가질 수 있다.<br>
    - 생성자에 넘기는 매개변수와 생성자 자체만으로는 반환될 객체의 특성을 제대로 설명하지 못한다.
        - 정적 팩터리는 이름만 잘 지으면 반환될 객체의 특성을 쉽게 묘사할 수 있다.
      ```
      ex) 어떤 방식이 '값이 소수인 BigInteger를 반환한다'는 의미를 잘 전달해주는가?
      - BigInteger(imt, int, Random)
      - BigInteger.probablePrime
      ```
    - 하나의 시그니처로는 생성자를 하나만 만들 수 있다.<br>
      ➡️ 정적 팩터리 메서드를 이용해 각각의 차이를 잘 드러내는 이름을 지어주자
        - 생성자 이용
          ```java
          public class Animal {
                String type;
                String name;
      
                public Animal(String name){
                    this.name = name;
                }
      
                /* => 동일한 시그니처로는 생성자를 하나만 만들 수 있다.
                public Animal(String type){
                    this.type = type;
                }*/
          }
          ```
        - 팩토리 메서드 이용
          ```java
          public class Animal {
                String type;
                String name;
        
                public Animal(String name){
                      this.name = name;
                }
      
                public static Animal withName(String name){
                      return new Animal(name);
                }
        
                public static Animal withType(String type){
                      Animal animal = new Animal();
                      animal.setType("cat");
                      return animal;
                }
          }
          ```
2. #### 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.
   - 이로 인하여, 불필요한 객체 생성을 피할 수 있다.
     1. 불변 클래스(Immutable Class; item17)는 인스턴스를 미리 만들어 놓거나, 
     2. 새로 생성한 인스턴스를 캐싱하여 재활용<br>
   - 대표적 예로 `Boolean.valueOf(boolean)`메서드는 객체를 아예 생성하지 않는다.
   ##### 인스턴스 통제 클래스
   > 정적 팩터리 메서드는 반복되는 요청에 같은 객체를 반환하는 식으로 언제 어느 인스턴스를 살아 있게 할지를 통제할 수 있다.<br>
   - 통제하는 이유?
     1. 싱글톤(Singleton, item3)으로 만들 수도 있고, 
     2. 인스턴스화 불가(Noninstantiable, item4)로 만들 수도 있다.
     3. 불변 값 클래스(item17)에서 동치인 인스턴스가 단 하나 뿐임을 보장할 수 있다.<br>
        `a == b일 때만, a.equals(b)가 성립`

     이러한 인스턴스 통제는 Flyweight Pattern의 근간이 되며, 열거 타입(item34)는 인스턴스가 하나만 만들어짐을 보장한다.

3. #### 반환 타입의 하위 객체 타입을 반환할 수 있는 능력이 있다.
   - 반환할 객체의 클래스를 자유롭게 선택할 수 있는 `엄청난 유연성`을 선물한다.
     - 구현 클래스를 공개하지 않고도, 그 객체를 반환할 수 있게 된다.<br>
       ➡️ API를 보다 작게 유지할 수 있다.
     - 이는, 인터페이스를 정적 팩터리 메서드의 반환타입으로 사용하는 인터페이스 기반 프레임워크(item20)을 만드는 핵심 기술이 된다.
     
4. #### 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.
    > 반환 타입의 하위 타입이기만 하면, 어떤 클래스의 객체를 반환하든 상관 없다.
    - 가령 EnumSet 클래스(item36)는 public 생성자 없이 오직 정적 팩터리 메서드만 제공하며, 원소의 수에 따라 두 가지 하위 클래스 중 하나의 인스턴스를 반환한다.
      - 원소가 64개 이하 : 원소들을 `long 변수 하나`로 관리하는 RegularEnumSet의 인스턴스를 반환
      - 원소가 65개 이상 : 원소들을 `long 배열`로 관리하는 JumboEnumSet의 인스턴스를 반환

   ➡ 클라이언트는 이 두 클래스의 존재를 몰라도 된다. 
5. #### 정적 펙터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 안하도 된다.
   ##### 서비스 제공자 프레임워크( Service Provider Framework )
    - 3개의 핵심 컴포넌트로 구성

   | 컴포넌트 명     | 역할                        |             JDBC             |
   |:-----------|:--------------------------|:----------------------------:|
   | 서비스 인터페이스  | 구현체의 동작을 정의               |          Connection          |
   | 제공자 등록 API | 제공자가 구현체를 등록할 때 사용        | DriverManager.registerDriver |
   | 서비스 접근 API | 클라이언트가 서비스의 인스턴스를 얻을 때 사용 | Drivermanager,getConnection  |
---
### 단점
1. #### 정적 펙터리 메서드만 제공하면 하위 클래스를 만들 수 없다.
   > 상속을 하려면 public 또는 protected 생성자가 필요하기 때문에, 정적 펙터리 메서드만 제공하면 하위 클래스를 만들 수 없다.
   
   - 하지만, 상속보다 **컴포지션(item18)하도록 유도**하고, **불변 타입(item17)으로 만들려면 이 제약을 지켜야 한다는 점**에서 오히려 장점으로 받아들일 수도 있다.

2. #### 정적 팩터리 메서드는 프로그래머가 찾기 어렵다
   - 생성자처럼 API 명세서에 명확히 드러나지 않다 보니, 사용자는 해당 방식의 클래스를 인스턴스화 시킬 방법을 찾아야한다.
   - 따라서, **API 문서를 잘 작성**하거나, **알려진 규약에 따라 메서드 이름을 작성하는 방법**으로 문제를 완화해주어야 한다.


## 정적 팩터리 메서드에 흔히 사용되는 명명 방식
___
- `from` : 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 **형변환 메서드**<br>


        Date d = Date.from(instance);
- `of` : 여러 매개변수를 받아 **적합한 타입의 인스턴스를 반환하는 집계 메서드**


        Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);

- `valueOf` : from과 of의 더 자세한 버전


        BigInteger prime = BigInteger.valuof(Integer.MAX_VALUE);

- `instance` or `getInstance` : (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지는 않는다.


        StackWalker luke = StackWalker.getInstance(options);

- `create` or `newInstance` : 매번 새로운 인스턴스를 생성해 반환함을 보장한다.

    
        Object newArray = Array.newInstance(classObject, arrayLen);

- `getType` : `getInstance`와 같으나, 생성할 클래스가 아닌 다른 클래스의 팩터리 메서드를 정의할 때 쓴다.`type`은 팩터리 메서드가 반환할 객체의 타입이다.


        FileStore fs = Files.getFileStore(path);

- `newType` : `newInstance`와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다.


        BufferedReader br = Files.newBufferedReader(path);


- `type` : `getType`과 `newType`의 간결한 버전


        List<Complaint> litany = Collections.list(legacyLitany);



