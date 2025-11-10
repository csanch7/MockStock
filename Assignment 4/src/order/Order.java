package order;

import price.Price;

public class Order implements Tradable {
    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;
    private String id;

    public Order (String user, String product, Price price, int originalVolume, BookSide side) throws InvalidTradableException {
        setUser(user);
        setProduct(product);
        setPrice(price);
        setSide(side);
        setOriginalVolume(originalVolume);
        this.remainingVolume = originalVolume;
        this.cancelledVolume = 0;
        this.filledVolume = 0;
        this.id = user + product + price + System.nanoTime();
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

    private void setPrice(Price price) throws InvalidTradableException {
        if (price == null){
            throw new InvalidTradableException("invalid price");
        }
        this.price = price;
    }

    private void setSide(BookSide side) throws InvalidTradableException {
        if (side == null){
            throw new InvalidTradableException("invalid side");
        }
        this.side = side;
    }

    private void setOriginalVolume(int originalVolume) throws InvalidTradableException {
        if (originalVolume <= 0 || originalVolume > 10000){
            throw new InvalidTradableException("invalid original value");
        }
        this.originalVolume = originalVolume;
    }

    // can be changed
    // apparently this can be changed to private
    @Override
    public void setRemainingVolume(int remainingVolume) {
        this.remainingVolume = remainingVolume;
    }

    @Override
    public void setCancelledVolume(int cancelledVolume) {
        this.cancelledVolume = cancelledVolume;
    }

    @Override
    public void setFilledVolume(int filledVolume) {
        this.filledVolume = filledVolume;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public Price getPrice() {
        return price;
    }

    @Override
    public BookSide getSide() {
        return side;
    }

    @Override
    public int getOriginalVolume() {
        return originalVolume;
    }

    @Override
    public int getRemainingVolume() {
        return remainingVolume;
    }

    @Override
    public int getCancelledVolume() {
        return cancelledVolume;
    }

    @Override
    public int getFilledVolume() {
        return filledVolume;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                user + ' ' + side + " order: " + product + " at " + price + ", Original Vol:"
                + originalVolume + ", Rem Vol: " + remainingVolume + ", Fill Vol: " + filledVolume + ", CXL Vol: "
                + cancelledVolume + ", ID: " + id;
    }

    @Override
    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this);
    }
}
