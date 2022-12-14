package item7;

import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack(){
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e){
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop(){
        if(size == 0) throw new EmptyStackException();

        // 스택을 pop할 떄, 참조 해제가 이뤄지지 않는다.
        // return elements[--size];
        Object result = elements[--size];

        // 다 쓴 객체 참조 해제
        elements[size] = null;
        return result;
    }

//    원소를 위한 공간을 적어도 하나 이상 확보한다.
//    배열 크기를 늘려야 할 때마다 대략 두 배씩 늘린다.
    private void ensureCapacity() {
        if(elements.length == size) elements = Arrays.copyOf(elements, (size + 1) * 2);
    }
}
