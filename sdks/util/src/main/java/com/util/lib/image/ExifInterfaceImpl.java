package com.util.lib.image;

import android.media.ExifInterface;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by sunsg on 15/10/17.
 */
public class ExifInterfaceImpl extends ExifInterface {

    private HashMap<String, String> mAttributes;

    public ExifInterfaceImpl(String filename) throws IOException {
        super(filename);

        mAttributes = new HashMap<String, String>();

        Field[] fields = ExifInterface.class.getDeclaredFields();

        Field field = null;
        String key = null;
        String value = null;
        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            if (String.class.isAssignableFrom(field.getType()) && field.getName().startsWith("TAG_")) {
                try {
                    key = field.get(field.getName()).toString();
                    value = getAttribute(field.get(field.getName()).toString());
                    mAttributes.put(key, value);
                } catch (IllegalAccessException | IllegalArgumentException e) {
//                    Logger.e(e.toString());
                }
            }
        }

    }

    public Set<String> keySet() {
        return new TreeSet<>(mAttributes.keySet());
    }

    public boolean hasAttribute(final String key) {
        return mAttributes.containsKey(key);
    }

}
