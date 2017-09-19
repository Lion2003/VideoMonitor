package videomonitor.videomonitor.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017-09-08.
 */

public class ShareUtils {
    private static String dbName = "sewing"; //缝纫机

    /**
     * 保存缝纫机ID，站点ID
     * @param context
     * @param productOrderId 生产单ID
     * @param sewingId 缝纫机ID
     * @param siteId 站点ID
     */
    public static void saveInfo(Context context, int machineType, String productOrderId,
                                String color, String size, String sewingId, String siteId) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("type", machineType);
        editor.putString("productOrderId", productOrderId);
        editor.putString("color", color);
        editor.putString("size", size);
        editor.putString("sewingId", sewingId);
        editor.putString("siteId", siteId);
        editor.commit();
    }

    public static void setMachineType(Context context, int machineType) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("type", machineType);
        editor.commit();
    }

    public static void setProductOrderId(Context context, String productOrderId) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("productOrderId", productOrderId);
        editor.commit();
    }

    public static void setSewingId(Context context, String sewingId) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sewingId", sewingId);
        editor.commit();
    }

    public static void setSiteId(Context context, String siteId) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("siteId", siteId);
        editor.commit();
    }

    public static void setColor(Context context, String color) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("color", color);
        editor.commit();
    }

    public static void setSize(Context context, String size) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("size", size);
        editor.commit();
    }

    public static void setUrl(Context context, String url) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("url", url);
        editor.commit();
    }

    public static String getProductOrderId(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("productOrderId", "D293-HR");
        return id;
    }

    public static String getSewingId(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("sewingId", "cisma001");
        return id;
    }

    public static String getSiteId(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("siteId", "13");
        return id;
    }

    public static int getMachineType(Context context) {
        int id = 0;
        try{
            SharedPreferences preferences = context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
            id = preferences.getInt("type", 1);
        }catch (Exception e) {
            id = 1;
        }
        return id;
    }

    public static String getColor(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("color", "黄色");
        return id;
    }

    public static String getSize(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("size", "S");
        return id;
    }

    public static String getUrl(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("url", "http://112.124.63.137:8016");
        return id;
    }

    public static void clear(Context context) {
        if (null == context) {
            return;
        }

        SharedPreferences preferences = context.getSharedPreferences(ShareUtils.dbName, Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
