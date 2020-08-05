package in.co.techsmith.tpmobilepos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

//Created by Pavithra on 31-07-2020

public class SettingsActivity extends AppCompatActivity {

    TextView tvDeviceIdValue;
    TsCommonMethods tsCommonMethods;
    String deviceId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tsCommonMethods = new TsCommonMethods(this);
        deviceId = tsCommonMethods.GetDeviceUniqueId();
        tvDeviceIdValue = (TextView) findViewById(R.id.tvDeviceIdValue);
        tvDeviceIdValue.setText(deviceId);
    }
}
