package praktikum.stellar.model.ingredient;

import java.util.ArrayList;

public class IngredientsResponse {
    private ArrayList<Ingredient> data;

    public IngredientsResponse(ArrayList<Ingredient> data) {
        this.data = data;
    }

    public ArrayList<Ingredient> getData() {
        return data;
    }

    public void setData(ArrayList<Ingredient> data) {
        this.data = data;
    }
}
