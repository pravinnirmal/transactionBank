package pnd.pravin.bank.BankRepositories;

import org.springframework.data.repository.CrudRepository;
import pnd.pravin.bank.BankEntitites.TransactionStatement;

import java.util.List;

public interface TransactionStatementRepository extends CrudRepository <TransactionStatement, String> {
//    List<TransactionStatement> findIdByList (String userName);

}
