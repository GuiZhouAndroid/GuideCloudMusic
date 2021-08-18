package com.zs.itking.guidecloudmusic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * created by on 2021/8/19
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-19-0:32
 */
public class MyApplication extends Application {

    public static Activity AppContext =null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
