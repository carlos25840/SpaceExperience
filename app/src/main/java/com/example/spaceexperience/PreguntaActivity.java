package com.example.spaceexperience;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
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

import JSONs.Idioma;
import JSONs.Pregunta;
import JSONs.Resultado;


public class PreguntaActivity extends AppCompatActivity {
    /*----------------Constantes--------------------*/
    public static final String DIR_SEPAR = File.separator;
    public static final String DIRECTORY_JSONS = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "JSONs";
    public static final String DIRECTORY_IMAGES = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "imagenes" + DIR_SEPAR;
    public static final String CATALAN = DIRECTORY_JSONS + DIR_SEPAR + "catalan.json";
    public static final String CASTELLANO = DIRECTORY_JSONS + DIR_SEPAR + "castellano.json";
    public static final String INGLES = DIRECTORY_JSONS + DIR_SEPAR + "ingles.json";
    public static final String RESULTADOS = DIRECTORY_JSONS + DIR_SEPAR + "resultados.json";
    public static final int PREGUNTAS = 10;
    public static final int TIEMPO = 15000;
    public static final int TIEMPO_ESPERA = 2000;
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
    private int insignias = 0;
    private ArrayList<Resultado> resultados = new ArrayList<>();
    private File directory = new File(DIRECTORY_IMAGES);
    private ArrayList<File> files = new ArrayList<>(Arrays.asList(directory.listFiles()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pregunta);
        final TextView textViewCounter = findViewById(R.id.counter);
        final TextView textViewPuntos = findViewById(R.id.txtPuntos);
        final Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        final Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        final Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        final Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);
        File file = new File(RESULTADOS);
        try {
            catalan = getIdioma(CATALAN);
            castellano = getIdioma(CASTELLANO);
            ingles = getIdioma(INGLES);
            if(file.exists()){
                getResults();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*Se recupera el intent y se guarda el extra que le pasamos de la otra
        * activity en el atributo nivel*/
        final Intent intent = getIntent();
        nivel = intent.getStringExtra("nivel");

        textViewPuntos.setText(Integer.toString(score));
        cargarPreguntas();
        final int size = preguntas.size();

        timer = new CountDownTimer(TIEMPO, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewCounter.setTextColor(getResources().getColor(R.color.blanco));
                textViewCounter.setText(Long.toString(millisUntilFinished / 1000));
                if((millisUntilFinished / 1000) <= 5)
                {
                    textViewCounter.setTextColor(getResources().getColor(R.color.rojo));
                }
            }

            @Override
            public void onFinish() {
                score -= 5;
                textViewPuntos.setText(Integer.toString(score));
                this.cancel();
                contador++;
                pintarBotones();
                if (contador < size) {
                    preguntas.remove(pregunta);
                    pregunta = preguntaAleatoria(preguntas);
                    refrescarCampos(pregunta);
                }
                else{
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            showTextDialog();
                        }
                    }, TIEMPO_ESPERA);
                }
            }
        };
        timer.start();

        pregunta = preguntaAleatoria(preguntas);
        refrescarCampos(pregunta);

        buttonRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(0, size);
            }
        });

        buttonRespuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(1, size);
            }
        });

        buttonRespuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(2, size);
            }
        });

        buttonRespuesta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBoton(3, size);
            }
        });
    }

    @Override
    public void onBackPressed(){
    }

    public Idioma getIdioma (String ruta) throws FileNotFoundException {
        Idioma idioma = new Idioma();
        Gson gson = new Gson();
        FileReader fr = new FileReader(ruta);
        BufferedReader br = new BufferedReader(fr);
        Type type = new TypeToken<Idioma>() {}.getType();
        idioma =  gson.fromJson(br,type);
        return idioma;
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

    public void getResults() throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(RESULTADOS);
        BufferedReader br = new BufferedReader(fr);
        Type typeRes = new TypeToken<ArrayList<Resultado>>() {}.getType();
        resultados = gson.fromJson(br, typeRes);
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
        //Ordena la lista de respuestas de manera aleatoria
        Collections.shuffle(p.getRespuestas());
        return p;
    }
    public void accionBoton(int respuesta, int size)
    {
        TextView textViewPuntos = findViewById(R.id.txtPuntos);
        TextView textViewCounter = findViewById(R.id.counter);
        pintarBotones();
        if(pregunta.getRespuestas().get(respuesta).isCorrecta()){
            score += 2*(Integer.parseInt(textViewCounter.getText().toString()));
            textViewPuntos.setText(Integer.toString(score));
            timer.cancel();
            contador++;
            insignias++;
        }
        else{
            score -= 5;
            textViewPuntos.setText(Integer.toString(score));
            timer.cancel();
            contador++;
        }
        if (contador < size){
            preguntas.remove(pregunta);
            pregunta = preguntaAleatoria(preguntas);
            refrescarCampos(pregunta);
        } else {
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
        ArrayList<Button> botones = new ArrayList<>();
        botones.add(buttonRespuesta1);
        botones.add(buttonRespuesta2);
        botones.add(buttonRespuesta3);
        botones.add(buttonRespuesta4);

        for (int i = 0; i < botones.size(); i++){
            botones.get(i).setEnabled(false);
            if(pregunta.getRespuestas().get(i).isCorrecta())
            {
                botones.get(i).setBackgroundResource(R.drawable.rounded_button_verde);
            }
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

        final int random = (int) Math.floor(Math.random()*files.size());
        final Bitmap bmImg = BitmapFactory.decodeFile(files.get(random).getAbsolutePath());
        if(contador!=0){
            handler.postDelayed(new Runnable() {
                public void run() {
                    timer.start();
                    imageViewPregunta.setImageBitmap(bmImg);
                    files.remove(random);
                    textViewPregunta.setText(pre.getPregunta());
                    for (int i = 0; i < botones.size(); i++){
                        botones.get(i).setEnabled(true);
                        botones.get(i).setBackgroundResource(R.drawable.rounded_button);
                        botones.get(i).setText(pre.getRespuestas().get(i).getRespuesta());
                    }
                }
            }, TIEMPO_ESPERA);
        }
        else{
            timer.start();
            imageViewPregunta.setImageBitmap(bmImg);
            files.remove(random);
            textViewPregunta.setText(pre.getPregunta());
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
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Coge el input del usuario y lo guarda en un string
                                TextView textViewPuntos = findViewById(R.id.txtPuntos);
                                String resultado = userInput.getText().toString();
                                //Guarda los puntos conseguidos en una varianble
                                int puntos = Integer.parseInt(textViewPuntos.getText().toString());
                                /*Se crea un objeto de tipo Resultado y se le asignan los valores
                                * guardados*/
                                Resultado result = new Resultado(resultado, puntos, insignias);
                                /*Se añade el objeto al ArrayList de resultados y se guarda en el fichero
                                * json correspondiente*/
                                resultados.add(result);
                                File file = new File(RESULTADOS);
                                save(file, resultados);
                                Intent intent = new Intent(PreguntaActivity.this, HistoriaActivity.class);
                                intent.putExtra("nivel", "final");
                                intent.putExtra("nombre", resultado);
                                intent.putExtra("puntos", puntos);
                                startActivity(intent);
                            }
                        });

        // Crea alertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
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
}

