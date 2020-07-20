package in.co.techsmith.tpmobilepos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


//Added by 1165 on 19-03-2020

public class TsCommonMethods {

    Context CurrentContext;

    public TsCommonMethods(Context cntxt) {
        CurrentContext = cntxt;
    }

    public boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) CurrentContext.getSystemService(CurrentContext.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

}
