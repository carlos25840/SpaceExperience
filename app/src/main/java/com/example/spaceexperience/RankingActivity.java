package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import JSONs.Resultado;

public class RankingActivity extends AppCompatActivity {
    public static final String DIR_SEPAR = File.separator;
    public static final String DIRECTORY_JSONS = Environment.getExternalStorageDirectory() +
            DIR_SEPAR + "JSONs";
    public static final String RESULTADOS = DIRECTORY_JSONS + DIR_SEPAR + "resultados.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<Resultado> resultados = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        GridView gridViewRanking = (GridView)findViewById(R.id.GridRanking);
        try {
            resultados = getResults();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RankingAdapter rankingAdapter = new RankingAdapter(getApplicationContext(),resultados);
        gridViewRanking.setAdapter(rankingAdapter);
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
}
