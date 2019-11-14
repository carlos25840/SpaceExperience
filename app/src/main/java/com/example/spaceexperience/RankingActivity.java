package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        /*TextView prueba = findViewById(R.id.Prueba);
        Intent intent = getIntent();
        prueba.setText(intent.getStringExtra("puntuacion"));*/
    }
}
