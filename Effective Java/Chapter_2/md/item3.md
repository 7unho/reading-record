# ref. [Effective Java 3/E]()

---

# [`Item3`] private 생성자나 열거 타입으로 싱글턴임을 보증하라

---

## SingleTone
- 인스턴스를 오직 하나만 생성할 수 있는 클래스<br>
➡ 클래스를 싱글턴으로 만들게되면 이를 사용하는 클라이언트를 **테스트하기가 어려워질 수 있다.**

## 싱글턴 생성 방식
___
### [1]. public static final 필드 방식의 싱글턴
```java
// 싱글턴 생성 방법 1. public static 멤버가 final 필드
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis(){};

    public void leaveTheBuilding() {
        //...
    }
}
```
#### 장점
1. private 생성자는 public static final 필드인 `Evlis.INSTANCE`를 초기화할 때, 한 번만 호출된다.<br>
   - public이나 protected 생성자가 없으므로 클래스가 초기화 될 때, 인스턴스가 하나뿐임이 보장된다.
2. 코드가 간결하다.
3. 해당 클래스가 **싱글턴임이 API에 명백히 드러난다는 장점**이 있다.
    - public static 필드가 final이니 절대로 다른 객체를 참조할 수 없다.

#### 단점
1. 권한이 있는 클라이언트는 리플렉션 API(item65)인 `AccessibleObject.setAccessible`을 사용해야만 private생성자를 호출할 수 있다.
    - 이러한 공격을 방어하려면 생성자를 수정하여 **두 번째 객체가 생성되려 할 때, 예외를 던질 수 있도록 로직을 구성**한다.


### [2]. 정적 팩터리 방식의 싱글턴
```java
// 싱글턴 생성 방법 1. public static 멤버가 final 필드
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis(){}
    public static Elvis getInstance() { return INSTANCE; }
    public void leaveTheBuilding() {
        //...
    }
}
```
#### 장점
1. API를 바꾸지 않고도 싱글턴이 아니게 변결할 수 있다.
   - 유일한 인스턴스를 반환하던 팩터리 메서드가 사용자가 원한다면, 호출하는 쓰레드별로 다른 인스턴스를 넘겨줄 수 있다.
2. 원한다면 정적 팩터리를 **제네릭 싱글턴 팩터리**로 만들 수 있다(item30).
3. 정적 팩터리의 메서드 참조를 공급자(supplier)로 사용할 수 있다.
   ```java
      // Supplier<[클래스]> [클래스]Supplier = [클래스]::[메서드명]; 
      Supplier<Elvis> eivisSupplier = Elvis::getInstance;
   ```

### [3]. 열거 타입 방식의 싱글턴 - 바람직한 방법
```java
public enum Elvis{
    INSTANCE;
    
    public void leaveTheBuilding(){/*...*/};
}
```

💡 대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.
- public 필드 방식과 비슷하지만,<br>
  - 더 간결하다.
  - 추가 노력 없이 직렬화할 수 있다.
  - 아주 복잡한 직렬화 상황이나, 리플렉션 공격에서도 제 2의 인스턴스가 생기는 일을 완벽히 막아준다.
- ❗️단, 만들려는 싱글턴이 **Enum이외의 클래스를 상속해야 한다면** 이 방법은 사용할 수 없다.
  - 열거 타입이 다른 인터페이스를 구현하도록 선언할 수 는 있다.


## [1]과 [2]를 통한 직렬화( Serializing )[`12장`]
___
> 둘 중 하나의 방식으로 만든 싱글턴 클래스를 직렬화하려면 **[`12장`]** 단순히 Serializable을 구현한다고 선언하는 것만으로는 부족하다.


1. 모든 인스턴스 필드를 일시적(`transient`)이라고 선언하고, `readResolve`메서드를 제공해야 한다. **[`item89`]**
   - 이렇게 하지 않으면 직렬화된 인스턴스를 **역직렬화할 때 마다 새로운 인스턴스가 생성**된다.
   ```java
   public class Elvis implements Serializable { 
   
        private static final Elvis INSTANCE = new Elvis();
        // transient public static final Elvis INSTANCE = new Elvis();
        private Elvis() {}

        public Object readResolve() { return this.INSTANCE; }
   }   
   ```