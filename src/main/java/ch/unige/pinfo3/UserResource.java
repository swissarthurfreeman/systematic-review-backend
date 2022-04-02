package ch.unige.pinfo3;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private ArrayList<User> users = new ArrayList<>();

    public UserResource() {
        users.add(new User("john_doe", "john.doe@unige.ch", "81391884-7b3e-4b79-a82b-d2445ba7e806"));
        users.add(new User("user2", "user2@etu.unige.ch", "81391884-7b3e-4b80-a82b-d2445ba7e806"));
    }

    @GET
    public ArrayList<User> returnUsers() {
        return users;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User get(@PathParam("id") String id) {

        for (User curr : users) {
            if (curr.getId().toString().equals(id))
                return curr;            // returns random first element for now.
        }
        return null;
    }

    @POST
    public User addUser(User user){
        users.add(user);
        return user;
    }

    @DELETE
    @Path("/{id}")
    public User delete(@PathParam("id") UUID id) {
        users.removeIf(i -> i.getId().equals(id));
        return null;
    }
}
