package pnd.pravin.bank.BankServices;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankEntitites.TransactionStatement;
import pnd.pravin.bank.BankRepositories.TransactionStatementRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class TransactionStatementRepositoryService {

    @Autowired
    TransactionStatementRepository transactionStatementRepository;
    public TransactionStatementRepositoryService(TransactionStatementRepository transactionStatementRepository) {
        this.transactionStatementRepository = transactionStatementRepository;
    }

    public List<TransactionStatement> getStatementsByUserName(String userName) {
        return  transactionStatementRepository.findByTransactionStatementEmbeddedIdUserName(userName);
    }

    public List<TransactionStatement> getAllStatementsByUserName(String userName) {
        List <TransactionStatement> list = new ArrayList<>();
        transactionStatementRepository.findAll().forEach(list::add);
        return list;
    }

}
