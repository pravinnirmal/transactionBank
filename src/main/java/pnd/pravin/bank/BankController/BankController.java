package pnd.pravin.bank.BankController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pnd.pravin.bank.BankEntitites.AddUserAuthority;
import pnd.pravin.bank.BankEntitites.AddUserEntity;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;
import pnd.pravin.bank.BankServices.BankService;

@Controller
public class BankController {
@Autowired
    BankService bankService;

    @GetMapping("/")
    public String homePage(){
        return "index";
    }

    @GetMapping ("admin/dashboard")
    public String viewAdminDashboard(){
        return "admin/dashboard";
    }

    @GetMapping ("user/dashboard")
    public String viewUserDashboard(@ModelAttribute("PersonalAccountEntity") PersonalAccountEntity personalAccountEntity, Model model){
//        bankService.getAccountDetails();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String UserName =authentication.getName();
        double money = bankService.getAccountDetails(UserName);

        model.addAttribute("money", money);

        return "user/dashboard";
    }

    @GetMapping("admin/adduser")
    public String addUserToDb(Model model){
        model.addAttribute("AddUserEntity", new AddUserEntity());
        return "admin/adduser";
    }

    @PostMapping("admin/adduser")
    public String addUserToDb(@ModelAttribute("AddUserEntity") AddUserEntity addUserEntity, Model model){
        model.addAttribute("AddUserEntity", new AddUserEntity());
        AddUserAuthority addUserAuthority = new AddUserAuthority();
        bankService.addUserToDatabase(addUserEntity, addUserAuthority);
        return "/admin/registrationsuccess";
    }

}
