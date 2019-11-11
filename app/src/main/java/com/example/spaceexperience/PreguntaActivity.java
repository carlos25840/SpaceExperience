package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

import JSONs.Idioma;


public class PreguntaActivity extends AppCompatActivity {

    private int score = 0;
    public static final String DIR_SEPAR = File.separator;
    public static final String DIRECTORY_JSONS = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "JSONs";
    public static final String CATALAN = DIRECTORY_JSONS + DIR_SEPAR + "catalan.json";
    public static final String CASTELLANO = DIRECTORY_JSONS + DIR_SEPAR + "castellano.json";
    public static final String INGLES = DIRECTORY_JSONS + DIR_SEPAR + "ingles.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pregunta);
        final TextView textViewCounter = findViewById(R.id.counter);
        final TextView textViewPuntos = findViewById(R.id.txtPuntos);
        final Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);
        final TextView textViewPregunta = findViewById(R.id.txtPregunta);
        Idioma catalan = new Idioma();
        Idioma castellano = new Idioma();
        Idioma ingles = new Idioma();
        try {
            catalan = getCatalan();
            castellano = getCastellano();
            ingles = getIngles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final CountDownTimer timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewCounter.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                score -= 5;
                textViewPuntos.setText(Integer.toString(score));
                this.cancel();
                this.start();
            }
        };

        timer.start();

        final Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        textViewPuntos.setText(Integer.toString(score));
        // ----------------------Prueba---------------------//
        textViewPregunta.setText(catalan.getInfantil().get(0).getPregunta());
        // -------------------------------------------------------//
        buttonRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreguntaActivity.this, PreguntaActivity.class);
                if(Integer.parseInt(textViewCounter.getText().toString()) >= 1)
                {
                    score += 2*(Integer.parseInt(textViewCounter.getText().toString()));
                    textViewPuntos.setText(Integer.toString(score));
                    timer.cancel();
                    timer.start();
                }
            }
        });

        buttonRespuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreguntaActivity.this, PreguntaActivity.class);
                if(Integer.parseInt(textViewCounter.getText().toString()) >= 1)
                {
                    score += 2*(Integer.parseInt(textViewCounter.getText().toString()));
                    textViewPuntos.setText(Integer.toString(score));
                    timer.cancel();
                    timer.start();
                }
            }
        });

        buttonRespuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreguntaActivity.this, PreguntaActivity.class);
                if(Integer.parseInt(textViewCounter.getText().toString()) >= 1)
                {
                    score += 2*(Integer.parseInt(textViewCounter.getText().toString()));
                    textViewPuntos.setText(Integer.toString(score));
                    timer.cancel();
                    timer.start();
                }
            }
        });

        buttonRespuesta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreguntaActivity.this, PreguntaActivity.class);
                if(Integer.parseInt(textViewCounter.getText().toString()) >= 1)
                {
                    score += 2*(Integer.parseInt(textViewCounter.getText().toString()));
                    textViewPuntos.setText(Integer.toString(score));
                    timer.cancel();
                    timer.start();
                }
            }
        });



    }

    @Override
    public void onBackPressed(){
        return;
    }
    public Idioma getCatalan () throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(CATALAN);
        BufferedReader br = new BufferedReader(fr);
        Type typeCat = new TypeToken<Idioma>() {}.getType();
        Idioma catalan =  gson.fromJson(br,typeCat);
        return catalan;
    }

    public Idioma getCastellano () throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(CASTELLANO);
        BufferedReader br = new BufferedReader(fr);
        Type typeCas = new TypeToken<Idioma>() {}.getType();
        Idioma castellano =  gson.fromJson(br,typeCas);
        return castellano;
    }

    public Idioma getIngles () throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(INGLES);
        BufferedReader br = new BufferedReader(fr);
        Type typeIng = new TypeToken<Idioma>() {}.getType();
        Idioma ingles =  gson.fromJson(br,typeIng);
        return ingles;
    }
}
