package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAll();
    User get(UUID id);
    void create(User user);
    void delete(UUID id);
}
