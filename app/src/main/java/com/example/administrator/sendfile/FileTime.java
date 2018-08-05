package com.example.administrator.sendfile;

import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/12/9.
 */

public class FileTime {
    public static String getStandardTime(File filePath) {
        long timestamp = filePath.lastModified();

        Log.i("time 2", "getStandardTime: " + timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //时区不是北京时
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(timestamp);
        Log.i("time 1", "getStandardTime: " + date);
        sdf.format(date);
        Log.i("time", "getStandardTime: " + sdf.format(date));
        return sdf.format(date);
    }
}
