package roomescape.util;

import java.util.Objects;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException(this.getClass().getName() + "의 인스턴스는 생성되어서 안됩니다.");
    }

    public static boolean isEmpty(String arg) {
        return Objects.isNull(arg) || arg.isEmpty();
    }
}
