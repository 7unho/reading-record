package item10;

import java.util.Objects;

public final class CaseInsensitiveString {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    /* before
    // 대칭성 위배
    @Override public boolean equals(Object o){
        if(o instanceof CaseInsensitiveString) return s.equalsIgnoreCase(((CaseInsensitiveString) o). s);

        // 한 방향으로만 작동한다.
        if(o instanceof String) return s.equalsIgnoreCase((String) o);
        return false;
    }
    */

    // after
    @Override public boolean equals(Object o){
        return o instanceof CaseInsensitiveString && ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }

}
