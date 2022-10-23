package nextstep.auth.resolver;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class LoginUser {

    private final String memberUuid;
    private final List<Authority> roles;

    public static LoginUser anonymous() {
        return new LoginUser("", List.of(Authority.ANONYMOUS));
    }

    public enum Authority {
        USER, ADMIN, ANONYMOUS;

        public boolean isMember() {
            return USER.equals(this);
        }

        public boolean isAnonymous() {
            return ANONYMOUS.equals(this);
        }
    }

    public LoginUser(String memberUuid, List<Authority> roles) {
        this.memberUuid = memberUuid;
        this.roles = roles;
    }

    public <T> UserAction<T> act(Class<T> returnType) {
        return new UserAction<>(this);
    }

    public static class UserAction<T> {
        private final LoginUser loginUser;
        private T returnValue;

        public UserAction(LoginUser loginUser) {
            this.loginUser = loginUser;
        }

        public UserAction<T> ifAnonymous(Supplier<T> supplier) {
            if (loginUser.isAnonymous()) {
                this.returnValue = supplier.get();
            }
            return this;
        }

        public UserAction<T> ifUser(Function<String, T> function) {
            if (loginUser.isUser()) {
                this.returnValue = function.apply(loginUser.getMemberUuid());
            }
            return this;
        }

        public UserAction<T> ifAdmin(Function<String, T> function) {
            if (loginUser.isAdmin()) {
                this.returnValue = function.apply(loginUser.getMemberUuid());
            }
            return this;
        }

        public UserAction<T> ifNotAnonymous(Function<String, T> function) {
            if (!loginUser.isAnonymous()) {
                this.returnValue = function.apply(loginUser.getMemberUuid());
            }
            return this;
        }

        public T getResult() {
            return returnValue;
        }
    }

    public boolean isAnonymous() {
        return CollectionUtils.isEmpty(roles) || roles.contains(Authority.ANONYMOUS);
    }

    public boolean isUser() {
        return !CollectionUtils.isEmpty(roles) && roles.contains(Authority.USER);
    }

    public boolean isAdmin() {
        return !CollectionUtils.isEmpty(roles) && roles.contains(Authority.ADMIN);
    }

    public <T extends Throwable> LoginUser throwIfAnonymous(Supplier<? extends T> supplier)
        throws T {
        if (isAnonymous()) {
            throw supplier.get();
        }
        return this;
    }

    public String getMemberUuid() {
        return memberUuid;
    }
}
