package user.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.service.models.User;
import user.service.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements user.service.service.Service<User> {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    public User findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public User findBySecondName(String secondName) {
        return repository.findBySecondName(secondName);
    }

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public void remove(User user) {
        repository.delete(user);
    }
}
