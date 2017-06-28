package com.xiaofu.mysearch;

import android.content.Context;

/**
 * Created by XiaoFu on 2017-06-28 14:26.
 * 注释：
 */

public class Utils {
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
