package nl.erikduisters.letsbake.data.model;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class Ingredient {
    private float quantity;
    private @Measure String measure;
    private String ingredient;

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
