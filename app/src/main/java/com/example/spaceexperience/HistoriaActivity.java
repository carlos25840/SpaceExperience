package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    //Atributos
    private String aux = "";
    private int i;
    private int contador = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Controla que la sctivity se ponga en modo pantalla completa*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_historia);
        //Variable
        final Button btnSiguiente = findViewById(R.id.BtnSiguiente);
        final String nivel;
        final ImageView imageViewAstronauta = findViewById(R.id.imagenPersonaje);
        //Recuperamos el intent que le pasamos con el extra de nivel
        Intent intent = getIntent();
        nivel = intent.getStringExtra("nivel");

        imageViewAstronauta.setImageResource(R.drawable.laika2);

        //Dependiendo de si es el final del juego o no, nos muestra una parte de la historia u otra
        if(!nivel.equals("final"))
        {
            //Una vez creado el string llama al método mostrarTexto pasandole el string
            mostrarTexto(getResources().getString(R.string.historia));
        }
        else
        {
            //Nos coge el nombre y los puntos de la activity PreguntaActivity y muestra un mnesaje
            String nombre = intent.getStringExtra("nombre");
            int puntos = intent.getIntExtra("puntos",0);
            String historia = getResources().getString(R.string.historia3) + nombre +
                    getResources().getString(R.string.historia4) + puntos + getResources().getString(R.string.historia5);
            //Una vez creado el string llama al método mostrarTexto pasándole el string
            mostrarTexto(historia);
        }

        //Listener que controla el botón de siguiente
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //La primera vez que hagamos click nos mostrará la segunda parte de la historia
                if(contador == 1 && !nivel.equals("final"))
                {
                    btnSiguiente.setEnabled(false);
                    mostrarTexto(getResources().getString(R.string.historia2));
                    contador++;
                }
                //La segunda vez nos hará la animación y nos llamará a la siguiente activity
                else if(contador==2 && !nivel.equals("final"))
                {
                    ImageView imageViewAstronauta = findViewById(R.id.imagenPersonaje);
                    //Se crea la animación y se ejecuta
                    Animation animation = AnimationUtils.loadAnimation(HistoriaActivity.this, R.anim.zoomout);
                    imageViewAstronauta.startAnimation(animation);
                    //Esperemos hasta que acabe la animación e iniciamos la activity PreguntaActivity
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        Intent intent = new Intent(HistoriaActivity.this, PreguntaActivity.class);
                        public void run() {
                            /*Se pasa el nivel recibido de las otras activitys donde se seleccionaba el nivel
                            * y se inicia la activity del juego*/
                            intent.putExtra("nivel", nivel);
                            startActivity(intent);
                        }
                    }, 2000);
                }
                /*Si el nivel es final, lo que significa que es al final del juego, se ejecuta la animación
                * y se inicia el ranking*/
                else if(nivel.equals("final"))
                {
                    ImageView imageViewAstronauta = findViewById(R.id.imagenPersonaje);
                    Animation animation = AnimationUtils.loadAnimation(HistoriaActivity.this, R.anim.zoomout);
                    imageViewAstronauta.startAnimation(animation);
                    //Se espera hasta que acabe la animación
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        Intent intent = new Intent(HistoriaActivity.this, RankingActivity.class);
                        public void run() {
                            //Iniciamos la activity RankingActivity
                            startActivity(intent);
                        }
                    }, 2000);
                }

            }
        });
    }

    /*Método que nos muestra el texto que le pasamos por parámetro*/
    public void mostrarTexto(final String texto)
    {
        //Variables
        final Button btnSiguiente = findViewById(R.id.BtnSiguiente);
        final TextView txtHistoria = findViewById(R.id.txtHistoria);
        final Handler handler = new Handler();
        aux="";
        //Clase runeable creada para ir mostrando el texto como si fuera una animación
        Runnable runnable = new Runnable() {
            public void run() {
                SystemClock.sleep(900);
                for (i = 0; i < texto.length(); i++) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /*Mientras se ejecute se va cargando el texto en el String aux y
                            * se va cargando el contenido en el TextView txtHistoria*/
                            aux = aux + texto.charAt(i);
                            txtHistoria.setText(aux);
                            /*Cuando se acabe de leer el string se habilita el botón de siguiente y
                            * se pone como visisble*/
                            if (i==texto.length()-1)
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
        //Se ejecuta el hilo que contiene el Runnable creado anteriormente
        new Thread(runnable).start();
    }
    /*Método que nos bloquea el botón atrás*/
    @Override
    public void onBackPressed(){
    }
}
