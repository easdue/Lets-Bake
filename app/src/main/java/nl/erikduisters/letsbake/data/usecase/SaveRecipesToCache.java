package nl.erikduisters.letsbake.data.usecase;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import nl.erikduisters.letsbake.async.Cancellable;
import nl.erikduisters.letsbake.data.model.Recipe;

/**
 * Created by Erik Duisters on 28-03-2018.
 */
public class SaveRecipesToCache extends UseCase<SaveRecipesToCache.RequestInfo, Void> {

    public SaveRecipesToCache(@NonNull RequestInfo requestInfo, @NonNull Callback<Void> callback) {
        super(requestInfo, callback);
    }

    @Override
    public void execute(Cancellable cancellable) {
        try {
            Gson gson = requestInfo.gson;

            FileWriter writer = new FileWriter(requestInfo.recipeCacheFile, false);

            gson.toJson(requestInfo.recipeList, writer);
            writer.close();

            if (!cancellable.isCancelled()) {
                callback.onResult(null);
            }
        } catch (IOException e) {
            callback.onError(e);
        }
    }

    public static class RequestInfo {
        private final File recipeCacheFile;
        private final List<Recipe> recipeList;
        private final Gson gson;

        public RequestInfo(File recipeCacheFile, List<Recipe> recipeList, Gson gson) {
            this.recipeCacheFile = recipeCacheFile;
            this.recipeList = recipeList;
            this.gson = gson;
        }
    }
}
