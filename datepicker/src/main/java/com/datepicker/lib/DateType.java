package com.datepicker.lib;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DateType {

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String HOUR_12 = "hour_12";
    public static final String HOUR_24 = "hour_24";
    public static final String MINUTE = "minute";

    @StringDef({YEAR, MONTH, DAY, HOUR_12, HOUR_24, MINUTE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
    }
}
