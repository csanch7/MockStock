package currentMarket;
import price.*;

public class CurrentMarketSide {

    private Price price;
    private int volume;

    public CurrentMarketSide (Price price, int volume){
        setPrice(price);
        setVolume(volume);
    }

    private void setVolume(int volume) {
        this.volume = volume;
    }

    private void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return price + "x" + volume;
    }
}
