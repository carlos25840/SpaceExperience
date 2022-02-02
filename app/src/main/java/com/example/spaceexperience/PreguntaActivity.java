package com.example.spaceexperience;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import jsons.Idioma;
import jsons.Pregunta;
import jsons.Resultado;


public class PreguntaActivity extends AppCompatActivity {
    /*----------------Constantes--------------------*/
    public static final String DIR_SEPAR = File.separator;
    public static final String DIRECTORY_JSONS = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "jsons";
    public static final String DIRECTORY_IMAGES = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "imagenes" + DIR_SEPAR;
    public static final String RESULTADOS = DIRECTORY_JSONS + DIR_SEPAR + "resultados.json";
    public static final int PREGUNTAS = 10;
    public static final int TIEMPO = 15000;
    public static final int TIEMPO_INFANTIL = 30000;
    public static final int TIEMPO_ESPERA = 2000;
    /*--------------------Atributos-----------------------------*/
    private String nivel;

    private ArrayList<Pregunta> preguntas = new ArrayList<>();
    private CountDownTimer timer;
    private Pregunta pregunta;
    private int score = 0;
    private int contador = 0;
    private int insignias = 0;
    private ArrayList<Resultado> resultados = new ArrayList<>();
    private ArrayList<String> files;
    private int tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Controla que la activity se ponga en modo pantalla completa*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pregunta);
        final TextView textViewCounter = findViewById(R.id.counter);
        final TextView textViewPuntos = findViewById(R.id.txtPuntos);
        final TextView textViewRestantes = findViewById(R.id.preguntasRestantes);
        final Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        final Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        final Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        final Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);
        ImageButton imgButtonHome = findViewById(R.id.BtnHome);
        final ProgressBar progressBar = findViewById(R.id.pg_bar);
        File fileRanking = new File(RESULTADOS);

        files = new ArrayList<>();

        for (int i = 1; i < 12 ; i++){
            files.add("astronauta" + i);
        }

        if(fileRanking.exists()){  //Si hay un JSON con resultados los carga para el ranking
            try {
                getResults();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /*Se recupera el intent y se guarda el extra que le pasamos de la otra
        * activity en el atributo nivel*/
        final Intent intent = getIntent();
        nivel = intent.getStringExtra("nivel");
        //Muestra los puntos iniciales que será 0
        textViewPuntos.setText(Integer.toString(score));
        textViewRestantes.setText(getResources().getString(R.string.restantes)+ (PREGUNTAS - contador));
        //Carga la preguntas según el idioma seleccionado
        cargarPreguntas();
        //Guarda en una variable la cantidad de preguntas que hay
        final int size = preguntas.size();
        final int sizeFile = 11;

        //Creamos un contador
        timer = new CountDownTimer(tiempo, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Mostramos los segundos restantes
                textViewCounter.setTextColor(getResources().getColor(R.color.blanco));
                textViewCounter.setText(Long.toString(millisUntilFinished / 1000));
                if(nivel.equals("infantil")){
                    progressBar.setProgress((int)(millisUntilFinished / 1000)*(100/30));
                }else{
                    progressBar.setProgress((int)(millisUntilFinished / 1000)*(100/15));
                }
                //Si solo faltan 5 segundos se muestran en rojo
                if((millisUntilFinished / 1000) <= 5)
                {
                    textViewCounter.setTextColor(getResources().getColor(R.color.rojo));
                }
            }
            //Al llegar a 0 el contador
            @Override
            public void onFinish() {
                //Restamos 5 puntos
                score -= 5;
                //Mostramos los puntos totales
                textViewPuntos.setText(Integer.toString(score));
                //Cancelamos el contador
                this.cancel();
                //Sumamos uno al contador de preguntas
                contador++;
                textViewRestantes.setText(getResources().getString(R.string.restantes)+ (PREGUNTAS - contador));
                //Llamamos a la funcion pintarBotones que nos muestra la respuesta correcta en verde
                pintarBotones();

                //Si no han pasado todas las preguntas
                if (contador < size) {
                    //Se elimina la pregunta mostrada para que no se repita
                    preguntas.remove(pregunta);
                    //Se busca una nueva pregunta aleatoria
                    pregunta = preguntaAleatoria(preguntas);
                    //Se muestra la nueva pregunta
                    refrescarCampos(pregunta);
                }
                //Si ya han pasado todas las preguntas muestra un diálogo
                else{
                    showTextDialog();
                }
            }
        };
        timer.start(); // empieza el contador para la 1 pregunta
        //Carga una pregunta aleatoria
        pregunta = preguntaAleatoria(preguntas);
        //Muestra la pregunta
        refrescarCampos(pregunta);

        //Listener para el boton 1, 2, 3 y 4
        buttonRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llama a la funcion accionBoton que comprueba la respuesta correcta,
                // pasandole la respuesta(boton), la cantidad de pregunta sy de imagenes
                accionBoton(0, size, sizeFile);
            }
        });

        buttonRespuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(1, size, sizeFile);
            }
        });

        buttonRespuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(2, size, sizeFile);
            }
        });

        buttonRespuesta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(3, size, sizeFile);
            }
        });

        imgButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreguntaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Carga los JSON en el idioma
    public Idioma getIdioma (String lang) throws FileNotFoundException {
        Idioma idioma;

        String preguntas = "";

        switch (lang){
            case "cat":
                preguntas = "{\"infantil\":[{\"pregunta\":\"Quants planetes té el nostre sistema solar?\",\"respuestas\":[{\"respuesta\":\"4\",\"correcta\":false},{\"respuesta\":\"8\",\"correcta\":true},{\"respuesta\":\"9\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":false}]},{\"pregunta\":\"En el nostre Sistema Solar, quin és el planeta més calent?\",\"respuestas\":[{\"respuesta\":\"Terra\",\"correcta\":false},{\"respuesta\":\"Venus\",\"correcta\":true},{\"respuesta\":\"Júpiter\",\"correcta\":false},{\"respuesta\":\"Urà\",\"correcta\":false}]},{\"pregunta\":\"Quin és el planeta més proper al sol?\",\"respuestas\":[{\"respuesta\":\"Mercuri\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false},{\"respuesta\":\"Mart\",\"correcta\":false}]},{\"pregunta\":\"Com es diu el planeta on vivim?\",\"respuestas\":[{\"respuesta\":\"Mart\",\"correcta\":false},{\"respuesta\":\"La Terra\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false}]},{\"pregunta\":\"Quin és el planeta més llunyà al sol?\",\"respuestas\":[{\"respuesta\":\"Neptú\",\"correcta\":true},{\"respuesta\":\"Júpiter\",\"correcta\":false},{\"respuesta\":\"Urà\",\"correcta\":false},{\"respuesta\":\"Saturn\",\"correcta\":false}]},{\"pregunta\":\"El sol és un/a...\",\"respuestas\":[{\"respuesta\":\"Planeta\",\"correcta\":false},{\"respuesta\":\"Estrella\",\"correcta\":true},{\"respuesta\":\"Galàxia\",\"correcta\":false},{\"respuesta\":\"Meteorit\",\"correcta\":false}]},{\"pregunta\":\"Quin és el planeta més gran del sistema solar?\",\"respuestas\":[{\"respuesta\":\"Saturn\",\"correcta\":false},{\"respuesta\":\"La Terra\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false}]},{\"pregunta\":\"Els humans hem arribat a...\",\"respuestas\":[{\"respuesta\":\"Mart\",\"correcta\":false},{\"respuesta\":\"La lluna\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Mercuri\",\"correcta\":false}]},{\"pregunta\":\"En el nostre Sistema Solar, quin és el planeta més petit?\",\"respuestas\":[{\"respuesta\":\"Terra\",\"correcta\":false},{\"respuesta\":\"Mart\",\"correcta\":false},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Mercuri\",\"correcta\":true}]},{\"pregunta\":\"La lluna és un/a...\",\"respuestas\":[{\"respuesta\":\"Planeta\",\"correcta\":false},{\"respuesta\":\"Meteorit\",\"correcta\":false},{\"respuesta\":\"Estrella\",\"correcta\":false},{\"respuesta\":\"Satèl·lit\",\"correcta\":true}]}],\"facil\":[{\"pregunta\":\"Com es coneix a Mart?\",\"respuestas\":[{\"respuesta\":\"Planeta verd\",\"correcta\":false},{\"respuesta\":\"Planeta vermell\",\"correcta\":true},{\"respuesta\":\"Planeta fred\",\"correcta\":false},{\"respuesta\":\"Planeta blau\",\"correcta\":false}]},{\"pregunta\":\"Com és diu la nostra galàxia?\",\"respuestas\":[{\"respuesta\":\"Via Lactea\",\"correcta\":true},{\"respuesta\":\"Andròmeda\",\"correcta\":false},{\"respuesta\":\"Cometa\",\"correcta\":false},{\"respuesta\":\"NGC 1055\",\"correcta\":false}]},{\"pregunta\":\"Com es coneix a la Terra?\",\"respuestas\":[{\"respuesta\":\"Planeta verd\",\"correcta\":false},{\"respuesta\":\"Planeta vermell\",\"correcta\":false},{\"respuesta\":\"Planeta blau\",\"correcta\":true},{\"respuesta\":\"Planeta viu\",\"correcta\":false}]},{\"pregunta\":\"Plutó és un/a...\",\"respuestas\":[{\"respuesta\":\"Planeta nan\",\"correcta\":true},{\"respuesta\":\"Planeta\",\"correcta\":false},{\"respuesta\":\"Estrella\",\"correcta\":false},{\"respuesta\":\"Satèl·lit\",\"correcta\":false}]},{\"pregunta\":\"Quin planeta té anells?\",\"respuestas\":[{\"respuesta\":\"Mart\",\"correcta\":false},{\"respuesta\":\"La Terra\",\"correcta\":false},{\"respuesta\":\"Saturn\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false}]},{\"pregunta\":\"Quants planetes gasosos hi ha al Sistema Solar?\",\"respuestas\":[{\"respuesta\":\"4\",\"correcta\":true},{\"respuesta\":\"2\",\"correcta\":false},{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":false}]},{\"pregunta\":\"Quants planetes rocosos hi ha al Sistema Solar?\",\"respuestas\":[{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"2\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":false},{\"respuesta\":\"4\",\"correcta\":true}]},{\"pregunta\":\"Els planetes orbiten al voltant del/la...\",\"respuestas\":[{\"respuesta\":\"Centre de la galàxia\",\"correcta\":false},{\"respuesta\":\"Sol\",\"correcta\":true},{\"respuesta\":\"La Terra\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false}]},{\"pregunta\":\"Quant triga la Terra a orbitar sobre si mateixa?\",\"respuestas\":[{\"respuesta\":\"Un any\",\"correcta\":false},{\"respuesta\":\"Un dia\",\"correcta\":true},{\"respuesta\":\"Un mes\",\"correcta\":false},{\"respuesta\":\"Una setmana\",\"correcta\":false}]},{\"pregunta\":\"Quina forma tenen les òrbites dels planetes?\",\"respuestas\":[{\"respuesta\":\"El·lipse\",\"correcta\":true},{\"respuesta\":\"Cercle\",\"correcta\":false},{\"respuesta\":\"Quadrat\",\"correcta\":false},{\"respuesta\":\"Triangle\",\"correcta\":false}]}],\"medio\":[{\"pregunta\":\"Qué és MIR referent a l'espai?\",\"respuestas\":[{\"respuesta\":\"Una antiga estació espacial\",\"correcta\":true},{\"respuesta\":\"Un coet\",\"correcta\":false},{\"respuesta\":\"Un cotxe\",\"correcta\":false},{\"respuesta\":\"Una estrella\",\"correcta\":false}]},{\"pregunta\":\"Quant triga laTerra a orbitar al voltant del sol?\",\"respuestas\":[{\"respuesta\":\"Un dia\",\"correcta\":false},{\"respuesta\":\"Un any\",\"correcta\":true},{\"respuesta\":\"Un mes\",\"correcta\":false},{\"respuesta\":\"Una setmana\",\"correcta\":false}]},{\"pregunta\":\"En quin any es va arribar per primer cop a la lluna?\",\"respuestas\":[{\"respuesta\":\"1970\",\"correcta\":false},{\"respuesta\":\"1972\",\"correcta\":false},{\"respuesta\":\"1969\",\"correcta\":true},{\"respuesta\":\"1965\",\"correcta\":false}]},{\"pregunta\":\"Quant es va descobrir aigua en Mart per primer cop?\",\"respuestas\":[{\"respuesta\":\"2010\",\"correcta\":false},{\"respuesta\":\"2014\",\"correcta\":false},{\"respuesta\":\"2007\",\"correcta\":false},{\"respuesta\":\"2004\",\"correcta\":true}]},{\"pregunta\":\"Quants satèl·lits de Júpiter han sigut descoberts fins avui (2019)?\",\"respuestas\":[{\"respuesta\":\"80\",\"correcta\":false},{\"respuesta\":\"69\",\"correcta\":true},{\"respuesta\":\"103\",\"correcta\":false},{\"respuesta\":\"54\",\"correcta\":false}]},{\"pregunta\":\"Quin d'aquests planetes no té anells?\",\"respuestas\":[{\"respuesta\":\"Venus\",\"correcta\":true},{\"respuesta\":\"Neptú\",\"correcta\":false},{\"respuesta\":\"Urà\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false}]},{\"pregunta\":\"Quantes vegades és el sol més gran que la Terra?\",\"respuestas\":[{\"respuesta\":\"1.000\",\"correcta\":false},{\"respuesta\":\"100.000\",\"correcta\":false},{\"respuesta\":\"500.000\",\"correcta\":false},{\"respuesta\":\"300.000\",\"correcta\":true}]},{\"pregunta\":\"Quants cm creix un astronauta de mitja?\",\"respuestas\":[{\"respuesta\":\"10\",\"correcta\":false},{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":true},{\"respuesta\":\"No creix\",\"correcta\":false}]},{\"pregunta\":\"Quin planeta té menys densitat que l'aigua?\",\"respuestas\":[{\"respuesta\":\"Saturn\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Mart\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false}]},{\"pregunta\":\"Quina és la velocitat de la llum (aproximada)?\",\"respuestas\":[{\"respuesta\":\"500.000 km/h\",\"correcta\":false},{\"respuesta\":\"300.000 km/h\",\"correcta\":true},{\"respuesta\":\"100.000 km/h\",\"correcta\":false},{\"respuesta\":\"200.000 km/h\",\"correcta\":false}]}],\"dificil\":[{\"pregunta\":\"En quin any es va crear l'ESA?\",\"respuestas\":[{\"respuesta\":\"1959\",\"correcta\":false},{\"respuesta\":\"1975\",\"correcta\":true},{\"respuesta\":\"2000\",\"correcta\":false},{\"respuesta\":\"2019\",\"correcta\":false}]},{\"pregunta\":\"De què estan formats els anells de Saturn?\",\"respuestas\":[{\"respuesta\":\"Gel\",\"correcta\":true},{\"respuesta\":\"Roques\",\"correcta\":false},{\"respuesta\":\"Gas\",\"correcta\":false},{\"respuesta\":\"Satèl·lits\",\"correcta\":false}]},{\"pregunta\":\"La primera persona en orbitar la terra va ser:\",\"respuestas\":[{\"respuesta\":\"Yuri Gagarin\",\"correcta\":true},{\"respuesta\":\"Dmitri Kondratiev\",\"correcta\":false},{\"respuesta\":\"Pedro Duque\",\"correcta\":false},{\"respuesta\":\"Neil Armstrong\",\"correcta\":false}]},{\"pregunta\":\"La primera dona en anar a l'espai va ser:\",\"respuestas\":[{\"respuesta\":\"Anne McClain\",\"correcta\":false},{\"respuesta\":\"Svetlana Savitskaya\",\"correcta\":false},{\"respuesta\":\"Christina Koch\",\"correcta\":false},{\"respuesta\":\"Valentina Tereshkova\",\"correcta\":true}]},{\"pregunta\":\"Qui va ser la persona més jove a anar a l'espai?\",\"respuestas\":[{\"respuesta\":\"Guerman Titov\",\"correcta\":true},{\"respuesta\":\"Serguéi Vólkov\",\"correcta\":false},{\"respuesta\":\"Neil Armstrong\",\"correcta\":false},{\"respuesta\":\"John Young\",\"correcta\":false}]},{\"pregunta\":\"La primera imatge d'un forat negre es va obtenir el:\",\"respuestas\":[{\"respuesta\":\"10 d’abril de 2019\",\"correcta\":true},{\"respuesta\":\"4 de març de 2019\",\"correcta\":false},{\"respuesta\":\"27 de desembre de 1976\",\"correcta\":false},{\"respuesta\":\"21 de novembre de 2000\",\"correcta\":false}]},{\"pregunta\":\"L'estació espacial MIR es va llançar a:\",\"respuestas\":[{\"respuesta\":\"1986\",\"correcta\":true},{\"respuesta\":\"1968\",\"correcta\":false},{\"respuesta\":\"2003\",\"correcta\":false},{\"respuesta\":\"2018\",\"correcta\":false}]},{\"pregunta\":\"Què és Hubble?\",\"respuestas\":[{\"respuesta\":\"Un telescopi espacial\",\"correcta\":true},{\"respuesta\":\"Un planeta\",\"correcta\":false},{\"respuesta\":\"Una galàxia\",\"correcta\":false},{\"respuesta\":\"Una nau espacial\",\"correcta\":false}]},{\"pregunta\":\"Quina és l'edat del nostre sistema solar?\",\"respuestas\":[{\"respuesta\":\"4568 milions d'anys\",\"correcta\":true},{\"respuesta\":\"2574 milions d'anys\",\"correcta\":false},{\"respuesta\":\"2020 anys\",\"correcta\":false},{\"respuesta\":\"9587462 milions d'anys\",\"correcta\":false}]},{\"pregunta\":\"Ganímedes és:\",\"respuestas\":[{\"respuesta\":\"Un planeta\",\"correcta\":false},{\"respuesta\":\"Un satèl·lit\",\"correcta\":true},{\"respuesta\":\"Un astronauta\",\"correcta\":false},{\"respuesta\":\"Una estrella\",\"correcta\":false}]}]}\n";
                break;
            case "spa":
                preguntas = "{\"infantil\":[{\"pregunta\":\"¿Cuántos planetas tiene nuestro Sistema Solar?\",\"respuestas\":[{\"respuesta\":\"2\",\"correcta\":false},{\"respuesta\":\"6\",\"correcta\":false},{\"respuesta\":\"8\",\"correcta\":true},{\"respuesta\":\"9\",\"correcta\":false}]},{\"pregunta\":\"¿Cuál es el planeta más pequeño del sistema solar?\",\"respuestas\":[{\"respuesta\":\"La Tierra\",\"correcta\":false},{\"respuesta\":\"Marte\",\"correcta\":false},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Mercurio\",\"correcta\":true}]},{\"pregunta\":\"¿Cómo se llama el planeta donde vivimos?\",\"respuestas\":[{\"respuesta\":\"Marte\",\"correcta\":false},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"La Tierra\",\"correcta\":true},{\"respuesta\":\"Júpiter\",\"correcta\":false}]},{\"pregunta\":\"¿Cuál es el planeta más caliente del sistema solar?\",\"respuestas\":[{\"respuesta\":\"La Tierra\",\"correcta\":false},{\"respuesta\":\"Venus\",\"correcta\":true},{\"respuesta\":\"Júpiter\",\"correcta\":false},{\"respuesta\":\"Urano\",\"correcta\":false}]},{\"pregunta\":\"¿Cuál es el planeta más cercano al sol?\",\"respuestas\":[{\"respuesta\":\"Mercurio\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false},{\"respuesta\":\"Marte\",\"correcta\":false}]},{\"pregunta\":\"¿Cuál es el planeta más lejano del sol?\",\"respuestas\":[{\"respuesta\":\"Neptuno\",\"correcta\":true},{\"respuesta\":\"Júpiter\",\"correcta\":false},{\"respuesta\":\"Urano\",\"correcta\":false},{\"respuesta\":\"Saturno\",\"correcta\":false}]},{\"pregunta\":\"El sol es un/a...\",\"respuestas\":[{\"respuesta\":\"Planeta\",\"correcta\":false},{\"respuesta\":\"Estrella\",\"correcta\":true},{\"respuesta\":\"Galaxia\",\"correcta\":false},{\"respuesta\":\"Meteorito\",\"correcta\":false}]},{\"pregunta\":\"¿Cuál es el planeta más grande del sistema solar?\",\"respuestas\":[{\"respuesta\":\"Saturno\",\"correcta\":false},{\"respuesta\":\"La Tierra\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false}]},{\"pregunta\":\"Los humanos hemos llegado a...\",\"respuestas\":[{\"respuesta\":\"Marte\",\"correcta\":false},{\"respuesta\":\"La luna\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Mercurio\",\"correcta\":false}]},{\"pregunta\":\"La luna es un/a...\",\"respuestas\":[{\"respuesta\":\"Planeta\",\"correcta\":false},{\"respuesta\":\"Meteorito\",\"correcta\":false},{\"respuesta\":\"Estrella\",\"correcta\":false},{\"respuesta\":\"Satélite\",\"correcta\":true}]},{\"pregunta\":\"¿Cuánto tarda la tierra en darle una vuelta al sol?\",\"respuestas\":[{\"respuesta\":\"24 horas\",\"correcta\":false},{\"respuesta\":\"un año\",\"correcta\":true},{\"respuesta\":\"tres meses\",\"correcta\":false},{\"respuesta\":\"cinco años\",\"correcta\":false}]}],\"facil\":[{\"pregunta\":\"¿Cómo se conoce al planeta Marte?\",\"respuestas\":[{\"respuesta\":\"Planeta rojo\",\"correcta\":true},{\"respuesta\":\"Planeta verde\",\"correcta\":false},{\"respuesta\":\"Planeta frío\",\"correcta\":false},{\"respuesta\":\"Planeta lejano\",\"correcta\":false}]},{\"pregunta\":\"¿Cómo se llama nuestra galaxia?\",\"respuestas\":[{\"respuesta\":\"Andrómeda\",\"correcta\":false},{\"respuesta\":\"Vía Lactea\",\"correcta\":true},{\"respuesta\":\"Cometa\",\"correcta\":false},{\"respuesta\":\"NGC 1055\",\"correcta\":false}]},{\"pregunta\":\"¿Cómo se conoce a la Tierra?\",\"respuestas\":[{\"respuesta\":\"Planeta verde\",\"correcta\":false},{\"respuesta\":\"Planeta rojo\",\"correcta\":false},{\"respuesta\":\"Planeta azul\",\"correcta\":true},{\"respuesta\":\"Planeta vivo\",\"correcta\":false}]},{\"pregunta\":\"Plutón es un/a...\",\"respuestas\":[{\"respuesta\":\"Planeta enano\",\"correcta\":true},{\"respuesta\":\"Planeta\",\"correcta\":false},{\"respuesta\":\"Estrella\",\"correcta\":false},{\"respuesta\":\"Satélite\",\"correcta\":false}]},{\"pregunta\":\"¿Qué planeta tiene anillos?\",\"respuestas\":[{\"respuesta\":\"Marte\",\"correcta\":false},{\"respuesta\":\"La Tierra\",\"correcta\":false},{\"respuesta\":\"Saturno\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false}]},{\"pregunta\":\"¿Cuántos planetas gaseosos hay en el Sistema Solar?\",\"respuestas\":[{\"respuesta\":\"4\",\"correcta\":true},{\"respuesta\":\"2\",\"correcta\":false},{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":false}]},{\"pregunta\":\"¿Cuántos planetas rocosos hay en el Sistema Solar?\",\"respuestas\":[{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"2\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":false},{\"respuesta\":\"4\",\"correcta\":true}]},{\"pregunta\":\"Los planetas orbitan alrededor del/la...\",\"respuestas\":[{\"respuesta\":\"Centro de la galaxia\",\"correcta\":false},{\"respuesta\":\"Sol\",\"correcta\":true},{\"respuesta\":\"La Tierra\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false}]},{\"pregunta\":\"¿Cuánto tarda la Tierra a orbitar sobre si misma?\",\"respuestas\":[{\"respuesta\":\"Un año\",\"correcta\":false},{\"respuesta\":\"Un dia\",\"correcta\":true},{\"respuesta\":\"Un mes\",\"correcta\":false},{\"respuesta\":\"Una semana\",\"correcta\":false}]},{\"pregunta\":\"¿Qué forma tienen las órbitas de los planetas?\",\"respuestas\":[{\"respuesta\":\"Elipse\",\"correcta\":true},{\"respuesta\":\"Círculo\",\"correcta\":false},{\"respuesta\":\"Cuadrado\",\"correcta\":false},{\"respuesta\":\"Triángulo\",\"correcta\":false}]}],\"medio\":[{\"pregunta\":\"¿Qué es MIR referente al espacio?\",\"respuestas\":[{\"respuesta\":\"Un cohete\",\"correcta\":false},{\"respuesta\":\"Una antigua estación espacial\",\"correcta\":true},{\"respuesta\":\"Un coche\",\"correcta\":false},{\"respuesta\":\"Una estrella\",\"correcta\":false}]},{\"pregunta\":\"¿Cuánto tarda la Tierra a orbitar alrededor del sol?\",\"respuestas\":[{\"respuesta\":\"Un dia\",\"correcta\":false},{\"respuesta\":\"Un año\",\"correcta\":true},{\"respuesta\":\"Un mes\",\"correcta\":false},{\"respuesta\":\"Una semana\",\"correcta\":false}]},{\"pregunta\":\"¿En qué año se llegó por primera vez a la luna?\",\"respuestas\":[{\"respuesta\":\"1970\",\"correcta\":false},{\"respuesta\":\"1972\",\"correcta\":false},{\"respuesta\":\"1969\",\"correcta\":true},{\"respuesta\":\"1965\",\"correcta\":false}]},{\"pregunta\":\"¿Cuándo se descubrió agua en Marte por primera vez?\",\"respuestas\":[{\"respuesta\":\"2010\",\"correcta\":false},{\"respuesta\":\"2014\",\"correcta\":false},{\"respuesta\":\"2007\",\"correcta\":false},{\"respuesta\":\"2004\",\"correcta\":true}]},{\"pregunta\":\"¿Cuántos satélites de Júpiter han sido descubiertos hasta la fecha (2019)?\",\"respuestas\":[{\"respuesta\":\"80\",\"correcta\":false},{\"respuesta\":\"69\",\"correcta\":true},{\"respuesta\":\"103\",\"correcta\":false},{\"respuesta\":\"54\",\"correcta\":false}]},{\"pregunta\":\"¿Cuál de estos planetas no tiene anillos?\",\"respuestas\":[{\"respuesta\":\"Venus\",\"correcta\":true},{\"respuesta\":\"Neptuno\",\"correcta\":false},{\"respuesta\":\"Urano\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false}]},{\"pregunta\":\"¿Cuántas veces es el Sol más grande que la Tierra?\",\"respuestas\":[{\"respuesta\":\"1.000\",\"correcta\":false},{\"respuesta\":\"100.000\",\"correcta\":false},{\"respuesta\":\"500.000\",\"correcta\":false},{\"respuesta\":\"300.000\",\"correcta\":true}]},{\"pregunta\":\"¿Cuántos cm crece un astronauta de media?\",\"respuestas\":[{\"respuesta\":\"10\",\"correcta\":false},{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":true},{\"respuesta\":\"No crece\",\"correcta\":false}]},{\"pregunta\":\"¿Qué planeta tiene menos densidad que el agua?\",\"respuestas\":[{\"respuesta\":\"Saturno\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Marte\",\"correcta\":false},{\"respuesta\":\"Júpiter\",\"correcta\":false}]},{\"pregunta\":\"¿Cuál es la velocidad de la luz (Aproximada)?\",\"respuestas\":[{\"respuesta\":\"500.000 km/h\",\"correcta\":false},{\"respuesta\":\"300.000 km/h\",\"correcta\":true},{\"respuesta\":\"100.000 km/h\",\"correcta\":false},{\"respuesta\":\"200.000 km/h\",\"correcta\":false}]}],\"dificil\":[{\"pregunta\":\"¿En que año se creó la ESA?\",\"respuestas\":[{\"respuesta\":\"2019\",\"correcta\":false},{\"respuesta\":\"1975\",\"correcta\":true},{\"respuesta\":\"1959\",\"correcta\":false},{\"respuesta\":\"2000\",\"correcta\":false}]},{\"pregunta\":\"¿De qué estan formados los anillos de Saturno?\",\"respuestas\":[{\"respuesta\":\"Hielo\",\"correcta\":true},{\"respuesta\":\"Rocas\",\"correcta\":false},{\"respuesta\":\"Gas\",\"correcta\":false},{\"respuesta\":\"Satélites\",\"correcta\":false}]},{\"pregunta\":\"La primera persona en orbitar la tierra fue: \",\"respuestas\":[{\"respuesta\":\"Yuri Gagarin\",\"correcta\":true},{\"respuesta\":\"Dmitri Kondratiev\",\"correcta\":false},{\"respuesta\":\"Pedro Duque\",\"correcta\":false},{\"respuesta\":\"Neil Armstrong\",\"correcta\":false}]},{\"pregunta\":\"La primera mujer en ir al espacio fue:\",\"respuestas\":[{\"respuesta\":\"Valentina Tereshkova\",\"correcta\":true},{\"respuesta\":\"Christina Koch\",\"correcta\":false},{\"respuesta\":\"Svetlana Savitskaya\",\"correcta\":false},{\"respuesta\":\"Anne McClain\",\"correcta\":false}]},{\"pregunta\":\"¿Quién fue la persona más joven en ir al espacio?\",\"respuestas\":[{\"respuesta\":\"Neil Armstrong\",\"correcta\":false},{\"respuesta\":\"Brian Binnie\",\"correcta\":false},{\"respuesta\":\"Guerman Titov\",\"correcta\":true},{\"respuesta\":\"Serguéi Vólkov\",\"correcta\":false}]},{\"pregunta\":\"La primera imagen de un agujero negro se obtuvo el:\",\"respuestas\":[{\"respuesta\":\"10 de abril de 2019\",\"correcta\":true},{\"respuesta\":\"25 de marzo de 2015\",\"correcta\":false},{\"respuesta\":\"27 de diciembre de 1992\",\"correcta\":false},{\"respuesta\":\"03 de julio de 1986\",\"correcta\":false}]},{\"pregunta\":\"La estación espacial MIR fue lanzada en:\",\"respuestas\":[{\"respuesta\":\"1986\",\"correcta\":true},{\"respuesta\":\"1968\",\"correcta\":false},{\"respuesta\":\"2003\",\"correcta\":false},{\"respuesta\":\"2018\",\"correcta\":false}]},{\"pregunta\":\"¿Qué es Hubble?\",\"respuestas\":[{\"respuesta\":\"Un telescopio espacial\",\"correcta\":true},{\"respuesta\":\"Un planeta\",\"correcta\":false},{\"respuesta\":\"Una galaxia\",\"correcta\":false},{\"respuesta\":\"Una nave espacial\",\"correcta\":false}]},{\"pregunta\":\"¿Cuál es la edad de nuestro sistema solar?\",\"respuestas\":[{\"respuesta\":\"4568 millones de años\",\"correcta\":true},{\"respuesta\":\"35 millones de años\",\"correcta\":false},{\"respuesta\":\"2020 años\",\"correcta\":false},{\"respuesta\":\"576524 millones de años\",\"correcta\":false}]},{\"pregunta\":\"Ganímedes es:\",\"respuestas\":[{\"respuesta\":\"Un planeta enano\",\"correcta\":false},{\"respuesta\":\"Un astronauta\",\"correcta\":false},{\"respuesta\":\"Un satélite\",\"correcta\":true},{\"respuesta\":\"Una estrella\",\"correcta\":false}]}]}\n";
                break;
            default:
                preguntas = "{\"infantil\":[{\"pregunta\":\"How many planets are in our Solar System?\",\"respuestas\":[{\"respuesta\":\"2\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":false},{\"respuesta\":\"9\",\"correcta\":false},{\"respuesta\":\"8\",\"correcta\":true}]},{\"pregunta\":\"The name of the planet where we live is .......?\",\"respuestas\":[{\"respuesta\":\"Earth\",\"correcta\":true},{\"respuesta\":\"Mars\",\"correcta\":false},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Jupiter\",\"correcta\":false}]},{\"pregunta\":\"Which is the smallest planet in the Solar System?\",\"respuestas\":[{\"respuesta\":\"Earth\",\"correcta\":false},{\"respuesta\":\"Mars\",\"correcta\":false},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Mercury\",\"correcta\":true}]},{\"pregunta\":\"Which is the hottest planet in the Solar System?\",\"respuestas\":[{\"respuesta\":\"Earth\",\"correcta\":false},{\"respuesta\":\"Venus\",\"correcta\":true},{\"respuesta\":\"Jupiter\",\"correcta\":false},{\"respuesta\":\"Uranus\",\"correcta\":false}]},{\"pregunta\":\"Which is the nearest planet to the Sun?\",\"respuestas\":[{\"respuesta\":\"Mercury\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Jupiter\",\"correcta\":false},{\"respuesta\":\"Mars\",\"correcta\":false}]},{\"pregunta\":\"Which is the farthest planet to the Sun?\",\"respuestas\":[{\"respuesta\":\"Neptune\",\"correcta\":true},{\"respuesta\":\"Jupiter\",\"correcta\":false},{\"respuesta\":\"Uranus\",\"correcta\":false},{\"respuesta\":\"Saturn\",\"correcta\":false}]},{\"pregunta\":\"The sun is a...\",\"respuestas\":[{\"respuesta\":\"Planet\",\"correcta\":false},{\"respuesta\":\"Star\",\"correcta\":true},{\"respuesta\":\"Galaxy\",\"correcta\":false},{\"respuesta\":\"Meteorite\",\"correcta\":false}]},{\"pregunta\":\"Which is the biggest planet of the Solar System?\",\"respuestas\":[{\"respuesta\":\"Saturn\",\"correcta\":false},{\"respuesta\":\"Earth\",\"correcta\":false},{\"respuesta\":\"Jupiter\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false}]},{\"pregunta\":\"Humans have reached...\",\"respuestas\":[{\"respuesta\":\"Mars\",\"correcta\":false},{\"respuesta\":\"The moon\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Mercury\",\"correcta\":false}]},{\"pregunta\":\"The moon is a...\",\"respuestas\":[{\"respuesta\":\"Planet\",\"correcta\":false},{\"respuesta\":\"Meteorite\",\"correcta\":false},{\"respuesta\":\"Star\",\"correcta\":false},{\"respuesta\":\"Satellite\",\"correcta\":true}]}],\"facil\":[{\"pregunta\":\"By what other name is Mars known?\",\"respuestas\":[{\"respuesta\":\"Red planet\",\"correcta\":true},{\"respuesta\":\"Blue planet\",\"correcta\":false},{\"respuesta\":\"Unknown planet\",\"correcta\":false},{\"respuesta\":\"Green planet\",\"correcta\":false}]},{\"pregunta\":\"What is the name of the galaxy where we live?\",\"respuestas\":[{\"respuesta\":\"Andromeda\",\"correcta\":false},{\"respuesta\":\"NGC 1055\",\"correcta\":false},{\"respuesta\":\"Milky Way\",\"correcta\":true},{\"respuesta\":\"Kite\",\"correcta\":false}]},{\"pregunta\":\"By what other name is the Earth known?\",\"respuestas\":[{\"respuesta\":\"Green planet\",\"correcta\":false},{\"respuesta\":\"Red planet\",\"correcta\":false},{\"respuesta\":\"Blue planet\",\"correcta\":true},{\"respuesta\":\"Alive planet\",\"correcta\":false}]},{\"pregunta\":\"Pluto is a...\",\"respuestas\":[{\"respuesta\":\"Dwarf planet\",\"correcta\":true},{\"respuesta\":\"Planet\",\"correcta\":false},{\"respuesta\":\"Star\",\"correcta\":false},{\"respuesta\":\"Satellite\",\"correcta\":false}]},{\"pregunta\":\"Which planet has rings?\",\"respuestas\":[{\"respuesta\":\"Mars\",\"correcta\":false},{\"respuesta\":\"Earth\",\"correcta\":false},{\"respuesta\":\"Saturn\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false}]},{\"pregunta\":\"How many gas planets are there in the Solar System?\",\"respuestas\":[{\"respuesta\":\"4\",\"correcta\":true},{\"respuesta\":\"2\",\"correcta\":false},{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":false}]},{\"pregunta\":\"How many rocky planets are there in the Solar System?\",\"respuestas\":[{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"2\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":false},{\"respuesta\":\"4\",\"correcta\":true}]},{\"pregunta\":\"Planets orbit around...\",\"respuestas\":[{\"respuesta\":\"The center of the galaxy\",\"correcta\":false},{\"respuesta\":\"Sun\",\"correcta\":true},{\"respuesta\":\"Earth\",\"correcta\":false},{\"respuesta\":\"Jupiter\",\"correcta\":false}]},{\"pregunta\":\"How much it takes for the Earth to orbit itself?\",\"respuestas\":[{\"respuesta\":\"A year\",\"correcta\":false},{\"respuesta\":\"A day\",\"correcta\":true},{\"respuesta\":\"A month\",\"correcta\":false},{\"respuesta\":\"A week\",\"correcta\":false}]},{\"pregunta\":\"Which shape do the orbits of the planets have?\",\"respuestas\":[{\"respuesta\":\"Ellipse\",\"correcta\":true},{\"respuesta\":\"Circle\",\"correcta\":false},{\"respuesta\":\"Square\",\"correcta\":false},{\"respuesta\":\"Triangle\",\"correcta\":false}]}],\"medio\":[{\"pregunta\":\"What's the meaning of MIR referring to space?\",\"respuestas\":[{\"respuesta\":\"An old space station\",\"correcta\":true},{\"respuesta\":\"A car\",\"correcta\":false},{\"respuesta\":\"A star\",\"correcta\":false},{\"respuesta\":\"A rocket\",\"correcta\":false}]},{\"pregunta\":\"How much it takes for the Earth to orbit around the Sun?\",\"respuestas\":[{\"respuesta\":\"A day\",\"correcta\":false},{\"respuesta\":\"A year\",\"correcta\":true},{\"respuesta\":\"A month\",\"correcta\":false},{\"respuesta\":\"A week\",\"correcta\":false}]},{\"pregunta\":\"When was water in Mars found for the first time?\",\"respuestas\":[{\"respuesta\":\"2010\",\"correcta\":false},{\"respuesta\":\"2014\",\"correcta\":false},{\"respuesta\":\"2007\",\"correcta\":false},{\"respuesta\":\"2004\",\"correcta\":true}]},{\"pregunta\":\"How many Jupiter satellites have been discovered to date (2019)?\",\"respuestas\":[{\"respuesta\":\"80\",\"correcta\":false},{\"respuesta\":\"69\",\"correcta\":true},{\"respuesta\":\"103\",\"correcta\":false},{\"respuesta\":\"54\",\"correcta\":false}]},{\"pregunta\":\"When did humans arrived to the moon for the first time?\",\"respuestas\":[{\"respuesta\":\"1970\",\"correcta\":false},{\"respuesta\":\"1972\",\"correcta\":false},{\"respuesta\":\"1969\",\"correcta\":true},{\"respuesta\":\"1965\",\"correcta\":false}]},{\"pregunta\":\"Which of this planets doesn't have rings?\",\"respuestas\":[{\"respuesta\":\"Venus\",\"correcta\":true},{\"respuesta\":\"Neptune\",\"correcta\":false},{\"respuesta\":\"Uranus\",\"correcta\":false},{\"respuesta\":\"Jupiter\",\"correcta\":false}]},{\"pregunta\":\"How many times is the Sun bigger than the Earth?\",\"respuestas\":[{\"respuesta\":\"1.000\",\"correcta\":false},{\"respuesta\":\"100.000\",\"correcta\":false},{\"respuesta\":\"500.000\",\"correcta\":false},{\"respuesta\":\"300.000\",\"correcta\":true}]},{\"pregunta\":\"How many cm does an astronaut grow up?\",\"respuestas\":[{\"respuesta\":\"10\",\"correcta\":false},{\"respuesta\":\"3\",\"correcta\":false},{\"respuesta\":\"5\",\"correcta\":true},{\"respuesta\":\"He/She doesn't grow up\",\"correcta\":false}]},{\"pregunta\":\"Which planet is less dense than water?\",\"respuestas\":[{\"respuesta\":\"Saturn\",\"correcta\":true},{\"respuesta\":\"Venus\",\"correcta\":false},{\"respuesta\":\"Mars\",\"correcta\":false},{\"respuesta\":\"Jupiter\",\"correcta\":false}]},{\"pregunta\":\"What is the speed of light (Approximate)?\",\"respuestas\":[{\"respuesta\":\"500.000 km/h\",\"correcta\":false},{\"respuesta\":\"300.000 km/h\",\"correcta\":true},{\"respuesta\":\"100.000 km/h\",\"correcta\":false},{\"respuesta\":\"200.000 km/h\",\"correcta\":false}]}],\"dificil\":[{\"pregunta\":\"Which year was ESA founded?\",\"respuestas\":[{\"respuesta\":\"1975\",\"correcta\":true},{\"respuesta\":\"2000\",\"correcta\":false},{\"respuesta\":\"2019\",\"correcta\":false},{\"respuesta\":\"1959\",\"correcta\":false}]},{\"pregunta\":\"What are Saturn rings formed from?\",\"respuestas\":[{\"respuesta\":\"Ice\",\"correcta\":true},{\"respuesta\":\"Rocks\",\"correcta\":false},{\"respuesta\":\"Gas\",\"correcta\":false},{\"respuesta\":\"Satellites\",\"correcta\":false}]},{\"pregunta\":\"The first person to orbit the earth was:\",\"respuestas\":[{\"respuesta\":\"Neil Armstrong\",\"correcta\":false},{\"respuesta\":\"Pedro Duque\",\"correcta\":false},{\"respuesta\":\"Dmitri Kondratiev\",\"correcta\":false},{\"respuesta\":\"Yuri Gagarin\",\"correcta\":true}]},{\"pregunta\":\"Who was the youngest person to go to space?\",\"respuestas\":[{\"respuesta\":\"Guerman Titov\",\"correcta\":true},{\"respuesta\":\"John Young\",\"correcta\":false},{\"respuesta\":\"Neil Armstrong\",\"correcta\":false},{\"respuesta\":\"Serguéi Vólkov\",\"correcta\":false}]},{\"pregunta\":\"The first woman to go to space was:\",\"respuestas\":[{\"respuesta\":\"Valentina Tereshkova\",\"correcta\":true},{\"respuesta\":\"Christina Koch\",\"correcta\":false},{\"respuesta\":\"Svetlana Savitskaya\",\"correcta\":false},{\"respuesta\":\"Anne McClain\",\"correcta\":false}]},{\"pregunta\":\"The first image of a black hole was obtained on:\",\"respuestas\":[{\"respuesta\":\"April 10th 2019\",\"correcta\":true},{\"respuesta\":\"March 4th 2015\",\"correcta\":false},{\"respuesta\":\"November 1st 1976\",\"correcta\":false},{\"respuesta\":\"June 2nd 2000\",\"correcta\":false}]},{\"pregunta\":\"The MIR space station was launched in:\",\"respuestas\":[{\"respuesta\":\"1986\",\"correcta\":true},{\"respuesta\":\"1968\",\"correcta\":false},{\"respuesta\":\"2018\",\"correcta\":false},{\"respuesta\":\"2003\",\"correcta\":false}]},{\"pregunta\":\"What is hubble?\",\"respuestas\":[{\"respuesta\":\"A space telescope\",\"correcta\":true},{\"respuesta\":\"A space ship\",\"correcta\":false},{\"respuesta\":\"A planet\",\"correcta\":false},{\"respuesta\":\"A galaxy\",\"correcta\":false}]},{\"pregunta\":\"How old is our solar system?\",\"respuestas\":[{\"respuesta\":\"4568 million years\",\"correcta\":true},{\"respuesta\":\"958743 million years\",\"correcta\":false},{\"respuesta\":\"2020 years\",\"correcta\":false},{\"respuesta\":\"1489 million years\",\"correcta\":false}]},{\"pregunta\":\"Ganymede is:\",\"respuestas\":[{\"respuesta\":\"A satellite\",\"correcta\":true},{\"respuesta\":\"A planet\",\"correcta\":false},{\"respuesta\":\"An astronaut\",\"correcta\":false},{\"respuesta\":\"A star\",\"correcta\":false}]}]}\n";
        }

        Gson gson = new Gson();
        Type type = new TypeToken<Idioma>() {}.getType();
        idioma =  gson.fromJson(preguntas,type);
        return idioma;
    }

    //Obtiene los resultados del JSON para el ranking
    public void getResults() throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(RESULTADOS);
        BufferedReader br = new BufferedReader(fr);
        Type typeRes = new TypeToken<ArrayList<Resultado>>() {}.getType();
        resultados = gson.fromJson(br, typeRes);
    }

    //Carga las preguntas segun el idioma y el nivel
    public void cargarPreguntas()
    {
        Idioma idiomaAux = new Idioma();
        String idioma = this.getResources().getConfiguration().locale.getISO3Language();

        //Carga el json del idioma seleccionado
        try {
            idiomaAux = getIdioma(idioma);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Llama al metodo cargarNivel del idioma seleccionado y guarda las preguntas
        preguntas=cargarNivel(idiomaAux);
    }

    //Funcion que devuelve un arrayList de preguntas del nivel e idioma seleccionado

    public ArrayList<Pregunta> cargarNivel(Idioma idioma)
    {
        ArrayList<Pregunta> arrayListPreguntas = new ArrayList<>();

        //Segun el nivel llama al método getNivel de la clase Idioma
        switch (nivel)
        {
            case "infantil":
                arrayListPreguntas= (ArrayList<Pregunta>) idioma.getInfantil();
                tiempo = TIEMPO_INFANTIL;
                break;
            case "facil":
                arrayListPreguntas= (ArrayList<Pregunta>) idioma.getFacil();
                tiempo = TIEMPO;
                break;
            case "normal":
                arrayListPreguntas= (ArrayList<Pregunta>) idioma.getMedio();
                tiempo = TIEMPO;
                break;
            case "dificil":
                arrayListPreguntas= (ArrayList<Pregunta>) idioma.getDificil();
                tiempo=TIEMPO;
                break;
        }
        //Devulve las preguntas de ese nivel e idioma
        return arrayListPreguntas;
    }

    //Método que recibe un arrayList de preguntas y devuelve una de esas preguntas de forma aleatoria
    public Pregunta preguntaAleatoria(ArrayList<Pregunta> preguntasRestantes){
        //Guarda la cantidad de preguntas restantes para hacer el random
        int size = preguntasRestantes.size();
        //Hace un aleatorio
        int random = (int) Math.floor(Math.random()*size);
        //Obtiene la pregunta que está en la posición del nñumero aleatorio
        Pregunta pregunta = preguntasRestantes.get(random);
        //Ordena la lista de respuestas de manera aleatoria
        Collections.shuffle(pregunta.getRespuestas());
        //Retorna la pregunta aleatoria
        return pregunta;
    }

    //Funcion que recibe el numero de la respuesta(la ubicacion del boton), la cantidad de preguntas
    // y de imágenes comprueba que sea correcta para sumar o restar puntos y muestra la correcta
    // comprueba tambien que no se muestren mas de 10 preguntas, que se muestren solamente la cantidad que
    // hay si hay menos de 10 y que no muestre mas de la cantidad de imagenes disponibles para que no de errores
    public void accionBoton(int respuesta, int size, int sizeFile)
    {
        TextView textViewPuntos = findViewById(R.id.txtPuntos);
        TextView textViewCounter = findViewById(R.id.counter);
        TextView textViewRestantes = findViewById(R.id.preguntasRestantes);
        //Llama a la funcion pintarBotones que muestra con colores la respuesta correcta
        pintarBotones();

        //Si la respuesta es correcta
        if(pregunta.getRespuestas().get(respuesta).isCorrecta()){
            //Según el nivel, si es infantil como tienen el doble de tiempo solamente los puntos serán el tiempo
            // disponible, si es adulto tendrán el tiempo*2 para igualar las puntuaciones en funcion del tiempo
            switch (nivel)
            {
                case "infantil":
                    score += (Integer.parseInt(textViewCounter.getText().toString()));
                    break;
                case "facil":
                case "normal":
                case "dificil":
                    score += 2*(Integer.parseInt(textViewCounter.getText().toString()));
                    break;
            }
            //Muestra los puntos totales
            textViewPuntos.setText(Integer.toString(score));
            //cancela el timer
            timer.cancel();
            //suma uno a la cantidad de preguntas mostradas
            contador++;

            textViewRestantes.setText(getResources().getString(R.string.restantes)+ (PREGUNTAS - contador));
            //agrega una insignia(respuesta correcta)
            insignias++;
        }
        //Si responde mal se le restan 5 puntos, se muestra la puntuacion, se cancela el timer y se añade 1 al
        // contador de las preguntas usadas
        else{
            score -= 5;
            textViewPuntos.setText(Integer.toString(score));
            timer.cancel();
            contador++;
            textViewRestantes.setText(getResources().getString(R.string.restantes)+ (PREGUNTAS - contador));
        }
        //Si no se han mostrado 10 PREGUNTAS y  si no se han mostrado todas las disponibles y si no
        // se han mostrado todas las imagenes disponibles
        if (contador < size && contador < PREGUNTAS && contador < sizeFile){
            //Elimina la pregunta usada para que no se repita
            preguntas.remove(pregunta);
            //Carga una nueva pregunta aleatoria
            pregunta = preguntaAleatoria(preguntas);
            //Muestra la nueva pregunta y reinicia el timer
            refrescarCampos(pregunta);
        }
        //Si ya se han mostrado todas las preguntas (preguntas posibles para que no dé error),
        // muestra un textdialog para pedir nombre
        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    showTextDialog();
                }
            }, TIEMPO_ESPERA);
        }
    }

    /*Comprueba cual es la respuesta correcta y cambia el color del botón a verde, las otras las
    * cambia a rojo*/
    public void pintarBotones() {
        Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);

        //Crea un arraylist de botones para recorrerlo y pintarlos del color correcto
        ArrayList<Button> botones = new ArrayList<>();
        botones.add(buttonRespuesta1);
        botones.add(buttonRespuesta2);
        botones.add(buttonRespuesta3);
        botones.add(buttonRespuesta4);

        //Recorre el arraylist de los botones
        for (int i = 0; i < botones.size(); i++){
            botones.get(i).setEnabled(false);
            //El boton correcto lo pone verde
            if(pregunta.getRespuestas().get(i).isCorrecta())
            {
                botones.get(i).setBackgroundResource(R.drawable.rounded_button_verde);
            }
            //Los otros botones los pone en rojo
            else
            {
                botones.get(i).setBackgroundResource(R.drawable.rounded_button_rojo);
            }
        }
    }

    /*Actualiza los campos de la pregunta y los botones cuando se tiene que cargar una nueva pregunta */
    public void refrescarCampos(final Pregunta pre){
        Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);
        final ArrayList<Button> botones = new ArrayList<>();
        botones.add(buttonRespuesta1);
        botones.add(buttonRespuesta2);
        botones.add(buttonRespuesta3);
        botones.add(buttonRespuesta4);
        final TextView textViewPregunta = findViewById(R.id.txtPregunta);
        final ImageView imageViewPregunta = findViewById(R.id.imagenAleatoria);
        Handler handler = new Handler();
        //Obtiene una imagen aleatoria
        final int random = (int) Math.floor(Math.random()*files.size());

        final int resourceId = getResources().getIdentifier(files.get(random), "drawable",
                this.getApplicationContext().getPackageName());

        final Bitmap bmImg = BitmapFactory.decodeResource(getResources(), resourceId);

        //en la primera pregunta no lo hace porque lo carga en el main
        if(contador!=0){
            handler.postDelayed(new Runnable() {
                public void run() {
                    //Comienza el timer
                    timer.start();
                    imageViewPregunta.setImageBitmap(bmImg);
                    //Elimina la imagen usada para que no se repita
                    files.remove(random);
                    textViewPregunta.setText(pre.getPregunta());
                    //Muestra la preguntas y las respuesta y activa los botones para que se pueda responder
                    for (int i = 0; i < botones.size(); i++){
                        botones.get(i).setEnabled(true);
                        botones.get(i).setBackgroundResource(R.drawable.rounded_button);
                        botones.get(i).setText(pre.getRespuestas().get(i).getRespuesta());
                    }
                }
                //Espera 2 segundos para que puedan ver la respuesta correcta
            }, TIEMPO_ESPERA);
        }

        //Si es la primera pregunta, no espera 2 segundos para mostrarla, ni tiene que activar los botones
        else{
            //empieza el timer
            timer.start();
            imageViewPregunta.setImageBitmap(bmImg);
            //Elimina la imagen para que no se repita
            files.remove(random);
            textViewPregunta.setText(pre.getPregunta());

            //Recorre los botones y muestra las respuestas
            for (int i = 0; i < botones.size(); i++){
                botones.get(i).setBackgroundResource(R.drawable.rounded_button);
                botones.get(i).setText(pre.getRespuestas().get(i).getRespuesta());
            }
        }
    }

    /*Función que nos muestra un textDialog para poder guardar el nombre para el ranking*/
    private void showTextDialog() {
        // get prompts.xml view
        Context context = this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml al builder de alertdialog
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Coge el input del usuario y lo guarda en un string
                                TextView textViewPuntos = findViewById(R.id.txtPuntos);
                                String nombre = userInput.getText().toString();
                                //Guarda los puntos conseguidos en una varianble
                                int puntos = Integer.parseInt(textViewPuntos.getText().toString());
                                /*Se crea un objeto de tipo Resultado y se le asignan los valores
                                 * guardados*/
                                //nombre = nombre.substring(0,15);
                                Resultado result = new Resultado(nombre, puntos, insignias,nivel);
                                /*Se añade el objeto al ArrayList de resultados y se guarda en el fichero
                                 * json correspondiente*/
                                resultados.add(result);
                                File file = new File(RESULTADOS);
                                save(file, resultados);
                                Intent intent = new Intent(PreguntaActivity.this, HistoriaActivity.class);
                                intent.putExtra("nivel", "final");
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("puntos", puntos);
                                intent.putExtra("insignias", insignias);

                                startActivity(intent);
                            }

                        });

        // Crea alertDialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnPositive = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
                btnPositive.setTextSize(getResources().getDimension(R.dimen.textOk));

                Typeface face = ResourcesCompat.getFont(PreguntaActivity.this, R.font.alien);
                btnPositive.setTypeface(face);
            }
        });
        // Muestra el alertDialog
        alertDialog.show();
    }

    /*Función que dado un fichero y el ArrayList los guarda en el fichero json*/
    public void save(File file, ArrayList<Resultado> results) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"))) {
            writer.write(new Gson().toJson(results));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Deshabilita el boton de volver de android
    @Override
    public void onBackPressed(){
    }
}

