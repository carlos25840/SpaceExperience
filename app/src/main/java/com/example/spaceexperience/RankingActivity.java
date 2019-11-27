package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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
import java.util.Collection;
import java.util.Collections;

import JSONs.Resultado;

public class RankingActivity extends AppCompatActivity {
    public static final String DIR_SEPAR = File.separator;
    public static final String DIRECTORY_JSONS = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "JSONs";
    public static final String RESULTADOS = DIRECTORY_JSONS + DIR_SEPAR + "resultados.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ranking);
        GridView gridViewRanking = findViewById(R.id.GridRanking);
        ImageButton imgBackButton = findViewById(R.id.BtnBackRanking);
        TextView txtPrimero = findViewById(R.id.TxtPrimero);
        TextView txtSegundo = findViewById(R.id.TxtSegundo);
        TextView txtTercero = findViewById(R.id.TxtTercero);


        ArrayList<Resultado> resultados = new ArrayList<>();
        try {
            resultados = getResults();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Ordena el array de mayor a menor
        Collections.sort(resultados);
        RankingAdapter rankingAdapter = new RankingAdapter(getApplicationContext(),resultados);
        gridViewRanking.setAdapter(rankingAdapter);
        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        txtPrimero.setText(resultados.get(0).getNombre());
        txtSegundo.setText(resultados.get(1).getNombre());
        txtTercero.setText(resultados.get(2).getNombre());
    }
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
