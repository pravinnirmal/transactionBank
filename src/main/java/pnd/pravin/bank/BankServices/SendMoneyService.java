package pnd.pravin.bank.BankServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;
import pnd.pravin.bank.BankEntitites.SendMoneyEntity;
import pnd.pravin.bank.BankRepositories.PersonalAccountRepository;

@Service
public class SendMoneyService {

    @Autowired
    PersonalAccountRepository personalAccountRepository;


    public String sendMoneyToUser(SendMoneyEntity sendMoneyEntity) {

        if (personalAccountRepository.findById(sendMoneyEntity.getUserName()).isPresent()) {
            addMoneyToUser(sendMoneyEntity.getMoney(), sendMoneyEntity.getUserName());
        } else {
            return "Transfer Failed. Reason: User not found";
        }
        return "Transfer Success";
    }

    private void addMoneyToUser(Double moneyToSend, String userName) {

        PersonalAccountEntity account = personalAccountRepository.findById(userName).get();
        Double currentBalance = account.getMoney();
        currentBalance += moneyToSend;
        account.setMoney(currentBalance);
        personalAccountRepository.save(account);

    }
}
