package price;

import java.util.Objects;

public class Price implements Comparable<Price>{
    private final int x;
    public Price(int x) {

        this.x = x;
    }
    public boolean isNegative(){
        return x < 0;
    }
    public Price add(Price p) throws InvalidPriceException{
        if (p == null) throw new InvalidPriceException("Cannot add to null");
        else {
            return new Price(x + p.x);
        }
    }
    public Price subtract(Price p) throws InvalidPriceException{
        if (p == null) throw new InvalidPriceException("Cannot subtract to null");
        else {
            return new Price(x - p.x);
        }
    }
    public Price multiply(int n) {
        return new Price(x * n);
    }
    public boolean greaterOrEqual(Price p) throws InvalidPriceException{
        if (p == null) throw new InvalidPriceException("Null is a uncomparable value");
        else {
            return x >= p.x;
        }
    }
    public boolean lessOrEqual(Price p) throws InvalidPriceException{
        if (p == null) throw new InvalidPriceException("Null is a uncomparable value");
        else {
            return x <= p.x;
        }
    }
    public boolean greaterThan(Price p) throws InvalidPriceException{
        if (p == null) throw new InvalidPriceException("Null is a uncomparable value");
        else {
            return x > p.x;
        }
    }
    public boolean lessThan(Price p) throws InvalidPriceException{
        if (p == null) throw new InvalidPriceException("Null is a uncomparable value");
        else {
            return x < p.x;
        }
    }
    @Override
    public int compareTo(Price o) {
        if (o == null){
            return -1;
        }
        if (x < o.x){
            return -1;
        }
        else if (x > o.x){
            return 1;
        }
        else {
            return 0;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return x == price.x;
    }

    @Override
    public String toString() {
        return String.format("$%,.2f",x*0.01);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x);
    }
}
