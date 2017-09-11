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
    public static void saveInfo(Context context, String productOrderId, String sewingId, String siteId) {
        SharedPreferences sp = context.getSharedPreferences(ShareUtils.dbName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("productOrderId", productOrderId);
        editor.putString("sewingId", sewingId);
        editor.putString("siteId", siteId);
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

    public static String getProductOrderId(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("productOrderId", "D293-HR");
        return id;
    }

    public static String getSewingId(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("sewingId", "SJ000323");
        return id;
    }

    public static String getSiteId(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(ShareUtils.dbName, Context.MODE_PRIVATE);
        String id=preferences.getString("siteId", "70045");
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
