package pnd.pravin.bank.Bankservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankEntitites.AddUserAuthority;
import pnd.pravin.bank.BankEntitites.AddUserEntity;
import pnd.pravin.bank.BankRepositories.AddUserAuthorityRepository;
import pnd.pravin.bank.BankRepositories.AddUserRepository;

@Service
public class BankService {
@Autowired
    AddUserRepository addUserRepository;
@Autowired
AddUserAuthorityRepository addUserAuthorityRepository;
    public void addUserToDatabase(AddUserEntity addUserEntity, AddUserAuthority addUserAuthority) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
        addUserEntity.setPassword(encoder.encode(addUserEntity.getPassword()));

        addUserEntity.setEnabled(1);addUserAuthority.setUsername(addUserEntity.getUsername());
        addUserAuthority.setRole("ROLE_USER");
        addUserRepository.save(addUserEntity);
        addUserAuthorityRepository.save(addUserAuthority);
    }
}
