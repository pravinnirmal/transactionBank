package pnd.pravin.bank.BankController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pnd.pravin.bank.BankEntitites.*;
import pnd.pravin.bank.BankRepositories.TransactionStatementRepository;
import pnd.pravin.bank.BankServices.BankService;
import pnd.pravin.bank.BankServices.SendMoneyService;
import pnd.pravin.bank.BankServices.TransactionStatementRepositoryService;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BankController {
    @Autowired
    BankService bankService;

    @Autowired
    SendMoneyService sendMoneyService;

//    @Autowired
//    TransactionStatementRepository transactionStatementRepository;

    @Autowired
    TransactionStatementRepositoryService transactionStatementRepositoryService;

    Authentication authentication;
    String loggedInUser;


    @GetMapping("/")
    public String homePage() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return "index";
    }

    @GetMapping("admin/dashboard")
    public String viewAdminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("user/dashboard")
    public String viewUserDashboard(@ModelAttribute("PersonalAccountEntity") PersonalAccountEntity personalAccountEntity, Model model) {

        loggedInUser = authentication.getName();
        double money = bankService.getAccountDetails(loggedInUser);

        model.addAttribute("money", money);
        model.addAttribute("username", loggedInUser);

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
//        String transferee = authentication.getName();
        String transferStatus = sendMoneyService.sendMoneyToUser(sendMoneyEntity, loggedInUser );
        model.addAttribute("transferStatus", transferStatus);
        return  "user/dashboard/moneytransfer/transferstatus";
    }

    @GetMapping ("user/dashboard/statement")
    public String userStatement(@ModelAttribute("TransactionStatement") TransactionStatement transactionStatement, Model model) {
        String userId = loggedInUser;
        List<TransactionStatement> userStatements = new ArrayList<>();
        userStatements = transactionStatementRepositoryService.getStatementsByUserName(userId);
        model.addAttribute("userStatements", userStatements);

        return  "user/dashboard/statement/statement";
    }




}
