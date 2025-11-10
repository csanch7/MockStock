package currentMarket;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentMarketPublisher {

    private static CurrentMarketPublisher instance;
    private HashMap<String, ArrayList<CurrentMarketObserver>> filters = new HashMap<>();;

    public static CurrentMarketPublisher getInstance(){
        if (instance == null){
            instance = new CurrentMarketPublisher();
        }
        return instance;
    }

    private CurrentMarketPublisher(){

    }

    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo){
        if (filters.get(symbol) == null){
            filters.put(symbol, new ArrayList<CurrentMarketObserver>());
        }
        ArrayList<CurrentMarketObserver> lst = filters.get(symbol);
        lst.add(cmo);
    }

    public void unSubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo){
        if (filters.get(symbol) == null){
            return;
        }
        ArrayList<CurrentMarketObserver> lst = filters.get(symbol);
        lst.remove(cmo);
    }

    public void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide){
        if (filters.get(symbol) == null){
            return;
        }
        ArrayList<CurrentMarketObserver> lst = filters.get(symbol);

        for (CurrentMarketObserver o : lst){
            o.updateCurrentMarket(symbol, buySide, sellSide);
        }
    }
}
