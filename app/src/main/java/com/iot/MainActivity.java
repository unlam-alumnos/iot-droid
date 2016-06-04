package com.iot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.gmartin.alarmaiot_soa.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private boolean status;
    private Switch switchOnOff;
    private Button btnConfig;
    private TextView textTemperature;
    private Timer timer;
    private boolean alreadyVibratedMedium = false;
    private boolean alreadyVibratedHigh = false;

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
                        if (!isChecked) {
                            RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_main);
                            rl.setBackgroundColor(Color.WHITE);
                        }
                    }
                }).execute("http://192.168.1.72:8080/app/rest/alarm/" + (isChecked == true ? "on" : "off"));
            }
        });

        initNavigation();
        initAlarmStatusFromWS();

        TimerTask timerTask = new TimerTask() {
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
        if (!alarmOn) {
            textTemperature.setText("-- º C");
        } else {
            getTemperatureValueFromWS();
        }
    }

    private void getTemperatureValueFromWS() {
        new NetworkTask(new Callback() {
            @Override
            public void run(String result) {
                final Double temperature = Double.valueOf(result.toString());
                NumberFormat formatter = new DecimalFormat("#0.00");
                textTemperature.setText(formatter.format(temperature) + "º C");

                new NetworkTask(new Callback() {
                    @Override
                    public void run(String result) {
                        TemperatureLimits temperatureLimits = new Gson().fromJson(result,
                                TemperatureLimits.class);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_main);
                        if (temperature < temperatureLimits.getMin()) {
                            rl.setBackgroundColor(Color.rgb(0, 128, 255));
                            if (!alreadyVibratedMedium) {
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(500);
                                alreadyVibratedMedium = true;
                            }
                        } else if (temperature >= temperatureLimits.getMin()
                                && temperature <= temperatureLimits.getMax()) {
                            rl.setBackgroundColor(Color.rgb(127, 255, 0));
                            alreadyVibratedMedium = false;
                            alreadyVibratedHigh = false;
                        } else {
                            rl.setBackgroundColor(Color.rgb(250, 128, 114));
                            if (!alreadyVibratedHigh) {
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(1500);
                                alreadyVibratedHigh = true;
                            }
                        }
                    }
                }).execute("http://192.168.1.72:8080/app/rest/temperature/limits");
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
