package com.teachandroid.app.data;

import com.teachandroid.app.api.service.RequestForSend;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KnownUsers {

    private static KnownUsers instance ;
    private static Map<Long,User> knownUsers;

    private KnownUsers() {
        knownUsers = new HashMap<Long, User>();
        knownUsers.put(1L,new User(1L));
    }

    public static KnownUsers getInstance(){
        if (instance==null) {instance=new KnownUsers();}
        return instance;
    }

    public void addUser (Long id, User user){
        knownUsers.put(id,user);
    }

    public User getUserFromId (Long id){
        User returnUser;
        returnUser = knownUsers.get(id);
        if (returnUser==null){
            returnUser = knownUsers.get(1L);

            RequestForSend.getInstance().sendMeUsers(id);
        }
        return returnUser;
    }

    public Set<Long> getAllKnownUsers(){
        Set<Long> keySet = knownUsers.keySet();
        Set<Long>resultSet = new HashSet<Long>();
        resultSet.addAll(keySet);
        resultSet.remove(1L);
        return resultSet;
    }
}
