package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 30.10.15.
 */

public final class MDTools {

    public static int evaluateColor(float fraction, int startValue, int endValue) {

        if (fraction <= 0) return startValue;
        if (fraction >= 1) return endValue;
        if (startValue == endValue) return startValue;

        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24)
                | ((startR + (int) (fraction * (endR - startR))) << 16)
                | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }


    /**
     * According to the resolution of the mobile phone unit from dp turn became px (pixels)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * According to the resolution of the phone from px (pixel) units turn become dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
