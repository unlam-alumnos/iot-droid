package com.iot.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gmartin.alarmaiot_soa.R;
import com.google.gson.Gson;
import com.iot.common.UrlBuilder;
import com.iot.rest.Callback;
import com.iot.rest.NetworkTask;
import com.iot.dto.TemperatureLimits;

/**
 * Pantalla de configuración
 */
public class ConfigActivity extends AppCompatActivity {
    private EditText tbMinRange;
    private EditText tbMaxRange;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);

        this.tbMinRange = (EditText) findViewById(R.id.tb_MinRange);
        this.tbMaxRange = (EditText) findViewById(R.id.tb_MaxRange);
        this.btnUpdate = (Button) findViewById(R.id.btn_upd_range);

        initButtons();
        initRangeValuesFromWS();
    }

    /**
     * Inicializa el comportamiento de los botones
     */
    private void initButtons() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String min = tbMinRange.getText().toString();
                String max = tbMaxRange.getText().toString();
                setConfigValuesInWS(min, max);
            }
        });
    }

    /**
     * Obtiene los límites de temperatura actuales y los inserta en las cajas de texto correspondientes
     */
    private void initRangeValuesFromWS() {
        new NetworkTask(new Callback() {
            @Override
            public void run(String result) {
                TemperatureLimits temperatureLimits = new Gson().fromJson(result,
                        TemperatureLimits.class);
                tbMinRange.setText(String.valueOf(temperatureLimits.getMin()));
                tbMaxRange.setText(String.valueOf(temperatureLimits.getMax()));
            }
        }).execute(UrlBuilder.build("temperature/limits"));
    }

    /**
     * Actualiza los límites de temperatura de acuerdo a los recibidos por parámetro
     * @param min
     * @param max
     */
    private void setConfigValuesInWS(String min, String max) {
        new NetworkTask().execute(
                UrlBuilder.build("temperature/temperature/setlimits?min=" + min + "&max=" + max));
    }
}
