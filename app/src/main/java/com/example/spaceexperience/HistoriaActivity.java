package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

public class HistoriaActivity extends AppCompatActivity {

    private String aux = "";
    private String prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);

        final Intent intent = getIntent();

        final TextView txtHistoria = findViewById(R.id.txtHistoria);
        prueba = txtHistoria.getText().toString();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i=0;
            public void run() {
                for (i = 0; i < prueba.length(); i++) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // need to do tasks on the UI thread
                            aux = aux + prueba.charAt(i);
                            txtHistoria.setText(aux);
                            Log.d("tag", "runn test");
                        }
                    }, 0);
                    //Add some downtime
                    SystemClock.sleep(70);
                }
            }
        };
        new Thread(runnable).start();
    }
}
