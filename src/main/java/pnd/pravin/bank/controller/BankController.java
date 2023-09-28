package pnd.pravin.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BankController {

    @GetMapping("/")
    public String homePage(){
        return "index";
    }

    @GetMapping ("admin/dashboard")
    public String viewDashboard(){
        return "admin/dashboard";
    }

    @GetMapping ("user/dashboard")
    public String viewUserDashboard(){
        return "user/dashboard";
    }

}
