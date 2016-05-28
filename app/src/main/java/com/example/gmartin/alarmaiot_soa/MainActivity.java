package com.example.gmartin.alarmaiot_soa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private Switch switchOnOff;
    private Button btnConfig;
    private Button btnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchOnOff = (Switch) findViewById(R.id.switch_on_off);
        btnConfig = (Button) findViewById(R.id.btn_config);
        btnStatus = (Button) findViewById(R.id.btn_status);

        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new NetworkTask().execute("http://192.168.1.72:8080/app/rest/alarm/" + (isChecked == true ? "on" : "off"));
            }
        });

        initNavigation();
        initAlarmStatusFromWS();
    }

    private void initAlarmStatusFromWS() {
        new NetworkTask(new Callback() {
            @Override
            public void run(String result) {
                switchOnOff.setChecked(Boolean.valueOf(result));
            }
        }).execute("http://192.168.1.72:8080/app/rest/alarm/status");
    }

    private void initNavigation() {
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
            startActivity(intent);
            }
        });

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, StatusActivity.class);
            startActivity(intent);
            }
        });
    }
}
