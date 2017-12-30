package sec.project.domain;

public class LoggedInUser {
    
    private static String name;
    private static String role;
    
    public LoggedInUser() {
        name = null;
        role = null;
    }
    
    static public void logout() {
        name = null;
        role = null;
    }
    
    static public String getLoggedInName() {
        return name;
    }

    static public String getLoggedInRole() {
        return role;
    }
    
    static public void setLoggedInName(String newName) {
        name = newName;
    }

    static public void setLoggedInRole(String newRole) {
        role = newRole;
    }
    
    static public Boolean isLoggedIn() {
        return name != null;
    }
}
