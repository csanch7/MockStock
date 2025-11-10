package order;

import price.Price;

public class Quote {
    private String user;
    private String product;
    private QuoteSide buySide;
    private QuoteSide sellSide;

    public Quote (String symbol, Price buyPrice, int buyVolume, Price sellPrice,
                  int sellVolume, String userName) throws InvalidTradableException {
        setProduct(symbol);
        setUser(userName);
        this.buySide = new QuoteSide(userName, symbol, buyPrice, buyVolume, BookSide.BUY);
        this.sellSide = new QuoteSide(userName, symbol, sellPrice, sellVolume, BookSide.SELL);
    }

    private void setProduct(String product) throws InvalidTradableException {
        if (product == null || product.length() > 5 || product.isEmpty()) {
            throw new InvalidTradableException("invalid product");
        }
        for (int i = 0; i < product.length(); i++) {
            if (!Character.isLetter(product.charAt(i)) && product.charAt(i) != '.'){
                throw new InvalidTradableException("invalid product");
            }
        }
        this.product = product;
    }
    private void setUser(String user) throws InvalidTradableException {
        if (user == null || user.length() != 3) {
            throw new InvalidTradableException("invalid user");
        }
        for (int i = 0; i < user.length(); i++) {
            if (!Character.isLetter(user.charAt(i))){
                throw new InvalidTradableException("invalid user");
            }
        }
        this.user = user;
    }


    public QuoteSide getQuoteSide(BookSide sideIn) {
        if (sideIn == BookSide.SELL){
            return this.sellSide;
        }
        return this.buySide;
    }
    public String getSymbol(){
        return this.product;
    }
    public String getUser(){
        return this.user;
    }
}
