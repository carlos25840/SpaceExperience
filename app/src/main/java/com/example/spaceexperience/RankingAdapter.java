package com.example.spaceexperience;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import JSONs.Resultado;

public class RankingAdapter extends ArrayAdapter<Resultado> {
    Context context;
    ArrayList<Resultado> resultados;

    public RankingAdapter(Context context, ArrayList<Resultado> resultados) {
        super(context, R.layout.ranking_player, resultados);
        this.context = context;
        this.resultados = resultados;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.ranking_player,null);
        TextView txtNombre = item.findViewById(R.id.TxtName);
        TextView txtPuntos = item.findViewById(R.id.TxtPoints);
        TextView txtInsignias = item.findViewById(R.id.TxtInsignias);
        TextView txtPos = item.findViewById(R.id.TxtPos);
        int pos = position +1;
        txtPos.setText(Integer.toString(pos));

        switch (pos) {
            case 1:
                txtPos.setTextColor(context.getResources().getColor(R.color.oro));
                txtPos.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case 2:
                txtPos.setTextColor(context.getResources().getColor(R.color.plata));
                txtPos.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case 3:
                txtPos.setTextColor(context.getResources().getColor(R.color.bronce));
                txtPos.setTypeface(Typeface.DEFAULT_BOLD);
                break;
        }

        txtNombre.setText(resultados.get(position).getNombre());
        if (resultados.get(position).getInsignias() == 10) {
            txtInsignias.setTypeface(Typeface.DEFAULT_BOLD);
            txtInsignias.setTextSize(60);
        }
        txtInsignias.setText(Integer.toString(resultados.get(position).getInsignias()));
        txtPuntos.setText(Integer.toString(resultados.get(position).getPuntacion()));
        return (item);
    }
}
