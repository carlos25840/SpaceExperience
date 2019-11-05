package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PreguntaActivity extends AppCompatActivity {

    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pregunta);
        final TextView textViewCounter = findViewById(R.id.counter);
        final TextView textViewPuntos = findViewById(R.id.txtPuntos);
        final Button buttonRespuesta1 = findViewById(R.id.btnRespuesta1);
        Button buttonRespuesta2 = findViewById(R.id.btnRespuesta2);
        Button buttonRespuesta3 = findViewById(R.id.btnRespuesta3);
        Button buttonRespuesta4 = findViewById(R.id.btnRespuesta4);

        final CountDownTimer timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewCounter.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                score -= 5;
                textViewPuntos.setText(Integer.toString(score));
                this.cancel();
                this.start();
            }
        };

        timer.start();

        final Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        textViewPuntos.setText(Integer.toString(score));
        buttonRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreguntaActivity.this, PreguntaActivity.class);
                if(Integer.parseInt(textViewCounter.getText().toString()) >= 1)
                {
                    score += 2*(Integer.parseInt(textViewCounter.getText().toString()));
                    textViewPuntos.setText(Integer.toString(score));
                    timer.cancel();
                    timer.start();
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        return;
    }
}
