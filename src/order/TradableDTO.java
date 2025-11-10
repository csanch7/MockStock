package order;

import price.Price;

public record TradableDTO(String user, String product, Price price, int originalVolume, int remainingVolume, int cancelledVolume, int
filledVolume, BookSide side, String tradableId
) {
    public TradableDTO(Tradable order) {
        this(order.getUser(), order.getProduct(), order.getPrice(), order.getOriginalVolume(),
                order.getRemainingVolume(), order.getCancelledVolume(), order.getFilledVolume(),
                order.getSide(), order.getId());
    }

    @Override
    public String toString() {
        return "Product: " + product +
                ", Price: " + price +
                ", OriginalVolume: " + originalVolume +
                ", RemainingVolume: " + remainingVolume +
                ", CancelledVolume: " + cancelledVolume +
                ", FilledVolume: " + filledVolume +
                ", User: " + user +
                ", Side: " + side +
                ", Id: " + tradableId;
    }
}
