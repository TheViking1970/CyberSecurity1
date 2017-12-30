package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.config.CustomUserDetailsService;
import static sec.project.domain.LoggedInUser.isLoggedIn;
import static sec.project.domain.LoggedInUser.logout;
import static sec.project.domain.LoggedInUser.setLoggedInName;
import static sec.project.domain.LoggedInUser.setLoggedInRole;
import sec.project.repository.SignupRepository;

@Controller
public class LoginController {

    //@Autowired
    
    
    @RequestMapping("*")
    public String defaultMapping() {
        if(!isLoggedIn()) {
            return "redirect:/login";
        }
        return "redirect:/home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loadLoginForm() {
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String doLogout() {
        logout();
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String submitLoginForm(@RequestParam String name, @RequestParam String password) {
        CustomUserDetailsService uds = new sec.project.config.CustomUserDetailsService();
        UserDetails ud = uds.loadUserByUsername(name);
        if(ud == null) {
            // user with usernamne not found ==> back to login
            return "login";
        }
        PasswordEncoder pe = new BCryptPasswordEncoder();
        if(!pe.matches(password, ud.getPassword())) {
            // password doesn't match users password in system ==> back to login
            return "login";
        }
        // login sucessfull!
        setLoggedInName(name);
        setLoggedInRole(ud.getAuthorities().toArray()[0].toString());
        return "redirect:/home";
    }

}
