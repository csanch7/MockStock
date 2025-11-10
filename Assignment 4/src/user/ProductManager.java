package user;

import order.InvalidTradableException;
import order.*;
import order.Tradable;
import price.InvalidPriceException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public final class ProductManager {
    private static ProductManager instance;
    private HashMap<String, ProductBook> collection = new HashMap<>();

    public static ProductManager getInstance(){
        if (instance == null){
            instance = new ProductManager();
        }
        return instance;
    }
    private ProductManager(){

    }
    public void addProduct(String symbol) throws DataValidationException {
        if (symbol == null || symbol.length() > 5 || symbol.isEmpty()) {
            throw new DataValidationException("invalid symbol");
        }
        for (int i = 0; i < symbol.length(); i++) {
            if (!Character.isLetter(symbol.charAt(i)) && symbol.charAt(i) != '.'){
                throw new DataValidationException("invalid symbol");
            }
        }
        ProductBook pb = new ProductBook(symbol);
        collection.put(symbol, pb);
    }

    public ProductBook getProductBook(String symbol) throws DataValidationException {
        if (collection.get(symbol)==null){
            throw new DataValidationException("invalid symbol to get from collection");
        }
        else {
            return collection.get(symbol);
        }
    }
    public String getRandomProduct() throws DataValidationException {
        if (collection.isEmpty()){
            throw new DataValidationException("cannot get random product from empty collection");
        }
        else {
            var keys = new ArrayList<>(collection.keySet());
            return keys.get(new Random().nextInt(keys.size()));
        }
    }
    public TradableDTO addTradable(Tradable o) throws InvalidPriceException, DataValidationException {
        if (o == null){
            throw new DataValidationException("cannot add null tradable");
        }
        TradableDTO dto = collection.get(o.getProduct()).add(o);
        UserManager.getInstance().updateTradable(o.getUser(), dto);
        return dto;
    }

    public TradableDTO[] addQuote(Quote q) throws InvalidPriceException, DataValidationException {
        if (q == null){
            throw new DataValidationException("cannot add null quote");
        }
        collection.get(q.getSymbol()).removeQuotesForUser(q.getUser());
        TradableDTO buyDTO = addTradable(q.getQuoteSide(BookSide.BUY));
        TradableDTO sellDTO = addTradable(q.getQuoteSide(BookSide.SELL));
        return new TradableDTO[] {buyDTO, sellDTO};
    }

    public TradableDTO cancel(TradableDTO o){
        if (o == null){
            throw new DataValidationException("cannot cancel null tradableDTO");
        }
        try {
            ProductBook pb = collection.get(o.product());
            TradableDTO dto = pb.cancel(o.side(), o.tradableId());
            return dto;
        }
        catch (Exception e){
            System.out.println("failed to cancel");
            return null;
        }
    }

    public TradableDTO[] cancelQuote(String symbol, String user) throws InvalidPriceException {
        if (symbol == null || user == null || collection.get(symbol) == null){
            throw new DataValidationException("cannot cancel quote with null symcol or user");
        }
        return collection.get(symbol).removeQuotesForUser(user);
    }

    @Override
    public String toString() {
        String output = "";
        for (ProductBook pb : collection.values()){
            output += pb.toString() + "\n";
        }
        return output;
    }
}
