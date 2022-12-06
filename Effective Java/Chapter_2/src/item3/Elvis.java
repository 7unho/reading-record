package item3;

import java.io.Serializable;

public class Elvis implements Serializable {

    private static final Elvis INSTANCE = new Elvis();
//        transient public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}

    public Object readResolve() { return this.INSTANCE; }
}