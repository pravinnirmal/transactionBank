package pnd.pravin.bank.BankServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


    @Value("${money.transfer.failed.sameUser}")
    private String transferFailedSameUser;

    @Value("${money.transfer.failed.no-sufficient-balance}")
    private String transferFailedNoSufficientBalance;

    @Value("${money.transfer.failed.user-not-found}")
    private String transferFailedUserNotFound;

    @Value("${money.transfer.failed.transfer-success}")
    private String transferSuccess;

    public String sendMoneyToUser(SendMoneyEntity sendMoneyEntity, String transferee) {

        //Check if user name is present in DB
        if (personalAccountRepository.findById(sendMoneyEntity.getUserName()).isPresent()) {
            PersonalAccountEntity account = personalAccountRepository.findById(sendMoneyEntity.getUserName()).get();
            String transfererName = account.getUsername();
            Double transfererBalance = account.getMoney();
            // Check if transferer name and transferee name are same
            if (transferee.equals(transfererName)) {
                return transferFailedSameUser;
                // Check if transferer account balance is sufficient
            } else if (transfererBalance < sendMoneyEntity.getMoney()) {
                return transferFailedNoSufficientBalance;
            } else {
                addMoneyToUser(sendMoneyEntity.getMoney(), sendMoneyEntity.getUserName(), transferee);
            }
        } else {
            return transferFailedUserNotFound;
        }
        return transferSuccess;
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
