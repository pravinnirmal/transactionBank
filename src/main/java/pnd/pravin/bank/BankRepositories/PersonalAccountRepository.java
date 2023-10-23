package pnd.pravin.bank.BankRepositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;
@Repository

public interface PersonalAccountRepository extends CrudRepository <PersonalAccountEntity, String> {
}
