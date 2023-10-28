package pnd.pravin.bank.BankEntitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactional_statement")
public class TransactionStatement {

//    @EmbeddedId
//    private TransactionStatementEmbedded transactionStatementEmbedded;

    @Column(name = "user_id")
    private String userName;
    @Column(name = "transfer_from")
    private String transferFrom;
    @Column(name = "balance")
    private double balance;
    @Column(name = "amount")
    private double amount;
    @Column(name = "timestamp")
    private Timestamp timestamp;
    @Column(name = "credit_debit")
    private String creditDebit;
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
}
