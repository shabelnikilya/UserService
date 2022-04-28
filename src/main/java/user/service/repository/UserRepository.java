package user.service.repository;

import org.springframework.data.repository.CrudRepository;
import user.service.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findBySecondName(String secondName);
}
