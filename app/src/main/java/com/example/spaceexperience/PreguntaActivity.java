package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import JSONs.Idioma;
import JSONs.Pregunta;


public class PreguntaActivity extends AppCompatActivity {
    /*----------------Constantes--------------------*/
    public static final String DIR_SEPAR = File.separator;
    public static final String DIRECTORY_JSONS = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "JSONs";
    public static final String DIRECTORY_IMAGES = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "Imagenes Android" + DIR_SEPAR;
    public static final String CATALAN = DIRECTORY_JSONS + DIR_SEPAR + "catalan.json";
    public static final String CASTELLANO = DIRECTORY_JSONS + DIR_SEPAR + "castellano.json";
    public static final String INGLES = DIRECTORY_JSONS + DIR_SEPAR + "ingles.json";
    /*--------------------Atributos-----------------------------*/
    private String nivel;
    private Idioma catalan = new Idioma();
    private Idioma castellano = new Idioma();
    private Idioma ingles = new Idioma();
    private ArrayList<Pregunta> preguntas = new ArrayList<>();
    private CountDownTimer timer;
    private Pregunta pregunta;
    private int score = 0;
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pregunta);
        final TextView textViewCounter = findViewById(R.id.counter);
        final TextView textViewPuntos = findViewById(R.id.txtPuntos);
        TextView textViewPregunta = findViewById(R.id.txtPregunta);
        ImageView imageViewPregunta = findViewById(R.id.imagenAleatoria);
        Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);
        File directory = new File(DIRECTORY_IMAGES);
        File[] files = directory.listFiles();


        try {
            getCatalan();
            getCastellano();
            getIngles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        timer = new CountDownTimer(15000, 1000) {
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
        nivel = intent.getStringExtra("nivel");
        textViewPuntos.setText(Integer.toString(score));
        cargarPreguntas();
        // ----------------------Prueba---------------------//
        pregunta = preguntaAleatoria(preguntas);
        textViewPregunta.setText(pregunta.getPregunta());
        buttonRespuesta1.setText(pregunta.getRespuestas().get(0).getRespuesta());
        buttonRespuesta2.setText(pregunta.getRespuestas().get(1).getRespuesta());
        buttonRespuesta3.setText(pregunta.getRespuestas().get(2).getRespuesta());
        buttonRespuesta4.setText(pregunta.getRespuestas().get(3).getRespuesta());
        int random = (int) Math.floor(Math.random()*files.length);
        Bitmap bmImg = BitmapFactory.decodeFile(files[random].getAbsolutePath());
        imageViewPregunta.setImageBitmap(bmImg);
        // -------------------------------------------------------//
        buttonRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(0);
            }
        });

        buttonRespuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(1);
            }
        });

        buttonRespuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(2);
            }
        });

        buttonRespuesta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(3);
            }
        });
    }

    @Override
    public void onBackPressed(){
    }

    public void getCatalan () throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(CATALAN);
        BufferedReader br = new BufferedReader(fr);
        Type typeCat = new TypeToken<Idioma>() {}.getType();
        catalan =  gson.fromJson(br,typeCat);
    }

    public void getCastellano () throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(CASTELLANO);
        BufferedReader br = new BufferedReader(fr);
        Type typeCas = new TypeToken<Idioma>() {}.getType();
        castellano =  gson.fromJson(br,typeCas);
    }

    public void getIngles () throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(INGLES);
        BufferedReader br = new BufferedReader(fr);
        Type typeIng = new TypeToken<Idioma>() {}.getType();
        ingles =  gson.fromJson(br,typeIng);
    }


    public void cargarPreguntas()
    {
        String idioma = this.getResources().getConfiguration().locale.getISO3Language();
        if (idioma.equals("cat"))
        {
            preguntas=cargarNivel(catalan);
        }
        else if(idioma.equals("spa"))
        {
            preguntas=cargarNivel(castellano);
        }else if(idioma.equals("eng"))
        {
            preguntas=cargarNivel(ingles);
        }
        else{
            preguntas=cargarNivel(ingles);
        }
    }
    public ArrayList<Pregunta> cargarNivel(Idioma idioma)
    {
        ArrayList<Pregunta> arrayListPreguntas = new ArrayList<>();
        switch (nivel)
        {
            case "infantil":
                arrayListPreguntas= (ArrayList<Pregunta>) idioma.getInfantil();
                break;
            case "facil":
                arrayListPreguntas= (ArrayList<Pregunta>) idioma.getFacil();
                break;
            case "normal":
                arrayListPreguntas= (ArrayList<Pregunta>) idioma.getMedio();
                break;
            case "dificil":
                arrayListPreguntas= (ArrayList<Pregunta>) idioma.getDificil();
                break;
        }
        return arrayListPreguntas;
    }
    public Pregunta preguntaAleatoria(ArrayList<Pregunta> preguntasRestantes){
        int size = preguntasRestantes.size();
        int random = (int) Math.floor(Math.random()*size);
        Pregunta p = preguntasRestantes.get(random);
        return p;
    }
    public void accionBoton(int respuesta)
    {
        TextView textViewPuntos = findViewById(R.id.txtPuntos);
        TextView textViewCounter = findViewById(R.id.counter);
        if (contador < 10){
            if(Integer.parseInt(textViewCounter.getText().toString()) >= 1)
            {
                if(pregunta.getRespuestas().get(respuesta).isCorrecta()){
                    score += 2*(Integer.parseInt(textViewCounter.getText().toString()));
                    textViewPuntos.setText(Integer.toString(score));
                    timer.cancel();
                    timer.start();
                    contador++;
                    pregunta = preguntaAleatoria(preguntas);
                    refrescarCampos(pregunta);
                }
                else{
                    score -= 5;
                    textViewPuntos.setText(Integer.toString(score));
                    timer.cancel();
                    timer.start();
                    contador++;
                    pregunta = preguntaAleatoria(preguntas);
                    refrescarCampos(pregunta);
                }
            }
        }
    }
    public void refrescarCampos(Pregunta pre){
        Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);
        TextView textViewPregunta = findViewById(R.id.txtPregunta);
        ImageView imageViewPregunta = findViewById(R.id.imagenAleatoria);

        File directory = new File(DIRECTORY_IMAGES);
        File[] files = directory.listFiles();
        int random = (int) Math.floor(Math.random()*files.length);

        //--------------------PRUEBA

        textViewPregunta.setText(pre.getPregunta());
        buttonRespuesta1.setText(pre.getRespuestas().get(0).getRespuesta());
        buttonRespuesta2.setText(pre.getRespuestas().get(1).getRespuesta());
        buttonRespuesta3.setText(pre.getRespuestas().get(2).getRespuesta());
        buttonRespuesta4.setText(pre.getRespuestas().get(3).getRespuesta());
        Bitmap bmImg = BitmapFactory.decodeFile(files[random].getAbsolutePath());
        imageViewPregunta.setImageBitmap(bmImg);
    }
}

