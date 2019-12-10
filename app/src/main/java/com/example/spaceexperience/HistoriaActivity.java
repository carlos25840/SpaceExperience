package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ApplicationErrorReport;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
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
    private int contador = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Controla que la activity se ponga en modo pantalla completa*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_historia);
        //Variable
        final Button btnSiguiente = findViewById(R.id.BtnSiguiente);
        final String nivel;
        final ImageView imageViewLaika = findViewById(R.id.imagenPersonaje);
        //Recuperamos el intent que le pasamos con el extra de nivel
        final Intent intent = getIntent();
        nivel = intent.getStringExtra("nivel");

        imageViewLaika.setImageResource(R.drawable.laika2);

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
                    btnSiguiente.setEnabled(false);
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
                else if(contador==1 && nivel.equals("final"))
                {
                    btnSiguiente.setEnabled(false);
                    //Se espera hasta que acabe la animación
                    ImageView imageViewAstronauta = findViewById(R.id.imagenPersonaje);
                    Animation animation = AnimationUtils.loadAnimation(HistoriaActivity.this, R.anim.zoomout);
                    imageViewAstronauta.startAnimation(animation);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            btnSiguiente.setEnabled(false);
                            ImageView imageViewAstronauta = findViewById(R.id.imagenPersonaje);

                            int insignias = intent.getIntExtra("insignias",0);
                            String historia;
                            if(insignias>9){
                                imageViewAstronauta.setImageResource(R.drawable.tereshkova);
                                historia = getResources().getString(R.string.tereshkova);
                            } else if(insignias>6){
                                imageViewAstronauta.setImageResource(R.drawable.gagarin);
                                historia = getResources().getString(R.string.gagarin);
                            } else if(insignias>3){
                                imageViewAstronauta.setImageResource(R.drawable.armstrong);
                                historia = getResources().getString(R.string.armstrong);
                            } else if(insignias>0){
                                imageViewAstronauta.setImageResource(R.drawable.aldrin);
                                historia = getResources().getString(R.string.aldrin);

                            } else{
                                imageViewAstronauta.setImageResource(R.drawable.meteorito);
                                historia = getResources().getString(R.string.piedra);
                            }
                            Animation animation = AnimationUtils.loadAnimation(HistoriaActivity.this, R.anim.fadein);
                            imageViewAstronauta.startAnimation(animation);
                            mostrarTexto(historia);
                            contador++;
                        }
                    }, 1000);
                }
                else if(contador==2 && nivel.equals("final"))
                {
                    btnSiguiente.setEnabled(false);
                    final ImageView imageViewLaika = findViewById(R.id.imagenPersonaje);
                    Animation animation = AnimationUtils.loadAnimation(HistoriaActivity.this, R.anim.fadeout);
                    imageViewLaika.startAnimation(animation);
                    //Se espera hasta que acabe la animación
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(HistoriaActivity.this, RankingActivity.class);
                            startActivity(intent);
                        }
                    }, 1000);
                }

            }
        });
    }

    /*Método que nos muestra el texto que le pasamos por parámetro*/
    public void mostrarTexto(final String texto)
    {
        final TypeWriter typeWriter = findViewById(R.id.txtHistoria);
        final Button btnSiguiente = findViewById(R.id.BtnSiguiente);
        typeWriter.animateText(texto, btnSiguiente);
        //Puesto porque en mi movil se mostraba con letras normales aun cuando está en el xml
        Typeface face = ResourcesCompat.getFont(HistoriaActivity.this, R.font.alien);
        typeWriter.setTypeface(face);
    }
    /*Método que nos bloquea el botón atrás*/
    @Override
    public void onBackPressed(){
    }
}
