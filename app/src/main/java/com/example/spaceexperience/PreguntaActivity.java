package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PreguntaActivity extends AppCompatActivity {

    private int score2;
    private boolean time = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pregunta);

        final TextView textViewCounter = findViewById(R.id.counter);
        final TextView textViewPuntos = findViewById(R.id.txtPuntos);
        Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        score2 = score;
        textViewPuntos.setText(Integer.toString(score));

        buttonRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreguntaActivity.this, PreguntaActivity.class);
                if (time)
                {
                    score2 +=(2*Integer.parseInt(textViewCounter.getText().toString()));
                }
                else
                {
                    score2-=5;
                }
                intent.putExtra("score", score2);
                startActivity(intent);
            }
        });

        new CountDownTimer (15000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewCounter.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textViewCounter.setTextColor(getResources().getColor(R.color.rojo));
                textViewCounter.setText(R.string.timeOut);
                time = false;
            }
        }.start();

    }

    @Override
    public void onBackPressed(){
        return;
    }
}
