package nl.erikduisters.letsbake.data.remote;

import java.util.List;

import nl.erikduisters.letsbake.data.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public interface RecipeService {
    String BASE_URL = "http://go.udacity.com/";

    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipes();
}
