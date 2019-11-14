package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import JSONs.Idioma;
import JSONs.Resultado;

import static com.example.spaceexperience.PreguntaActivity.DIRECTORY_JSONS;
import static com.example.spaceexperience.PreguntaActivity.DIR_SEPAR;

public class RankingActivity extends AppCompatActivity {

    private ArrayList<Resultado> resultados = new ArrayList<>();
    public static final String RESULTADOS = DIRECTORY_JSONS + DIR_SEPAR + "resultados.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        final GridView gridView = findViewById(R.id.GrdRanking);

        try {
            getResults();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Adaptador adaptador = new Adaptador(this, resultados);

        gridView.setAdapter(adaptador);
    }

    public void getResults() throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fr = new FileReader(RESULTADOS);
        BufferedReader br = new BufferedReader(fr);
        Type typeRes = new TypeToken<ArrayList<Resultado>>() {}.getType();
        resultados = gson.fromJson(br, typeRes);
    }
}
