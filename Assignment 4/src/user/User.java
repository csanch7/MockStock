package user;

import order.*;
import currentMarket.*;

import java.util.HashMap;

public class User implements CurrentMarketObserver{
    private String userId;
    private HashMap<String, TradableDTO> tradables = new HashMap<>();
    private HashMap<String, CurrentMarketSide[]> currentMarkets = new HashMap<>();

    public User(String userId) {
        setUserId(userId);
    }

    private void setUserId(String userId) {
        if (userId == null || userId.length() != 3) {
            throw new InvalidTradableException("invalid userid");
        }
        for (int i = 0; i < userId.length(); i++) {
            if (!Character.isLetter(userId.charAt(i))){
                throw new InvalidTradableException("invalid userid");
            }
        }
        this.userId = userId;
    }

    public void updateTradable (TradableDTO o){
        if (o == null){
        }
        else {
            if (tradables.get(o.tradableId())!= null){
                tradables.remove(o.tradableId());
                tradables.put(o.tradableId(), o);
            }
            else{
                tradables.put(o.tradableId(), o);
            }
        }
    }

    @Override
    public String toString() {
        String output = "";
        output += "User: " + userId + "\n";
        for (TradableDTO t : tradables.values()){
            output += "\t " + t + "\n";
        }
        return output;
    }

    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        CurrentMarketSide[] lst = new CurrentMarketSide[]{buySide, sellSide};
        currentMarkets.put(symbol, lst);
    }

    public String getCurrentMarkets(){
        String output = "";
        for (String symbol : currentMarkets.keySet()){
            CurrentMarketSide[] lst = currentMarkets.get(symbol);
            CurrentMarketSide buySide = lst[0];
            CurrentMarketSide sellSide = lst[1];
            output += symbol + "\t" + buySide.toString() + " - " + sellSide.toString() + "\n";
        }
        return output;
    }

    public String getUserId() {
        return userId;
    }
}
