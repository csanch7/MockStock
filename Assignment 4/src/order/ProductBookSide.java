package order;

import price.InvalidPriceException;
import price.Price;
import user.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class ProductBookSide {
    private BookSide side;
    private TreeMap<Price, ArrayList<Tradable>> bookEntries;

    public ProductBookSide(BookSide side){
        if (side == BookSide.BUY){
            this.bookEntries = new TreeMap<>(Collections.reverseOrder());
        }
        else {
            this.bookEntries = new TreeMap<>();
        }
        setSide(side);
    }

    private void setSide(BookSide side) {
        this.side = side;
    }

    public TradableDTO add(Tradable o){
        if (bookEntries.get(o.getPrice()) == null){
            bookEntries.put(o.getPrice(), new ArrayList<Tradable>());
        }
        bookEntries.get(o.getPrice()).add(o);
        UserManager.getInstance().updateTradable(o.getUser(), o.makeTradableDTO());
        return new TradableDTO(o);
    }

    public TradableDTO cancel(String tradableId){
        for (Price p: bookEntries.keySet()){
            ArrayList<Tradable> lst = bookEntries.get(p);
            for (Tradable t : lst){
                if (tradableId.equals(t.getId())){
                    System.out.println("**CANCEL: " + t);
                    lst.remove(t);
                    t.setCancelledVolume(t.getRemainingVolume() + t.getCancelledVolume());
                    t.setRemainingVolume(0);
                    if (lst.isEmpty()){
                        bookEntries.remove(p);
                    }
                    UserManager.getInstance().updateTradable(t.getUser(), t.makeTradableDTO());
                    return new TradableDTO(t);
                }
            }
        }
        return null;
    }

    public TradableDTO removeQuotesForUser(String userName){
        for (Price p: bookEntries.keySet()){
            ArrayList<Tradable> lst = bookEntries.get(p);
            for (Tradable t : lst){
                if (userName.equals(t.getUser())){
                    TradableDTO dto = cancel(t.getId());
                    if (lst.isEmpty()){
                        bookEntries.remove(p);
                    }
                    UserManager.getInstance().updateTradable(t.getUser(), dto);
                    return dto;
                }
            }
        }
        return null;
    }

    public Price topOfBookPrice(){
        if (bookEntries.isEmpty()){
            return null;
        }

        return bookEntries.firstKey();
    }

    public int topOfBookVolume(){
        if (bookEntries.isEmpty()) {
            return 0;
        }
        int total = 0;
        if (side == BookSide.BUY){
            Price highestPrice = bookEntries.firstKey();
            ArrayList<Tradable> lst = bookEntries.get(highestPrice);
            for (Tradable t: lst){
                total += t.getRemainingVolume();
            }
        }
        if (side == BookSide.SELL){
            Price lowestPrice = bookEntries.firstKey();
            ArrayList<Tradable> lst = bookEntries.get(lowestPrice);
            for (Tradable t: lst){
                total += t.getRemainingVolume();
            }
        }
        return total;
    }

    public String toString(){
        String output = "Side: " + this.side + "\n";
        if (bookEntries.isEmpty()){
            output += "\t\t<Empty>";
            return output;
        }

            for (Price p : bookEntries.keySet()){
                output += "\t Price: " + p + "\n";
                for (Tradable t : bookEntries.get(p)){
                    output += "\t\t" + t.toString() + "\n";
                }
            }

        return output;
    }

    public void tradeOut(Price price, int volToTrade) throws InvalidPriceException {
        Price topOfBook = topOfBookPrice();
        if (topOfBook == null){
            return;
        }
        if (side == BookSide.BUY && topOfBook.lessThan(price)) {
            return;
        }
        if (side == BookSide.SELL && topOfBook.greaterThan(price)) {
            return;
        }
        ArrayList<Tradable> atPrice = bookEntries.get(topOfBook);
        int totalVolAtPrice = topOfBookVolume();
        if (volToTrade >= totalVolAtPrice){
            for (Tradable t : atPrice){
                int rv = t.getRemainingVolume();
                t.setFilledVolume(t.getOriginalVolume());
                t.setRemainingVolume(0);
                System.out.println("\tFULL FILL: (" + t.getSide() + " " + t.getFilledVolume() + ") " + t);
                UserManager.getInstance().updateTradable(t.getUser(), t.makeTradableDTO());
            }
            bookEntries.remove(topOfBook);
        }
        else {
            int remainder = volToTrade;
            for (Tradable t : atPrice){
                double ratio = (double) t.getRemainingVolume() / totalVolAtPrice;
                int toTrade = (int) Math.ceil(volToTrade * ratio);
                toTrade = Math.min(toTrade, remainder);
                t.setFilledVolume(t.getFilledVolume()+toTrade);
                t.setRemainingVolume(t.getRemainingVolume()-toTrade);
                System.out.println("\tPARTIAL FILL: (" + t.getSide() + " " + t.getFilledVolume() + ") " + t);
                remainder -= toTrade;
                UserManager.getInstance().updateTradable(t.getUser(), t.makeTradableDTO());
            }
        }
    }
}

