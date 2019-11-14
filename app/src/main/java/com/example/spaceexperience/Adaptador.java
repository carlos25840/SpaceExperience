package com.example.spaceexperience;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import JSONs.Resultado;

public class Adaptador extends ArrayAdapter<Resultado> {

    private Resultado resultados[];

    private Context context;

    public Adaptador(@NonNull Context context, Resultado resultados[]) {
        super(context, R.layout.ranking_player, resultados);
        this.context = context;
        this.resultados = resultados;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.ranking_player, null);

        TextView TxtPos = item.findViewById(R.id.TxtPos);
        TxtPos.setText(position);

        TextView TxtName = item.findViewById(R.id.TxtName);
        TxtName.setText(resultados[position].getNombre());

        TextView TxtInsignias = item.findViewById(R.id.TxtInsignias);
        TxtInsignias.setText(resultados[position].getInsignias());

        TextView TxtPoints = item.findViewById(R.id.TxtPoints);
        TxtPoints.setText(resultados[position].getPuntación());

        return(item);
    }
}
