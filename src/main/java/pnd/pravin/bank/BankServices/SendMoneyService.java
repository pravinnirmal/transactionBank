package pnd.pravin.bank.BankServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;
import pnd.pravin.bank.BankEntitites.SendMoneyEntity;
import pnd.pravin.bank.BankEntitites.TransactionStatement;
import pnd.pravin.bank.BankRepositories.PersonalAccountRepository;
import pnd.pravin.bank.BankRepositories.TransactionStatementRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class SendMoneyService {

    @Autowired
    private PersonalAccountRepository personalAccountRepository;

    @Autowired
    private TransactionStatementRepository transactionStatementRepository;

    @Value("${money.transfer.failed.sameUser}")
    private String transferFailedSameUser;

    @Value("${money.transfer.failed.no-sufficient-balance}")
    private String transferFailedNoSufficientBalance;

    @Value("${money.transfer.failed.user-not-found}")
    private String transferFailedUserNotFound;

    @Value("${money.transfer.failed.transfer-success}")
    private String transferSuccess;

    public String sendMoneyToUser(SendMoneyEntity sendMoneyEntity, String transferee) {

        //Check if username is present in DB
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
        deductFromBalance(transferee, moneyToSend, userName);

    }

    private void deductFromBalance(String userName, Double moneySent, String transferedTo) {
        PersonalAccountEntity account = personalAccountRepository.findById(userName).get();
        Double balanceMoney = account.getMoney();
        balanceMoney -= moneySent;
        account.setMoney(balanceMoney);
        personalAccountRepository.save(account);
        saveToSecondPersonTransactions(userName, moneySent, transferedTo);
    }

//    public List <TransactionStatement> getTransactionStatementbyList(String userId){
//        return transactionStatementRepository.findIdByList(userId);
//
//    }

    private void saveToSecondPersonTransactions(String userName, Double moneySent, String transferedTo) {
//        Optional<TransactionStatement> statement = Optional.ofNullable(transactionStatementRepository.findAllById(Collections.singleton(userName)).iterator().next());
//        TransactionStatement statement1 = transactionStatementRepository.findAllById(Collections.singleton(userName)).iterator().next();

        Queue <TransactionStatement> statementQueue = new ArrayDeque<>();
//        Queue <List <TransactionStatement>> statementQueue = new ArrayDeque<>();
    if(transactionStatementRepository.findAllById(Collections.singleton(userName)).iterator().hasNext()) {
        statementQueue.add(transactionStatementRepository.findAllById(Collections.singleton(userName)).iterator().next());
//        statementQueue.add(getTransactionStatementbyList(userName));
    }
        TransactionStatement statement1 = transactionStatementRepository.findById(userName).get();

        if (statementQueue.size() > 4) {
            System.out.println(statementQueue.size());
            statementQueue.poll();
            statementQueue.add(saveToStatementRepo(userName, moneySent, transferedTo, statement1));
            System.out.println(transactionStatementRepository.count());


        } else {
            addNewTransactionToUser(userName, moneySent, transferedTo, statement1);

        }

    }

    private void addNewTransactionToUser(String userName, Double moneySent, String transferedTo, TransactionStatement statement1) {
        TransactionStatement statement = new TransactionStatement(
                userName,
                transferedTo,
                moneySent+statement1.getBalance(),
                moneySent,
                Timestamp.from(Instant.now()),
                "Credit",
                generateTransactionId(userName));

        transactionStatementRepository.save(statement);

    }

    private TransactionStatement saveToStatementRepo(String userName, Double moneySent, String transferedTo, TransactionStatement statement1) {
        statement1.setAmount(moneySent);
        statement1.setBalance(moneySent + statement1.getBalance());
        statement1.setCreditDebit("Credit");
        statement1.setTimestamp(Timestamp.from(Instant.now()));
        statement1.setTransferFrom(transferedTo);
        statement1.setUserName(userName);
        statement1.setTransactionId(generateTransactionId(userName));
        transactionStatementRepository.save(statement1);
        return statement1;
    }

    private String generateTransactionId(String userName) {
        Random random = new Random();
        return userName.substring(0,3) +random.nextInt(999999);
    }
}





