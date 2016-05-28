package com.example.gmartin.alarmaiot_soa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnConfig = (Button)findViewById(R.id.btn_config);
        Button btnStatus = (Button)findViewById(R.id.btn_status);

        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                           // Source ---------> Destiny
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
