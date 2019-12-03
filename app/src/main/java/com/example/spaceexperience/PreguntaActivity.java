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
    private File directory = new File(DIRECTORY_IMAGES);
    private ArrayList<File> files = new ArrayList<>(Arrays.asList(directory.listFiles()));
    private int tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Controla que la activity se ponga en modo pantalla completa*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pregunta);
        final TextView textViewCounter = findViewById(R.id.counter);
        final TextView textViewPuntos = findViewById(R.id.txtPuntos);
        final Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        final Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        final Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        final Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);
        File fileRanking = new File(RESULTADOS);

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
        //Carga la preguntas según el idioma seleccionado
        cargarPreguntas();
        //Guarda en una variable la cantidad de preguntas que hay
        final int size = preguntas.size();
        final int sizeFile = files.size();

        //Creamos un contador
        timer = new CountDownTimer(tiempo, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Mostramos los segundos restantes
                textViewCounter.setTextColor(getResources().getColor(R.color.blanco));
                textViewCounter.setText(Long.toString(millisUntilFinished / 1000));

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
    }

    //Deshabilita el boton de volver de android
    @Override
    public void onBackPressed(){
    }

    //Carga los JSON en el idioma
    public Idioma getIdioma (String ruta) throws FileNotFoundException {
        Idioma idioma;
        Gson gson = new Gson();
        FileReader fr = new FileReader(ruta);
        BufferedReader br = new BufferedReader(fr);
        Type type = new TypeToken<Idioma>() {}.getType();
        idioma =  gson.fromJson(br,type);
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
        String ruta;

        //Segun el idioma selecciona la ruta del json
        switch (idioma){
            case "cat":
                ruta = CATALAN;
                break;
            case "spa":
                ruta = CASTELLANO;
                break;
            case "eng":
            default:
                ruta = INGLES;
                break;
        }

        //Carga el json del idioma seleccionado
        try {
            idiomaAux = getIdioma(ruta);
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
        final Bitmap bmImg = BitmapFactory.decodeFile(files.get(random).getAbsolutePath());

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
                .setPositiveButton("OK",
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
                                Resultado result = new Resultado(nombre, puntos, insignias);
                                /*Se añade el objeto al ArrayList de resultados y se guarda en el fichero
                                 * json correspondiente*/
                                resultados.add(result);
                                File file = new File(RESULTADOS);
                                save(file, resultados);
                                Intent intent = new Intent(PreguntaActivity.this, HistoriaActivity.class);
                                intent.putExtra("nivel", "final");
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("puntos", puntos);
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

                Typeface face2 = ResourcesCompat.getFont(PreguntaActivity.this, R.font.alien);
                btnPositive.setTypeface(face2);
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
}

