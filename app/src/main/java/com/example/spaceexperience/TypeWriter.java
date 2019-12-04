package com.example.spaceexperience;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatTextView;

public class TypeWriter extends AppCompatTextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 50; // in ms
    private Button boton;

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();

    private Runnable characterAdder = new Runnable() {

        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));

            //Si no ha llegado al final de la cadena
            if (mIndex <= mText.length()) {
                //vuelve a iniciar
                mHandler.postDelayed(characterAdder, mDelay);
            }
            //si ha mostrado toda la cadena activa el boton
            else
            {
                boton.setEnabled(true);
                boton.setVisibility(View.VISIBLE);
            }
        }
    };

    public void animateText(CharSequence txt, Button boton) {
        this.boton = boton;
        mText = txt;
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long m) {
        mDelay = m;
    }
}
