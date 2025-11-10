package user;

import order.*;

import java.util.TreeMap;

public final class UserManager {
    private static UserManager instance;
    private TreeMap<String, User> users = new TreeMap<>();

    public static UserManager getInstance(){
        if (instance == null){
            instance = new UserManager();
        }
        return instance;
    }

    private UserManager(){

    }

    public void init(String[] usersIn) throws DataValidationException {
        if (usersIn != null){
            for (String user : usersIn){
                users.put(user, new User(user));
            }
        }
        else {
            throw new DataValidationException("usersIn cannot be null");
        }
    }

    public void updateTradable(String userId, TradableDTO o) throws DataValidationException {
        if (userId == null || o == null){
            throw new DataValidationException("userId or TradableDTO cannot be null");
        }
        if (users.get(userId)==null){
            throw new DataValidationException("user does not exist");
        }
        users.get(userId).updateTradable(o);
    }

    @Override
    public String toString() {
        String output = "";
        for (User user : users.values()){
            output = output + user.toString() + "\n";
        }
        return output;
    }

    public User getUser(String userId){
        if (users.get(userId) == null){
            throw new DataValidationException("user does not exist");
        }
        return users.get(userId);
    }
}
