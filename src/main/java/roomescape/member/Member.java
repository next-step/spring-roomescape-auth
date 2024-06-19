package roomescape.member;

import java.util.concurrent.atomic.AtomicLong;

public class Member {

    private final static AtomicLong atomicLong = new AtomicLong(1);

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
        this.id = atomicLong.incrementAndGet();
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
