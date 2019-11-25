package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class NivelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Controla que la sctivity se ponga en modo pantalla completa*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nivel);
        //Atributos
        ImageButton btnBack = findViewById(R.id.BtnBack);
        Button btnAdult = findViewById(R.id.btnAdulto);
        Button btnInfantil = findViewById(R.id.btnInfantil);

        /*Botones que nos llaman a una activity u otra al hacer click, si seleccionamos Infantil se
        * inicia el juego, si seleccionamos adulto nos salta a la activity de seleccionar dificultad (AdultActivity)*/
        btnInfantil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NivelActivity.this, PreguntaActivity.class);
                /*Se pasa como extra el nivel seleccionado para saber que datos se tienen que cargar*/
                intent.putExtra("nivel", "infantil");
                startActivity(intent);
            }
        });

        btnAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NivelActivity.this, AdultActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
