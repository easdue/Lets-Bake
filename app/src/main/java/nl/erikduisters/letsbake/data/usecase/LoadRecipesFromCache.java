package nl.erikduisters.letsbake.data.usecase;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nl.erikduisters.letsbake.async.Cancellable;
import nl.erikduisters.letsbake.data.model.Recipe;

/**
 * Created by Erik Duisters on 28-03-2018.
 */
public class LoadRecipesFromCache extends UseCase<LoadRecipesFromCache.RequestInfo, List<Recipe>> {
    public LoadRecipesFromCache(@NonNull RequestInfo requestInfo, @NonNull Callback<List<Recipe>> callback) {
        super(requestInfo, callback);
    }

    @Override
    public void execute(Cancellable cancellable) {
        File cacheFile = requestInfo.cacheFile;

        if (cacheFile.exists() && cacheFile.canRead() && cacheFile.isFile()) {
            try {
                Gson gson = requestInfo.gson;

                JsonReader reader = gson.newJsonReader(new FileReader(requestInfo.cacheFile));
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                List<Recipe> recipes = gson.fromJson(reader, listType);

                reader.close();

                if (!cancellable.isCancelled()) {
                    callback.onResult(recipes);
                }
            } catch (FileNotFoundException e) {
                callback.onError(e);
            } catch (JsonIOException e) {
                callback.onError(e);
            } catch (JsonSyntaxException e) {
                callback.onError(e);
            } catch (IOException e) {
                callback.onError(e);
            }
        } else {
            callback.onResult(new ArrayList<>());
        }
    }

    public static class RequestInfo {
        private final File cacheFile;
        private final Gson gson;

        public RequestInfo(File cacheFile, Gson gson) {
            this.cacheFile = cacheFile;
            this.gson = gson;
        }
    }
}
