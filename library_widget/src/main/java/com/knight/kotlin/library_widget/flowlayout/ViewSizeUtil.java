package com.knight.kotlin.library_widget.flowlayout;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author created by knight
 * @organize wananadroid
 * @Date 2021/7/2 14:49
 * @descript:
 */
public class ViewSizeUtil {

    public static Point getScreenSize(Context context) {
        WindowManager wmManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display;
        display = wmManager.getDefaultDisplay();
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     * @param dimen dp值
     * @return
     */
    public static int getCustomDimen(Context context, float dimen) {
        float density = ViewSizeUtil.getScreenSize(context).x / ViewSizeUtil.getDensity(context);
        return (int) ((ViewSizeUtil.getDensity(context) * density * dimen / 360f) + 0.5f);
    }

    public static float getDensity(Context context) {


        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.density;
//        WindowManager wmManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wmManager.getDefaultDisplay();
//        DisplayMetrics metric = new DisplayMetrics();
//        display.getMetrics(metric);
//        return metric.density;
    }
}
