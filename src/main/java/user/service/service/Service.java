package user.service.service;

import user.service.models.User;

import java.util.List;

public interface Service<T> {

    List<T> findAll();

    T findById(int id);

    T findBySecondName(String secondName);

    void save(User user);

    void remove(User user);
}
