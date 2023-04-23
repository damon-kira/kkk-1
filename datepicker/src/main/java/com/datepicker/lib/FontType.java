package com.datepicker.lib;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

public class FontType {
    public static final String LARGE = "large";
    public static final String MEDIUM = "medium";
    public static final String NORMAL = "normal";
    public static final String SMALL = "small";

    @StringDef({LARGE, MEDIUM, NORMAL, SMALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
}
