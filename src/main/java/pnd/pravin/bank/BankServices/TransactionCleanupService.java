//package pnd.pravin.bank.BankServices;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import pnd.pravin.bank.BankEntitites.TransactionStatement;
//import pnd.pravin.bank.BankRepositories.TransactionStatementRepository;
//
//import java.util.List;
//
//@Service
//public class TransactionCleanupService {
//
//    @Autowired
//    private TransactionStatementRepository transactionStatementRepository;
//
//    @Value("${transaction.statement.max.count}")
//    private int maxTransactionCount;
//
//    @Scheduled(fixedRate = 0)
//    public void cleanUpTransactionStatements() {
//        List<String> userNames = transactionStatementRepository.findByTransactionStatementEmbeddedIdUserName();
//
//        for (String userName : userNames) {
//            List<TransactionStatement> statements = transactionStatementRepository.findLatestStatements(userName, maxTransactionCount);
//
//            if (statements.size() > maxTransactionCount) {
//                List<TransactionStatement> statementsToDelete = statements.subList(0, statements.size() - maxTransactionCount);
//                transactionStatementRepository.deleteAll(statementsToDelete);
//            }
//        }
//    }
//}
