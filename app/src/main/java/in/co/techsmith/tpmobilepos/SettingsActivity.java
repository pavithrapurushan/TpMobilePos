package in.co.techsmith.tpmobilepos;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

//Created by Pavithra on 31-07-2020

public class SettingsActivity extends AppCompatActivity {

    TextView tvDeviceIdValue;
    TextView tvScreenResolnValue;
    TextView tvVersionValue;
    TextView tvUrlValue;
    TsCommonMethods tsCommonMethods;
    String deviceId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tsCommonMethods = new TsCommonMethods(this);
        deviceId = tsCommonMethods.GetDeviceUniqueId();
        tvDeviceIdValue = (TextView) findViewById(R.id.tvDeviceIdValue);
        tvScreenResolnValue = (TextView) findViewById(R.id.tvScreenResolnValue);
        tvVersionValue = (TextView) findViewById(R.id.tvVersionValue);
        tvUrlValue = (TextView) findViewById(R.id.tvUrlValue);

        //Added by Pavithra on 18-08-2020
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersionValue.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvDeviceIdValue.setText(deviceId);

        String temp =  getScreenResolution(SettingsActivity.this);
        tvScreenResolnValue.setText(temp);
        tvUrlValue.setText(""+AppConfig.app_url);


        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                Toast.makeText(this, "values-small", Toast.LENGTH_SHORT).show();
                // values-small directory
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                Toast.makeText(this, "values-normal", Toast.LENGTH_SHORT).show();
                // values-normal directory
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                Toast.makeText(this, "values-large", Toast.LENGTH_SHORT).show();
                // values-large directory
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                Toast.makeText(this, "values-xlarge", Toast.LENGTH_SHORT).show();
                // values-xlarge directory
                break;
        }
    }



    private static String getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return "{" + width + "," + height + "}";
    }
}
