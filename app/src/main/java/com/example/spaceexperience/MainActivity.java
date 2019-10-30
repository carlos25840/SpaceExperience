package com.example.spaceexperience;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        final ImageButtonRounded buttonEn = findViewById(R.id.BtnEN);
        final ImageButtonRounded buttonEs = findViewById(R.id.BtnES);
        final ImageButtonRounded buttonCat = findViewById(R.id.BtnCat);
        final ImageButton buttonPlay = findViewById(R.id.playbutton);

        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        mediaPlayer.start();

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    buttonPlay.setImageResource(R.drawable.stopped);
                }
                else
                {
                    mediaPlayer.start();
                    buttonPlay.setImageResource(R.drawable.playing);
                }
            }
        });

        buttonEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });

        buttonCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ca");
            }
        });

        buttonEs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("");
            }
        });

    }

    public void setLocale(String lang) {
        mediaPlayer.stop();
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        recreate();
    }
}
