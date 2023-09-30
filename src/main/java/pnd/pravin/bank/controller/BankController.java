package pnd.pravin.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pnd.pravin.bank.BankEntitites.AddUserEntity;

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

    @GetMapping("admin/adduser")
    public String addUserToDb(){
        return "admin/adduser";
    }

//    @PostMapping
//    public String addUserToDb(AddUserEntity addUserEntity, Model model){
//        return "user added";
//    }

}
