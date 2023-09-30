package pnd.pravin.bank.BankRepositories;

import org.springframework.data.repository.CrudRepository;
import pnd.pravin.bank.BankEntitites.AddUserEntity;

public interface AddUserRepository extends CrudRepository <AddUserEntity, String> {
}
