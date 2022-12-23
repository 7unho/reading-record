## 3. 모든 객체의 공통 메서드
___
<br>

### 3장의 학습 목표
___
```java
Object는 기본적으로 상속해서 사용하도록 설계되었다.
Object에서 final이 아닌 메서드(equals, hashCode, toString, clone, finalize)는 모두 재정의를 염두에 두고 설계됨
```
- 위와같은 `final`이 아닌 `Object`메서드들을 언제 어떻게 재정의해야 하는지
- `Comparable.compareTo`의 사용법