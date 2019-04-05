package loglig.context_provider;

import android.app.Application;
import android.content.Context;

/**
 * Created by is_uptown4 on 26/09/16.
 */
public class ApplicationContextProvider extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getContext() {
        return appContext;
    }
}
