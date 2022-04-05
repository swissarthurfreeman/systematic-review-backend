package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.api.rest.UserRestService;
import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    private final List<User> users;

    public UserServiceImpl() {
        users = new ArrayList<>();
        users.add(new User("john.doe", "john.doe@unige.ch", "0ce40162-aaaa-5666-aaaa-8f36f394ffd9"));
        users.add(new User("user2", "user2@etu.unige.ch", "81391884-7b3e-4b80-a82b-d2445ba7e806"));
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User get(UUID id) {
        return users.stream().filter(user -> user.getId().equals(id)).findAny().orElse(null);
    }

    @Override
    public void create(User user) {
        users.add(user);
    }

    @Override
    public void delete(UUID id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
