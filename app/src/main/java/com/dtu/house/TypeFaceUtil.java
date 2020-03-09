package com.dtu.house;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import com.google.firebase.firestore.FieldValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TypeFaceUtil {

    public static void overrideFont(Context context, String defaultFontName, String customFileNameInAsset) {

        final Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), customFileNameInAsset);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Map<String, Typeface> newMAp = new HashMap<String, Typeface>();
            newMAp.put("serif", customTypeface);

            try {
                final Field staticField = Typeface.class
                        .getDeclaredField("sSystemFontMap");
                staticField.setAccessible(true);
                staticField.set(null, newMAp);


            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            final Field defaultFontTypefaceField;
            try {
                defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontName);
                defaultFontTypefaceField.setAccessible(true);
                defaultFontTypefaceField.set(null, customTypeface);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }

    }

}
