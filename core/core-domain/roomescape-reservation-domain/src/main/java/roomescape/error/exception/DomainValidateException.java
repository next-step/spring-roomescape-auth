package roomescape.error.exception;

import roomescape.error.code.DomainErrorCode;
import roomescape.error.key.DomainErrorKey;
import roomescape.error.key.DomainErrorKeys;

import static roomescape.error.code.DomainErrorCode.THIS_VALUE_NOT_TO_BE_EMPTY;
import static roomescape.error.code.DomainErrorCode.THIS_VALUE_TO_BE_NULL;

public class DomainValidateException extends DomainException {

    private static final String KEY_CLASS_NAME = "class";

    public DomainValidateException(DomainErrorCode code, DomainErrorKeys keys) {
        super(code, keys);
    }

    public static DomainValidateException notToBeEmpty(Class<?> cls) {
        return new DomainValidateException(
                THIS_VALUE_NOT_TO_BE_EMPTY,
                DomainErrorKeys.of(
                        new DomainErrorKey(
                                KEY_CLASS_NAME,
                                cls.getName()
                        )
                )
        );
    }

    public static DomainValidateException notToBeNull(Class<?> cls) {
        return new DomainValidateException(
                THIS_VALUE_TO_BE_NULL,
                DomainErrorKeys.of(
                        new DomainErrorKey(
                                KEY_CLASS_NAME,
                                cls.getName()
                        )
                )
        );
    }
}
