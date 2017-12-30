package sec.project.config;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sec.project.domain.LoggedInUser;
import sec.project.domain.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    static private Map<String, User> accountDetails;
    public LoggedInUser loggedInUser = new LoggedInUser();

    @PostConstruct
    public void init() {
        // this data would typically be retrieved from a database
        PasswordEncoder crypt = new BCryptPasswordEncoder();
        this.accountDetails = new TreeMap<>();
        this.accountDetails.put("ted", new User("ted", crypt.encode("ted"), "user"));
        this.accountDetails.put("ben", new User("ben", crypt.encode("ben"), "manager"));
        this.accountDetails.put("don", new User("don", crypt.encode("don"), "admin"));
        //"ted", "$2a$06$rtacOjuBuSlhnqMO2GKxW.Bs8J6KI0kYjw/gtF0bfErYgFyNTZRDm"
    }
    
    public User getUser(String name) {
        return accountDetails.get(name);
    }
    
    public Map<String, User> getAllUsers() {
        return accountDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!this.accountDetails.containsKey(username)) {
            throw new UsernameNotFoundException("No such user: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                username,
                this.accountDetails.get(username).getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority(this.accountDetails.get(username).getRole().toUpperCase())));
    }
}
