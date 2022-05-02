package user.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import user.service.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findBySecondName(String secondName);
}
