package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import JSONs.Resultado;

public class RankingActivity extends AppCompatActivity {
    //Constantes
    public static final String DIR_SEPAR = File.separator;
    public static final String DIRECTORY_JSONS = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "JSONs";
    public static final String RESULTADOS = DIRECTORY_JSONS + DIR_SEPAR + "resultados.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Controla que la activity se ponga en modo pantalla completa*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ranking);
        //Variables
        GridView gridViewRanking = findViewById(R.id.GridRanking);
        ImageButton imgBackButton = findViewById(R.id.BtnBackRanking);
        TextView txtPrimero = findViewById(R.id.TxtPrimero);
        TextView txtSegundo = findViewById(R.id.TxtSegundo);
        TextView txtTercero = findViewById(R.id.TxtTercero);
        ArrayList<Resultado> resultados = new ArrayList<>();
        //Se cargan los resultados del JSON en el ArrayList resultados llamando al método getResults();
        try {
            resultados = getResults();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Ordena el array de mayor a menor
        Collections.sort(resultados);
        //Se crea el adaptador personalizado y se asigna a la gridViewRanking
        RankingAdapter rankingAdapter = new RankingAdapter(getApplicationContext(),resultados);
        gridViewRanking.setAdapter(rankingAdapter);
        //Listener del botón que nos inicia la activity MainActivity para poder salir del Ranking
        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*Condicional que controla el tamaño del array para que solo lo llene acorde a la cantidad
        de resultados almacenados*/
        if(resultados.size()>=1){
            if (resultados.get(0).getNombre().equals("")) {
                txtPrimero.setText(R.string.anonimo);
            }
            else {
                txtPrimero.setText(resultados.get(0).getNombre());
            }
            if(resultados.size()>=2){
                if (resultados.get(1).getNombre().equals("")) {
                    txtSegundo.setText(R.string.anonimo);
                }
                else {
                    txtSegundo.setText(resultados.get(1).getNombre());
                }
                if(resultados.size()>=3){
                    if (resultados.get(2).getNombre().equals("")) {
                        txtTercero.setText(R.string.anonimo);
                    }
                    else {
                        txtTercero.setText(resultados.get(2).getNombre());
                    }
                }
            }
        }
    }
    /*Método que nos obtiene los datos del JSON y nos devuelve un ArrayList de tipo Resultado
     * ya cargado con los datos*/
    public ArrayList<Resultado> getResults() throws FileNotFoundException {
        ArrayList<Resultado> resultados;
        Gson gson = new Gson();
        FileReader fr = new FileReader(RESULTADOS);
        BufferedReader br = new BufferedReader(fr);
        Type typeRes = new TypeToken<ArrayList<Resultado>>() {}.getType();
        resultados = gson.fromJson(br, typeRes);
        return resultados;
    }
    @Override
    public void onBackPressed(){
    }
}
