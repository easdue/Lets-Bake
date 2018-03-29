package nl.erikduisters.letsbake.data.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Erik Duisters on 24-03-2018.
 */
@StringDef({Measure.CUP, Measure.GRAM, Measure.KILOGRAM, Measure.OUNCE, Measure.TABLESPOON, Measure.TEASPOON, Measure.UNIT})
@Retention(RetentionPolicy.RUNTIME)
public @interface Measure {
    String CUP = "CUP";
    String GRAM = "G";
    String KILOGRAM = "K";
    String OUNCE = "OZ";
    String TABLESPOON = "TBLSP";
    String TEASPOON = "TSP";
    String UNIT = "UNIT";
}
