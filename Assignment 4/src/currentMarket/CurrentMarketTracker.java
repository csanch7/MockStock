package currentMarket;

import price.*;
import user.*;

public class CurrentMarketTracker {

    private static CurrentMarketTracker instance;

    public static CurrentMarketTracker getInstance(){
        if (instance == null){
            instance = new CurrentMarketTracker();
        }
        return instance;
    }

    private CurrentMarketTracker(){

    }

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws InvalidPriceException {
        Price width;
        if (buyPrice == null || sellPrice == null) {
            width = PriceFactory.makePrice(0);
        } else {
            width = sellPrice.subtract(buyPrice);
        }
        buyPrice = (buyPrice == null) ? PriceFactory.makePrice(0) : buyPrice;
        sellPrice = (sellPrice == null) ? PriceFactory.makePrice(0) : sellPrice;
        CurrentMarketSide buySide = new CurrentMarketSide(buyPrice, buyVolume);
        CurrentMarketSide sellSide = new CurrentMarketSide(sellPrice, sellVolume);
        System.out.println("*********** Current Market ***********\n");
        System.out.println("* " + symbol + " " + buyPrice + "x" + buyVolume + " - " + sellPrice + "x" + sellVolume + " [" + width.toString() + "]");
        System.out.println("**************************************\n");
        CurrentMarketPublisher.getInstance().acceptCurrentMarket(symbol, buySide, sellSide);
    }
}
