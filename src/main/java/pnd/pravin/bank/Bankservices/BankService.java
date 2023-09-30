package pnd.pravin.bank.Bankservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pnd.pravin.bank.BankEntitites.AddUserEntity;
import pnd.pravin.bank.BankRepositories.AddUserRepository;

@Service
public class BankService {
@Autowired
    AddUserRepository addUserRepository;
    public void addUserToDatabase(AddUserEntity addUserEntity) {
        String password = addUserEntity.getPassword();
//        password
        addUserRepository.save(addUserEntity);
    }
}
