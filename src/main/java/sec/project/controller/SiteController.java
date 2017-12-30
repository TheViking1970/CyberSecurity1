package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.config.CustomUserDetailsService;
import static sec.project.domain.LoggedInUser.getLoggedInName;
import static sec.project.domain.LoggedInUser.getLoggedInRole;
import static sec.project.domain.LoggedInUser.isLoggedIn;
import sec.project.domain.Message;
import sec.project.domain.User;
import sec.project.repository.MessagesRepository;

@Controller
public class SiteController {

    @Autowired
    private CustomUserDetailsService uds;
    @Autowired
    private MessagesRepository messagesRepo;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String showSecretAdminPage(Model model) {
        model.addAttribute("username", getLoggedInName());
        return "home";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String loadAdminpage(Model model) {
        model.addAttribute("username", getLoggedInName());
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        
        // No-one will ever find this über secret page, so no need for authentication!
        /*
        if(!getLoggedInRole().equals("ADMIN")) {
            return "redirect:/error";
        }
        */
        
        return "admin";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String loadUsersList(Model model) {
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        model.addAttribute("username", getLoggedInName());
        Map<String, User> accounts = uds.getAllUsers();
        List<User> users = new ArrayList<>();
        for(String name : accounts.keySet()) {
            users.add(accounts.get(name));
        }
        model.addAttribute("users", users);
        model.addAttribute("admin", getLoggedInRole().equals("USER")?false:true);
        // model.addAttribute("admin", getLoggedInRole().equals("ADMIN"));
        return "users";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public String loadTransferForm(Model model) {
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        model.addAttribute("username", getLoggedInName());
        return "transfer";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String doTransfer(Model model, @RequestParam String reciever, @RequestParam String amount) {
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        // Do transfer ... not implemented!
        model.addAttribute("reciever", reciever);
        model.addAttribute("amount", amount);
        return "transfer_done";
    }

    
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String loadMessages(Model model) {
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        model.addAttribute("username", getLoggedInName());
        model.addAttribute("messages", messagesRepo.findAll());
        return "messages";
    }
    
    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public String addMessage(Model model, @RequestParam String name, @RequestParam String message) {
        if(!isLoggedIn()) {
            return "redirect:*";
        }
        if(!name.isEmpty() && !message.isEmpty()) {
            messagesRepo.save(new Message(name, message));
        }
        return "redirect:/messages";
    }

}
