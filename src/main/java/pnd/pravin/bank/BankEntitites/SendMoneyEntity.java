package pnd.pravin.bank.BankEntitites;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankRepositories.PersonalAccountRepository;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Service
@Slf4j
public class SendMoneyEntity {
    private String userName;
    private Double money;

    @Autowired
    PersonalAccountRepository personalAccountRepository;

    @Autowired
    public SendMoneyEntity(PersonalAccountRepository personalAccountRepository) {
        this.personalAccountRepository = personalAccountRepository;
    }

    public SendMoneyEntity(String formData, Double moneyData) {
        this.userName = formData;
        this.money = moneyData;
    }

    @PostConstruct
    public void init() {
        if (personalAccountRepository != null) {
           log.info("Personal Account Repository is not null");
        } else {
            log.warn("Personal Account Repository is null");
        }
    }





}

