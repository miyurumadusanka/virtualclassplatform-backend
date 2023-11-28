package com.miyum.virtualclassplatform.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Utility {

    private static Utility instance;
    private final String uuid;
    private final SimpleDateFormat sdf;
    private final SimpleDateFormat sdfDate;

    private Utility() {
        uuid = UUID.randomUUID().toString();
        sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdfDate = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
    }

    public static synchronized Utility getInstance() {
        if (instance == null) {
            instance = new Utility();
        }
        return instance;
    }

    public String getUUID() {
        return uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDateTime(Date date) {
        return sdf.format(date);
    }

    public String getDate(Date date) {
        return sdfDate.format(date);
    }

}
