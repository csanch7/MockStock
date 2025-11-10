package order;

import price.InvalidPriceException;
import price.Price;
import currentMarket.*;

public class ProductBook {
    private String product;
    private ProductBookSide buySide;
    private ProductBookSide sellSide;

    public ProductBook (String product) throws InvalidTradableException {
        setProduct(product);
        this.buySide = new ProductBookSide(BookSide.BUY);
        this.sellSide = new ProductBookSide(BookSide.SELL);
    }

    private void setProduct(String product) throws InvalidTradableException {
        if (product == null || product.length() > 5 || product.isEmpty()) {
            throw new InvalidTradableException("Invalid ProductBook product");
        }
        for (int i = 0; i < product.length(); i++) {
            if (!Character.isLetter(product.charAt(i)) && product.charAt(i) != '.'){
                throw new InvalidTradableException("Invalid ProductBook product");
            }
        }
        this.product = product;
    }


    public TradableDTO add(Tradable t) throws InvalidTradableException, InvalidPriceException {
        if (t == null) {
            throw new InvalidTradableException("Invalid Tradable object");
        }
        System.out.println("**ADD: " + t);
        if (t.getSide() == BookSide.BUY) {
            TradableDTO dto = buySide.add(t);
            tryTrade();
            updateMarket();
            return dto;
        }
        if (t.getSide() == BookSide.SELL) {
            TradableDTO dto = sellSide.add(t);
            tryTrade();
            updateMarket();
            return dto;
        }
        return null;
    }

    public TradableDTO[] add(Quote qte) throws InvalidTradableException, InvalidPriceException {
        if (qte == null) {
            throw new InvalidTradableException("Invalid Quote object");
        }

        System.out.println("**ADD: " + qte.getQuoteSide(BookSide.BUY).toString());
        System.out.println("**ADD: " + qte.getQuoteSide(BookSide.SELL).toString());
        removeQuotesForUser(qte.getUser());
        TradableDTO buyDTO = buySide.add(qte.getQuoteSide(BookSide.BUY));
        TradableDTO sellDTO = sellSide.add(qte.getQuoteSide(BookSide.SELL));
        updateMarket();
        return new TradableDTO[]{buyDTO, sellDTO};
    }
    public TradableDTO cancel(BookSide side, String orderId) throws InvalidPriceException {
        if (side == BookSide.BUY) {
            TradableDTO dto = buySide.cancel(orderId);
            updateMarket();
            return dto;
        }
        if (side == BookSide.SELL) {
            TradableDTO dto = sellSide.cancel(orderId);
            updateMarket();
            return dto;
        }
        return null;
    }

    public TradableDTO[] removeQuotesForUser(String userName) throws InvalidPriceException {
        TradableDTO buyDTO = buySide.removeQuotesForUser(userName);
        TradableDTO sellDTO = sellSide.removeQuotesForUser(userName);
        updateMarket();
        return new TradableDTO[]{buyDTO, sellDTO};
    }

    public void tryTrade() throws InvalidPriceException {
        Price topBuyPrice = buySide.topOfBookPrice();
        Price topSellPrice = sellSide.topOfBookPrice();
        if (topBuyPrice == null || topSellPrice == null){
            return;
        }
        int totalToTrade = Math.max(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
        while (totalToTrade > 0){
            Price buyPrice = buySide.topOfBookPrice();
            Price sellPrice = sellSide.topOfBookPrice();
            if (buyPrice == null || sellPrice == null || sellPrice.greaterThan(buyPrice)){
                return;
            }
            int toTrade = Math.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume());
            buySide.tradeOut(buyPrice, toTrade);
            sellSide.tradeOut(buyPrice, toTrade);
            totalToTrade -= toTrade;

        }
    }

    public String getTopOfBookString(BookSide side){
        String topBuy = (buySide.topOfBookPrice() == null)
                ? "$0.00"
                : buySide.topOfBookPrice().toString();

        String topSell = (sellSide.topOfBookPrice() == null)
                ? "$0.00"
                : sellSide.topOfBookPrice().toString();

        return switch (side) {
            case BUY -> "Top of BUY book: " + topBuy + " X " + buySide.topOfBookVolume();
            case SELL -> "Top of SELL book: " + topSell + " X " + sellSide.topOfBookVolume();
            default -> null;
        };
    }

    @Override
    public String toString(){
        return "--------------------------------------------\n" + "Product Book: " + product + " \n" + buySide.toString() + " \n" + sellSide.toString() + "\n--------------------------------------------";
    }

    private void updateMarket() throws InvalidPriceException {
        int buyVolume = buySide.topOfBookVolume();
        int sellVolume = sellSide.topOfBookVolume();
        Price buyPrice = buySide.topOfBookPrice();
        Price sellPrice = sellSide.topOfBookPrice();
        CurrentMarketTracker.getInstance().updateMarket(product, buyPrice, buyVolume, sellPrice, sellVolume);
    }
}

