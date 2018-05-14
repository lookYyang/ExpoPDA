package com.orangehi.expo.common;

import android.content.SharedPreferences;

/**
 * Created by yang on 2018/5/14.
 */

public final class Preferences {

    private static String sUSER_ID = "";// 说明没有初始化
    public static SharedPreferences sSHARED_REFERENCES = null;

    public Preferences() {
    }

    public static boolean isLogin() {
        return getAccountId().isEmpty() ? false : true;
    }

    /**
     * 获取登录用户的ID
     *
     * @return 如果已登录返回用户ID，否则返回0
     */
    public static String getAccountId() {
        if (sUSER_ID.isEmpty()) {
            sUSER_ID = sSHARED_REFERENCES.getString("user_id", "");
        }
        return sUSER_ID;
    }
}
