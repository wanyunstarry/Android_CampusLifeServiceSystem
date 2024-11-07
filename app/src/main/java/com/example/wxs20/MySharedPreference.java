package com.example.wxs20;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class MySharedPreference {
    private static SharedPreferences preContext = null;

    private static SharedPreferences getPreference(Context cx) {
        return PreferenceManager.getDefaultSharedPreferences(cx);
    }
    //注册
    public static void register(Context cx, String username, String password) {
        setValue(cx, username, password);
    }
    //设置用户ID
    public static void saveNames(Context cx, String username, String password) {
        setValue(cx, username, password);
    }

    //根据关键词key映射得到相应的字符串
    public static String getStringValue(Context cx, String key) {
        if (preContext == null) {
            preContext = getPreference(cx);
        }

        return preContext.getString(key, "error");
    }

    //根据关键词key映射得到“记住我”标志位状态
    public static int getFlag(Context cx, String key) {
        if (preContext == null) {
            preContext = getPreference(cx);
        }

        return preContext.getInt(key, 0);
    }

    //设置键值对<key,val>
    private static void setValue(Context cx, String key, String val) {
        if (preContext == null) {
            preContext = getPreference(cx);
        }
        Editor ed = preContext.edit();
        ed.putString(key, val);
        boolean ret = ed.commit();
    }

    //设置键值对<key,val>
    private static void setValue(Context cx, String key, int val) {
        if (preContext == null) {
            preContext = getPreference(cx);
        }
        Editor ed = preContext.edit();
        ed.putInt(key, val);
        boolean ret = ed.commit();
    }

    //保存人数
    public static void saveCount(Context cx, String key, int count) {
        setValue(cx, key, count);
    }

    //保存“记住我”标注位状态
    public static void saveRemember(Context cx,int i) {
        setValue(cx,"flag",i);
    }

    //根据关键词key映射得到相应的整型值
    public static int getIntValue(Context cx, String key) {
        if (preContext == null) {
            preContext = getPreference(cx);
        }

        return preContext.getInt(key, 0);
    }

    public static void setLossState(Context cx,int i){
        setValue(cx,"state",i);
    }
}
