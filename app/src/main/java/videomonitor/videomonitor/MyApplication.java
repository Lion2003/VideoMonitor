package videomonitor.videomonitor;

import android.app.Application;

/**
 * Created by Administrator on 2017-09-19.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
