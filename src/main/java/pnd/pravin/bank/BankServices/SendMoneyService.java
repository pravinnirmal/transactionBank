package pnd.pravin.bank.BankServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;
import pnd.pravin.bank.BankEntitites.SendMoneyEntity;
import pnd.pravin.bank.BankRepositories.PersonalAccountRepository;

@Service
public class SendMoneyService {

    @Autowired
    PersonalAccountRepository personalAccountRepository;


    public String sendMoneyToUser(SendMoneyEntity sendMoneyEntity, String transferee) {

        if (personalAccountRepository.findById(sendMoneyEntity.getUserName()).isPresent()) {
            addMoneyToUser(sendMoneyEntity.getMoney(), sendMoneyEntity.getUserName(), transferee);
        } else {
            return "Transfer Failed. Reason: User not found";
        }
        return "Transfer Success";
    }

    private void addMoneyToUser(Double moneyToSend, String userName, String transferee) {

        PersonalAccountEntity account = personalAccountRepository.findById(userName).get();
        Double currentBalance = account.getMoney();
        currentBalance += moneyToSend;
        account.setMoney(currentBalance);
        personalAccountRepository.save(account);
        deductFromBalance(transferee, moneyToSend);

    }

    private void deductFromBalance(String userName, Double moneySent) {
        PersonalAccountEntity account = personalAccountRepository.findById(userName).get();
        Double balanceMoney = account.getMoney();
        balanceMoney -= moneySent;
        account.setMoney(balanceMoney);
        personalAccountRepository.save(account);
    }
}
