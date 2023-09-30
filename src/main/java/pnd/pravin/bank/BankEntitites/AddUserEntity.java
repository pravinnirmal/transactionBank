package pnd.pravin.bank.BankEntitites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AddUserEntity {


    @Id
    private String username;
    private String password;
    private int enabled;

}
