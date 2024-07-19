package pnd.pravin.bank.BankServices;

import org.apache.catalina.security.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnd.pravin.bank.BankConstants.BankConstants;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;
import pnd.pravin.bank.BankEntitites.SendMoneyEntity;
import pnd.pravin.bank.BankEntitites.TransactionStatement;
import pnd.pravin.bank.BankEntitites.TransactionStatementEmbeddedId;
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

    private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private Authentication authentication;

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
                    logger.warn("User {} doesn't have sufficient balance to send money", sendMoneyEntity.getUserName());
                    return transferFailedNoSufficientBalance;
                } else {
                    addMoneyToUser(sendMoneyEntity.getMoney(), sendMoneyEntity.getUserName(), transferee);
                    logger.info("Transferred successfully to user {}", transferee);

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
        saveToSecondPersonTransactions(userName, moneySent, transferedTo, balanceMoney);
    }
    @Transactional
    public void saveToSecondPersonTransactions(String userName, Double moneySent, String transferedTo, Double balanceMoney) {
        Queue<TransactionStatement> statementQueue = new ArrayDeque<>();

        // Fetch the existing transaction statements for the user
        List<TransactionStatement> existingStatements = transactionStatementRepository.findByTransactionStatementEmbeddedIdUserName(userName);
        statementQueue.addAll(existingStatements);

        if (statementQueue.size() > 4) {
            logger.info(BankConstants.REMOVE_OLD_STATEMENT);
            // Remove the oldest statement
            statementQueue.poll();
        }

        TransactionStatement newTransactionStatement = createNewTransactionStatement(userName, moneySent, transferedTo, balanceMoney);
        statementQueue.add(newTransactionStatement);

        // Update the database with the modified statements
        updateTransactionStatements(userName, statementQueue);
        logger.info(BankConstants.UPDATED_TRANSACTION);
    }

    private TransactionStatement createNewTransactionStatement(String userName, Double moneySent, String transferedTo, Double balanceMoney) {
        // Create a new TransactionStatement with the relevant details
        TransactionStatement newTransactionStatement = new TransactionStatement();
        TransactionStatementEmbeddedId newEmbeddedId = new TransactionStatementEmbeddedId(generateTransactionId(userName), transferedTo);
        PersonalAccountEntity personalAccountEntity = personalAccountRepository.findById(userName).get();
        newTransactionStatement.setTransactionStatementEmbeddedId(newEmbeddedId);
        newTransactionStatement.setAmount(moneySent);
        newTransactionStatement.setBalance(balanceMoney);
        newTransactionStatement.setTransferFrom(userName);
        newTransactionStatement.setTimestamp(Timestamp.from(Instant.now()));
        newTransactionStatement.setCreditDebit("Debit");


        return newTransactionStatement;
    }

    private void updateTransactionStatements(String userName, Queue<TransactionStatement> statementQueue) {
        // Update the database with the modified transaction statements
        List<TransactionStatement> updatedStatements = new ArrayList<>(statementQueue);
        transactionStatementRepository.saveAll(updatedStatements);
    }


       private void addNewTransactionToUser(String userName, Double moneySent, String transferedTo, TransactionStatement statement1) {
        statement1.setTransactionStatementEmbeddedId(new TransactionStatementEmbeddedId(userName, generateTransactionId(userName)));
        statement1.setAmount(moneySent);
        statement1.setBalance(moneySent + statement1.getBalance());
        statement1.setTransferFrom(transferedTo);
        statement1.setTimestamp(Timestamp.from(Instant.now()));
        statement1.setCreditDebit("Credit");


        transactionStatementRepository.save(statement1);

    }

    private TransactionStatement saveToStatementRepo(String userName, Double moneySent, String transferedTo, TransactionStatement statement1) {
        statement1.setAmount(moneySent);
        statement1.setBalance(moneySent + statement1.getBalance());
        statement1.setCreditDebit("Credit");
        statement1.setTimestamp(Timestamp.from(Instant.now()));
        statement1.setTransferFrom(transferedTo);
        statement1.setTransactionStatementEmbeddedId(new TransactionStatementEmbeddedId(userName, generateTransactionId(userName)));
        transactionStatementRepository.save(statement1);
        return statement1;
    }

    private String generateTransactionId(String userName) {
        Random random = new Random();
        return userName.substring(0, 3) + random.nextInt(999999);
    }
}






