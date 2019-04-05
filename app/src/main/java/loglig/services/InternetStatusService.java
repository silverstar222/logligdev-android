package loglig.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by is_uptown4 on 11/05/16.
 */
public class InternetStatusService {

    public static boolean isOnline(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager;
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())
                isConnected = true;
            else isConnected = false;
            return isConnected;
        } catch (Exception e) {
            System.out.println("Internet Connection Exception: " + e.getMessage());
            isConnected = false;
        }
        return isConnected;
    }
}