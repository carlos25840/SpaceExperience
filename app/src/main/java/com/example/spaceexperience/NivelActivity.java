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
        /*Controla que la activity se ponga en modo pantalla completa*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nivel);
        //Variables
        ImageButton btnBack = findViewById(R.id.BtnBack);
        Button btnAdult = findViewById(R.id.btnAdulto);
        Button btnInfantil = findViewById(R.id.btnInfantil);

        /*Botones que nos llaman a una activity u otra al hacer click, si seleccionamos Infantil se
        * inicia la historia, si seleccionamos adulto nos salta a la activity de seleccionar dificultad (AdultActivity)*/
        btnInfantil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NivelActivity.this, HistoriaActivity.class);
                /*Se pasa como extra el nivel seleccionado para saber que datos se tienen que cargar*/
                intent.putExtra("nivel", "infantil");
                startActivity(intent);
            }
        });

        btnAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nos inicia la activity de AdultActivity
                Intent intent = new Intent(NivelActivity.this, AdultActivity.class);
                startActivity(intent);
            }
        });

        //Listener que al hacer click en el botón nos lleva a la activity anterior
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
