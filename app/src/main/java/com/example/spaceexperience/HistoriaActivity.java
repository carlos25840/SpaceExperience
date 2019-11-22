package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoriaActivity extends AppCompatActivity {

    private String aux = "";
    private String prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);

        Button btnSiguiente = findViewById(R.id.BtnSiguiente);
        final String nivel;
        Intent intent = getIntent();
        nivel = intent.getStringExtra("nivel");
        ImageView imageViewAstronauta = findViewById(R.id.imagenAstronauta);

        if(nivel.equals("infantil"))
        {
            imageViewAstronauta.setImageResource(R.drawable.astronauta_infantil);
        }
        else
        {
            imageViewAstronauta.setImageResource(R.drawable.astronauta_sin_fondo);
        }

        final TextView txtHistoria = findViewById(R.id.txtHistoria);
        prueba = getResources().getString(R.string.historia);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i;
            public void run() {
                SystemClock.sleep(600);
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
                    SystemClock.sleep(40);
                }
                SystemClock.sleep(5000);
            }
        };
        new Thread(runnable).start();

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoriaActivity.this, PreguntaActivity.class);
                intent.putExtra("nivel", nivel);
                startActivity(intent);
            }
        });
    }
}
