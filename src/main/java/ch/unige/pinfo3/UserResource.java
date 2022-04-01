package ch.unige.pinfo3;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private Set<User> users = Collections.synchronizedSet(new LinkedHashSet<>());

    public UserResource() {
        users.add(new User("user1", "user1@unige.ch"));
        users.add(new User("user2", "user2@etu.unige.ch"));
    }

    @GET
    public Set<User> returnUsers() {
        return users;
    }

    @POST
    public Set<User> addUser(String username, String email){
        users.add(new User(username,email));
        return users;
    }
    /*
    public Set<User> add(User user) {
        users.add(user);
        return users;
    }
     */

    @DELETE
    public Set<User> delete(User user) {
        users.removeIf(i -> i.getId().equals(user.getId()));
        return users;
    }
}
