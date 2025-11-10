package price;

import java.util.HashMap;

public abstract class PriceFactory {

    private static HashMap<Integer, Price> flyweight = new HashMap<>();

    public static Price makePrice(int centsValue)  {
        if (flyweight.get(centsValue) != null) {
            return flyweight.get(centsValue);
        } else {
            Price newPrice = new Price(centsValue);
            flyweight.put(centsValue, newPrice);
            return newPrice;
        }
    }
    public static Price makePrice(String stringValueIn) throws InvalidPriceException {
        if (stringValueIn.isEmpty()) {
            throw new InvalidPriceException("Invalid empty string");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringValueIn.length(); i++) {
            char character = stringValueIn.charAt(i);

            if (!(character == '$') && !(character == ',')) {
                sb.append(stringValueIn.charAt(i));
            }
            if (Character.isLetter(character)) {
                throw new InvalidPriceException("Cannot have letter in cents value");
            }
        }
        String[] parts = sb.toString().split("\\.");
        if ((parts.length > 1) && (parts[1].length() > 2 || parts[1].length() == 1)) {
            throw new InvalidPriceException("Price can have at most 2 decimal places: " + stringValueIn);
        }
        double cents;
        try {
            double value = Double.parseDouble(sb.toString());
            cents = Math.round(value * 100);

        } catch (Exception e) {
            throw new InvalidPriceException("Invalid number format: " + stringValueIn);
        }

        int finalValue = (int) cents;
        if (flyweight.get(finalValue) != null){
            return flyweight.get(finalValue);
        }
        else {
            flyweight.put(finalValue, new Price(finalValue));
            return flyweight.get(finalValue);
        }

    }
}
