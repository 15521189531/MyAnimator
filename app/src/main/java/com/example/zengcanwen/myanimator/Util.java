package com.example.zengcanwen.myanimator;

import android.content.Context;

/**
 * Created by zengcanwen on 2018/1/8.
 */

public class Util {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
