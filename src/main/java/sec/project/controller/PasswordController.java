package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.config.CustomUserDetailsService;
import static sec.project.domain.LoggedInUser.getLoggedInName;
import static sec.project.domain.LoggedInUser.isLoggedIn;
import static sec.project.domain.LoggedInUser.logout;
import static sec.project.domain.LoggedInUser.setLoggedInName;
import sec.project.domain.User;
import sec.project.repository.SignupRepository;

@Controller
public class PasswordController {
    
    @Autowired
    private CustomUserDetailsService uds;

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String loadLoginForm(Model model) {
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        model.addAttribute("username", getLoggedInName());
        return "password";
    }

    //@RequestMapping(value = "/password", method = RequestMethod.POST)
    @RequestMapping(value = "/password/{username}", method = RequestMethod.POST)
    public String doPasswordChange(@PathVariable("username") String name, @RequestParam String password1, @RequestParam String password2) {
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        
        if(!password1.equals(password2)) {
            // passwords don't match ==> back to password-page
            return "redirect:/password";
        }
        
        PasswordEncoder pe = new BCryptPasswordEncoder();
        // inputfield for name in the form can be removed if this metod declaration is modified appropriately
        //name = getLoggedInUsername();
        User user = uds.getUser(name);
        user.setPassword(pe.encode(password1));
        
        return "redirect:/password_changed";
    }

    @RequestMapping(value = "/password_changed", method = RequestMethod.GET)
    public String showPasswordChanged(Model model) {
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        model.addAttribute("username", getLoggedInName());
        return "password_changed";
    }

    private String getLoggedInUsername() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
