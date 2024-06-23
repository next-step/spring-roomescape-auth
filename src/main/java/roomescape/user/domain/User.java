package roomescape.user.domain;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    
    public User(Long id, String name, String email, String password, String role) {
        this(id, name, email, password, Role.valueOf(role));
    }

    public User(Long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User createUser(Long id, String name, String email, String password) {
        return new User(id, name, email, password, Role.USER);
    }

    public boolean isNotMatchPassword(String password) {
        return !this.password.equals(password);
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

    public Role getRole() {
        return role;
    }
}
