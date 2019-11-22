package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoriaActivity extends AppCompatActivity {

    private String aux = "";
    private String prueba;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_historia);
        final Button btnSiguiente = findViewById(R.id.BtnSiguiente);
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
            imageViewAstronauta.setImageResource(R.drawable.astronauta);
        }

        final TextView txtHistoria = findViewById(R.id.txtHistoria);
        prueba = getResources().getString(R.string.historia);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                SystemClock.sleep(800);
                for (i = 0; i < prueba.length(); i++) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // need to do tasks on the UI thread
                            aux = aux + prueba.charAt(i);
                            txtHistoria.setText(aux);
                            if (i==prueba.length()-1)
                            {
                                btnSiguiente.setEnabled(true);
                                btnSiguiente.setVisibility(View.VISIBLE);
                            }
                            Log.d("tag", "runn test");
                        }
                    }, 0);
                    //Add some downtime
                    SystemClock.sleep(80);
                }
                SystemClock.sleep(2000);
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
    public void comprobar()
    {
        Button btnSiguiente = findViewById(R.id.BtnSiguiente);
        if (i==prueba.length()-1)
        {
            btnSiguiente.setEnabled(true);
            btnSiguiente.setVisibility(View.VISIBLE);
        }
    }
}
