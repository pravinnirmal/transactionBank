package pnd.pravin.bank.Bankservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankEntitites.AddUserEntity;
import pnd.pravin.bank.BankRepositories.AddUserRepository;

@Service
public class BankService {
@Autowired
    AddUserRepository addUserRepository;
    public void addUserToDatabase(AddUserEntity addUserEntity) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
        addUserEntity.setPassword(encoder.encode(addUserEntity.getPassword()));
        addUserEntity.setEnabled(1);
        addUserRepository.save(addUserEntity);
    }
}
