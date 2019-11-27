package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Constante referente a los permisos
    public static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Verificación de permisos
        if (android.os.Build.VERSION.SDK_INT >= 23)
        {
            /* Si ejecutamos la versión Marshmallow (6.0) o posterior, tendremos que pedir
            permisos en tiempo de ejecución*/

            // Comprobamos si el usuario dio permisos a la app en una ejecución anterior
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {

                /*Si el usuario no nos dio permisos, los otorgamos y ejecutamos el código*/
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }
        }

        //Atributos
        final ImageButtonRounded buttonEn = findViewById(R.id.BtnEN);
        final ImageButtonRounded buttonEs = findViewById(R.id.BtnES);
        final ImageButtonRounded buttonCat = findViewById(R.id.BtnCat);

        final Button btnPlay = findViewById(R.id.BtnJugar);
        final Button btnRanking = findViewById(R.id.BtnRanking);

        /*Evento del botón Play, si hacemos click en jugar nos salta a la
        siguiente activity que es la de nivel*/
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NivelActivity.class);
                startActivity(intent);
            }
        });

        /*Evento del botón Inglés, si hacemos click nos cambia el idioma del juego a Inglés*/
        buttonEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });

        /*Evento del botón Catalán, si hacemos click nos cambia el idioma del juego a Catalán*/
        buttonCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ca");
            }
        });

        /*Evento del botón Español, si hacemos click nos cambia el idioma del juego a Español*/
        buttonEs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("es");
            }
        });

        /*Al hacer click en el botón de Ranking se inicia la activity Ranking*/
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });
    }

    /*Método que nos cambia el idioma del juego en función del seleccionado*/
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        recreate();
    }

    /*Método que nos bloquea el botón atrás para no poder salir del juego */
    @Override
    public void onBackPressed(){
    }

}
