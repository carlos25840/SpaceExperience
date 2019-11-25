package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class AdultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Controla que la sctivity se ponga en modo pantalla completa*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adult);
        ImageButton btnBack = findViewById(R.id.BtnBack);
        Button btnFacil = findViewById(R.id.btnFacil);
        Button btnNormal = findViewById(R.id.btnNormal);
        Button btnDificil = findViewById(R.id.btnDificil);

        /*Se pasa como extra el nivel seleccionado para saber que datos se tienen que cargar y se inicia la
        * activity del juego en función de la dificultad escogida*/
        btnFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdultActivity.this, PreguntaActivity.class);
                intent.putExtra("nivel", "facil");
                startActivity(intent);
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdultActivity.this, PreguntaActivity.class);
                intent.putExtra("nivel", "normal");
                startActivity(intent);
            }
        });

        btnDificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdultActivity.this, PreguntaActivity.class);
                intent.putExtra("nivel", "dificil");
                startActivity(intent);
            }
        });

        /*Nos retrocede a la activity anterior al presionar el botón atrás*/
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
