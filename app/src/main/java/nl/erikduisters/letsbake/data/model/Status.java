package nl.erikduisters.letsbake.data.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@IntDef({Status.LOADING, Status.SUCCESS, Status.ERROR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Status {
    int LOADING = 0;
    int SUCCESS = 1;
    int ERROR = 2;
}
