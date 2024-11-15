package vttp.batchb.ssf.week5project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/secret")
public class SecretController {
    @PostMapping
    public String getSecret(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam(value = "captcha", required = false) String captcha,
                            HttpSession session, Model model)
    {   
        Boolean accountLocked = (Boolean) session.getAttribute("accountLocked");
        if(accountLocked!= null && accountLocked){
            return "accountLocked";
        }
        Integer failedAttempts = (Integer) session.getAttribute("failedAttempts");
        if (failedAttempts == null) {
            failedAttempts = 0;
        }

        if ((username == null || username.isEmpty()) || (password == null || password.isEmpty())) {
            model.addAttribute("error", "Please input username and password.");
            if (failedAttempts >= 3) {
                model.addAttribute("captchaRequired", true);
            }
            return "index";
        }

        if (failedAttempts >= 3) {
            model.addAttribute("captchaRequired", true);

            if (captcha == null || !captcha.equals("4")) {
                model.addAttribute("error", "CAPTCHA is required and must be correct.");
                return "index";
            }
        }
        
        
        if ("lx".equals(username) && "password".equals(password)) {
            session.setAttribute("loggedIn", true);
            session.removeAttribute("failedAttempts");
            return "secret"; 
        } else {
            failedAttempts++;
            session.setAttribute("failedAttempts", failedAttempts);

            if (failedAttempts >= 4) {
                session.setAttribute("accountLocked", true);
                return "accountLocked";
            }

            model.addAttribute("error", "Invalid username or password");
            if (failedAttempts >= 3) {
                model.addAttribute("captchaRequired", true);
            }
            return "index";
        }
    }

    @PostMapping("/exit")
    public String getOut(HttpSession session,Model model){
        session.invalidate();
        return "index";
    }

    @GetMapping
    public String getCheater(HttpSession session){

        Boolean accountLocked = (Boolean) session.getAttribute("accountLocked");
        if (accountLocked != null && accountLocked) {
            return "accountLocked";
        }

        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn != null && loggedIn) {
            return "secret";
        } else{
            return "index";
        }
    }
    @PostMapping("/reset")
    public String resetAccount(HttpSession session) {
        session.removeAttribute("failedAttempts");
        session.removeAttribute("accountLocked");
        return "index";
    }
}
