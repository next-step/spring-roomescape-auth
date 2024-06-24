package roomescape.member.domain;

import java.util.concurrent.atomic.AtomicLong;

public class Member {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(1);

    private Long id;
    private String name;
    private String email;
    private String password;

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

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String name, String email, String password) {
        this.id = ATOMIC_LONG.incrementAndGet();
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
