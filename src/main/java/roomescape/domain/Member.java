package roomescape.domain;

import java.util.Objects;
import org.thymeleaf.util.StringUtils;
import roomescape.exception.custom.PasswordMismatchException;

public class Member {

    private Long id;
    private String name;
    private String email;
    private String password;

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Member toEntity(Member member, Long id) {
        return new Member(id, member.getName(), member.getName(), member.getPassword());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new PasswordMismatchException();
        }
    }
}
