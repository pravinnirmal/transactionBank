package pnd.pravin.bank.BankRepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pnd.pravin.bank.BankEntitites.TransactionStatement;
import pnd.pravin.bank.BankEntitites.TransactionStatementEmbeddedId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository

public interface TransactionStatementRepository extends CrudRepository <TransactionStatement, TransactionStatementEmbeddedId> {

//    Optional<TransactionStatement> findTransactionStatementByuserName(String userName);

    @Query("SELECT ts FROM TransactionStatement ts WHERE ts.transferFrom = :userName")
    List <TransactionStatement> findByTransactionStatementEmbeddedIdUserName (String userName);


}
