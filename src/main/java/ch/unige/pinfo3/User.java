package ch.unige.pinfo3;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private UUID uuid;
    private String username;
    private String email;
    private ArrayList<String> quesries;

    public User(String username, String email){
         uuid = UUID.randomUUID();
         this.username = username;
         this.email = email;
         this.quesries = new ArrayList<>();
    }


}
