package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoriaActivity extends AppCompatActivity {

    private String aux = "";
    private int i;
    private int contador = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_historia);
        final Button btnSiguiente = findViewById(R.id.BtnSiguiente);
        final String nivel;
        Intent intent = getIntent();
        nivel = intent.getStringExtra("nivel");
        final ImageView imageViewAstronauta = findViewById(R.id.imagenAstronauta);

        if(nivel.equals("infantil"))
        {
            imageViewAstronauta.setImageResource(R.drawable.laika2);
        }
        else
        {
            imageViewAstronauta.setImageResource(R.drawable.astronauta);
        }

        mostrarTexto(getResources().getString(R.string.historia));

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contador == 1)
                {
                    btnSiguiente.setEnabled(false);
                    mostrarTexto(getResources().getString(R.string.historia2));
                    contador++;
                }
                else
                {
                    ImageView imageViewAstronauta = findViewById(R.id.imagenAstronauta);
                    Animation animation = AnimationUtils.loadAnimation(HistoriaActivity.this, R.anim.zoomout);
                    imageViewAstronauta.startAnimation(animation);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        Intent intent = new Intent(HistoriaActivity.this, PreguntaActivity.class);
                        public void run() {
                            intent.putExtra("nivel", nivel);
                            startActivity(intent);
                        }
                    }, 2000);
                }

            }
        });
    }

    public void mostrarTexto(final String texto)
    {
        final Button btnSiguiente = findViewById(R.id.BtnSiguiente);
        final TextView txtHistoria = findViewById(R.id.txtHistoria);
        final Handler handler = new Handler();

        aux="";
        Runnable runnable = new Runnable() {
            public void run() {
                SystemClock.sleep(800);
                for (i = 0; i < texto.length(); i++) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aux = aux + texto.charAt(i);
                            txtHistoria.setText(aux);
                            if (i==texto.length()-1)
                            {
                                btnSiguiente.setEnabled(true);
                                btnSiguiente.setVisibility(View.VISIBLE);
                            }
                            Log.d("tag", "runn test");
                        }
                    }, 0);
                    //Add some downtime
                    SystemClock.sleep(60);
                }
                SystemClock.sleep(2000);
            }
        };
        new Thread(runnable).start();
    }
}
