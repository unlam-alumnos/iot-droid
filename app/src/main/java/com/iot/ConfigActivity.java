package com.iot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gmartin.alarmaiot_soa.R;
import com.google.gson.Gson;

public class ConfigActivity extends AppCompatActivity {
    private EditText tbMinRange;
    private EditText tbMaxRange;
    private Button btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        tbMinRange = (EditText) findViewById(R.id.tb_MinRange);
        tbMaxRange = (EditText) findViewById(R.id.tb_MaxRange);
        btnActualizar = (Button) findViewById(R.id.btn_upd_range);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String min = tbMinRange.getText().toString();
                String max = tbMaxRange.getText().toString();
                setConfigValuesInWS(min, max);
            }
        });

        getConfigValuesFromWS();

    }

    private void setConfigValuesInWS(String min, String max) {
        new NetworkTask().execute("http://192.168.1.72:8080/app/rest/temperature/setlimits?min=" + min + "&max=" + max);
    }

    private void getConfigValuesFromWS() {
        new NetworkTask(new Callback() {
            @Override
            public void run(String result) {
                TemperatureLimits temperatureLimits = new Gson().fromJson(result, TemperatureLimits.class);
                tbMinRange.setText(String.valueOf(temperatureLimits.getMin()));
                tbMaxRange.setText(String.valueOf(temperatureLimits.getMax()));
            }
        }).execute("http://192.168.1.72:8080/app/rest/temperature/limits");
    }
}
