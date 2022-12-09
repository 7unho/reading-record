# ref. [Effective Java 3/E]()

---

# [`Item9`] try-final보다는 try-with-resources를 사용하라

---

## 자원 닫기
___
> 자원 닫기는 클라이언트가 놓치기 쉬워서 예측할 수 없는 성능 문제로 이어질 수 있다.
- 이런 자원 중 상당수가 안전망으로 **finalizer**[`item8`]를 사용하고 있지만, 효율적이지 못하다.

### close 메서드
- 자바에서는 `close`메서드를 통해 직접 닫아줘야 하는 자원이 많다.
  - `InputStream`
  - `OutputStream`
  - `java.sql.Connection` 
  - 기타

<br>

## try-finally
___
- 전통적으로 자원 close를 보장하는 수단으로 `try-finally`가 쓰였다.<br> 
```java
// 9.1) try-finally : 더 이상 자원을 회수하는 최선의 방법이 아니다.
static String firstLineOfFile(String path) throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(path));
            try {
                return br.readLine();
            } finally {
                br.close();
            }
}
```
➡️ 나쁘지 않아 보이지만, 자원이 늘어난다면 문제가 발생한다.

```java
// 9.2) 자원이 둘 이상이면 try-finally 방식은 너무 지저분하다 !!
static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
                try {
                    byte[] buf = new byte[BUFFER_SIZE];
                    int n;
                    while ((n = in.read(buf)) >= 0) out.write(buf, 0, n);
                } finally {
                    out.close();
                }
        } finally {
            in.close();
        }
}
```
- 우선 코드가 너무 복잡해진다. 
- 또한, try절에서 예외발생 시 `close` 메서드가 호출되지 않을 수 있다.

<br>

## try-with-resources
___
- 앞선 `try-final`의 문제들을 해결할 수 있다.
- `try-with-resources`를 사용하려면 해당 자원이 `AutoCloseable` 인터페이스를 구현해야 한다.

### try-with-resources를 적용한 코드 9.1
```java
// 9.3) try-with-resources : 자원을 회수하는 최선책!
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
}
```
- 아름답다.

### try-with-resources를 적용한 코드 9.2
```java
// 9.4) 복수의 자원을 처리하는 try-with-resources : 아주 깔끔해..
static void copy(String src, String dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) out.write(buf, n, 0);
        }
}
```
- `try-with-resources`를 적용한 코드가 `가독성`은 물론 `디버깅`에도 훨씬 효율적이다.

### catch절과 함께 쓰는 try-with-resources
```java
// 9.5) catch절과 함께 쓰는 try-with-resources
static String firstLineOfFile(String path, String defaultVal) {
    try(BufferedReader br = new BufferedReader(new FileReader(path))){
        return br.readLine();
    } catch (IOException e){
        e.printStackTrace();
        return defaultVal;
    }
}
```
- `catch`절 덕 분에 `try`를 더 중첩하지 않고도 다수의 예외를 처리할 수 있다.

<br>

## 📝 요약
___
1. 꼭 회수해야 하는 자원을 다룰 때는 `❌try-final`말고, **`⭕️ try-with-resources`를 사용하자 !**
2. 예외는 없다.<br>
   - 코드는 더 짧고 분명해지고(`가독성`)
   - 만들어지는 예외 정보도 훨씬 유용하다.
   - 정확하고 쉽게 자원을 회수할 수 있다.
