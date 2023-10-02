package pnd.pravin.bank.BankServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
    public void addUserToDatabase(AddUserEntity addUserEntity, AddUserAuthority addUserAuthority) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
        addUserEntity.setPassword(encoder.encode(addUserEntity.getPassword()));

        addUserEntity.setEnabled(1);addUserAuthority.setUsername(addUserEntity.getUsername());
        addUserAuthority.setRole("ROLE_USER");
        addUserRepository.save(addUserEntity);
        addUserAuthorityRepository.save(addUserAuthority);
    }

    public double getAccountDetails(String UserName) {
        double money = 0L;
        Optional <PersonalAccountEntity> optionalPersonalAccount = personalAccountRepository.findById(UserName);
        if(optionalPersonalAccount.isPresent()){
            PersonalAccountEntity account = optionalPersonalAccount.get();
             money= account.getMoney();
         }
         return money;
        }


}
