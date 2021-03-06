package com.zx.freetime.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zx.freetime.engine.changeskin.control.SkinConfig;


/**
 * Created by zhy on 15/9/22.
 */
public class PrefUtils {
    private Context mContext;

    public PrefUtils(Context context) {
        this.mContext = context;
    }

    //获取插件路径;
    public String getPluginPath() {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(SkinConfig.KEY_PLUGIN_PATH, "");
    }

    //获取后缀
    public String getSuffix() {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(SkinConfig.KEY_PLUGIN_SUFFIX, "");
    }

    //清除内容;
    public boolean clear() {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        return sp.edit().clear().commit();
    }


    public void putPluginPath(String path) {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SkinConfig.KEY_PLUGIN_PATH, path).apply();
    }

    public void putPluginPkg(String pkgName) {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SkinConfig.KEY_PLUGIN_PKG, pkgName).apply();
    }

    public String getPluginPkgName() {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(SkinConfig.KEY_PLUGIN_PKG, "");
    }

    public void putPluginSuffix(String suffix) {
        SharedPreferences sp = mContext.getSharedPreferences(SkinConfig.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(SkinConfig.KEY_PLUGIN_SUFFIX, suffix).apply();
    }
}
