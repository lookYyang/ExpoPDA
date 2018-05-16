package com.orangehi.expo.modules;

import android.app.Application;

import org.xutils.x;

public class MainActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); //输出debug日志，开启会影响性能
    }
}
