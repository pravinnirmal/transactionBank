package pnd.pravin.bank.BankEntitites;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankRepositories.PersonalAccountRepository;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Service
public class SendMoneyEntity {
    private String userName;
    private Double money;


    PersonalAccountRepository personalAccountRepository;

    @Autowired
    public SendMoneyEntity(PersonalAccountRepository personalAccountRepository) {
        this.personalAccountRepository = personalAccountRepository;
    }

    public SendMoneyEntity(String formData, Double moneyData) {
    }


    @PostConstruct
    public void init() {
        if (personalAccountRepository != null) {
            System.out.println("personalAccountRepository is not null");
        } else {
            System.out.println("personalAccountRepository is null");
        }
    }





}

