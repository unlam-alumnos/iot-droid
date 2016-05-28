package com.example.gmartin.alarmaiot_soa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private boolean status;
    private Switch switchOnOff;
    private Button btnConfig;
    private TextView textTemperature;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = new Timer();

        btnConfig = (Button) findViewById(R.id.btn_config);
        textTemperature = (TextView) findViewById(R.id.text_temperature);
        switchOnOff = (Switch) findViewById(R.id.switch_on_off);
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                new NetworkTask(new Callback() {
                    @Override
                    public void run(String result) {
                        status = isChecked;
                        initTemperature(isChecked);
                    }
                }).execute("http://192.168.1.72:8080/app/rest/alarm/" + (isChecked == true ? "on" : "off"));
            }
        });

        initNavigation();
        initAlarmStatusFromWS();


        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                if (status) {
                    getTemperatureValueFromWS();
                }
            }
        };
        timer.schedule(timerTask, 0, 1500);
    }

    private void initTemperature(boolean alarmOn) {
        if(!alarmOn){
            textTemperature.setText("-- º C");
        } else {
            getTemperatureValueFromWS();
        }
    }

    private void getTemperatureValueFromWS() {
        new NetworkTask(new Callback() {
            @Override
            public void run(String result) {
                Double temperature = Double.valueOf(result.toString());
                textTemperature.setText(Math.round(temperature*100)/100 + "º C");
            }
        }).execute("http://192.168.1.72:8080/app/rest/temperature/read");
    }

    private void initAlarmStatusFromWS() {
        new NetworkTask(new Callback() {
            @Override
            public void run(String result) {
                status = Boolean.valueOf(result);
                switchOnOff.setChecked(status);
                initTemperature(status);
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
    }
}
