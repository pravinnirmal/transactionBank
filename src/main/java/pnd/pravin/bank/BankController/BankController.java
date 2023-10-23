package pnd.pravin.bank.BankController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pnd.pravin.bank.BankEntitites.AddUserAuthority;
import pnd.pravin.bank.BankEntitites.AddUserEntity;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;
import pnd.pravin.bank.BankServices.BankService;
import pnd.pravin.bank.BankEntitites.SendMoneyEntity;
import pnd.pravin.bank.BankServices.SendMoneyService;

@Controller
public class BankController {
    @Autowired
    BankService bankService;

    @Autowired
    SendMoneyService sendMoneyService;

    Authentication authentication;


    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("admin/dashboard")
    public String viewAdminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("user/dashboard")
    public String viewUserDashboard(@ModelAttribute("PersonalAccountEntity") PersonalAccountEntity personalAccountEntity, Model model) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String UserName = authentication.getName();
        double money = bankService.getAccountDetails(UserName);

        model.addAttribute("money", money);
        model.addAttribute("username", UserName);

        return "user/dashboard";
    }

    @GetMapping("admin/adduser")
    public String addUserToDb(Model model) {
        model.addAttribute("AddUserEntity", new AddUserEntity());
        return "admin/adduser";
    }

    @PostMapping("admin/adduser")
    public String addUserToDb(@ModelAttribute("AddUserEntity") AddUserEntity addUserEntity, Model model) {
        model.addAttribute("AddUserEntity", new AddUserEntity());
        AddUserAuthority addUserAuthority = new AddUserAuthority();
        bankService.addUserToDatabase(addUserEntity, addUserAuthority);
        return "/admin/registrationsuccess";
    }

    @GetMapping("user/dashboard/moneytransfer/sendmoney")
    public String sendMoney(Model model) {
        model.addAttribute("SendMoney", new SendMoneyEntity());
        return "user/dashboard/moneytransfer/sendmoney";
    }

    @PostMapping("user/dashboard/moneytransfer/sendmoney")
    public String sendMoneyToUser(@ModelAttribute("SendMoney") SendMoneyEntity sendMoneyEntity, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("SendMoney", new SendMoneyEntity());
        String transferee = authentication.getName();
        String transferStatus = sendMoneyService.sendMoneyToUser(sendMoneyEntity, transferee );
        model.addAttribute("transferStatus", transferStatus);
        return  "user/dashboard/moneytransfer/transferstatus";
    }




}
