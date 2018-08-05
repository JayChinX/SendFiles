package com.example.administrator.sendfile;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/12.
 */

public class TextLongEnd {
    Context context;
    public static int TextLongSL(String text, TextView view){
        float textWith =view.getTextSize();
        int columnCount = (int) (view.getWidth() / textWith);

        return columnCount;
    }
    // Dp转Px
    private static float convertDpToPixel(Context context, float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // 计算每一个字符的宽度
    public static float getCharWidth(TextView textView, String text) {
        textView.setText(text.valueOf("."));
        textView.measure(0, 0);
        return textView.getMeasuredWidth();
    }
}
