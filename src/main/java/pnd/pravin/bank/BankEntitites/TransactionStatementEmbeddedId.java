package pnd.pravin.bank.BankEntitites;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TransactionStatementEmbeddedId implements Serializable {

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "user_id", unique = true)
    private String userName;
}
