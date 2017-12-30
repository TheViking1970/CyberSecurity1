package sec.project.domain;

public class User {

    private String name;
    private String password;
    private String role;

    public User(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }
    
}
