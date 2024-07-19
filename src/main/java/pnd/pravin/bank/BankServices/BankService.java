package pnd.pravin.bank.BankServices;

import org.apache.catalina.security.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankConstants.BankConstants;
import pnd.pravin.bank.BankEntitites.AddUserAuthority;
import pnd.pravin.bank.BankEntitites.AddUserEntity;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;
import pnd.pravin.bank.BankRepositories.AddUserAuthorityRepository;
import pnd.pravin.bank.BankRepositories.AddUserRepository;
import pnd.pravin.bank.BankRepositories.PersonalAccountRepository;

import java.util.Optional;

@Service
public class BankService {
@Autowired
AddUserRepository addUserRepository;
@Autowired
AddUserAuthorityRepository addUserAuthorityRepository;
@Autowired
PersonalAccountRepository personalAccountRepository;
private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    public void addUserToDatabase(AddUserEntity addUserEntity, AddUserAuthority addUserAuthority) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
        addUserEntity.setPassword(encoder.encode(addUserEntity.getPassword()));
        addUserEntity.setEnabled(BankConstants.ENABLED_ONE);
        addUserAuthority.setUsername(addUserEntity.getUsername());
        addUserAuthority.setRole(BankConstants.ROLE_USER);
        addUserRepository.save(addUserEntity);
        addUserAuthorityRepository.save(addUserAuthority);
        logger.info("Added User to repository");
    }

    public double getAccountDetails(String UserName) {
        double money = BankConstants.SET_ZERO;
        Optional <PersonalAccountEntity> optionalPersonalAccount = personalAccountRepository.findById(UserName);
        if(optionalPersonalAccount.isPresent()){
            PersonalAccountEntity account = optionalPersonalAccount.get();
             money= account.getMoney();
         }
         return money;
        }


}
