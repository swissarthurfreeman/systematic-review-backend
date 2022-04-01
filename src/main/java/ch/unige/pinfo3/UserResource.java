package ch.unige.pinfo3;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User get(@PathParam("id") String id) {
        Iterator<User> it = users.iterator();

        while(it.hasNext()) {
            User curr = it.next();
            return curr;            // returns random first element for now.
        }
        return new User("user93", "user93@unige.ch");
    }

    @POST
    public Set<User> addUser(User user){
        users.add(user);
        return users;
    }


    @DELETE
    @Path("/{id}")
    public Set<User> delete(@PathParam("id") UUID id) {
        users.removeIf(i -> i.getId().equals(id));
        return users;
    }
}
