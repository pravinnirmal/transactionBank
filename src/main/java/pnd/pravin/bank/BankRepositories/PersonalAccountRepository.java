package pnd.pravin.bank.BankRepositories;

import org.springframework.data.repository.CrudRepository;
import pnd.pravin.bank.BankEntitites.PersonalAccountEntity;

public interface PersonalAccountRepository extends CrudRepository <PersonalAccountEntity, String> {
}
