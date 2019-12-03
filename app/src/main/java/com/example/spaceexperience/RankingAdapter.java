package com.example.spaceexperience;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        txtNombre.setText(resultados.get(position).getNombre());
        txtInsignias.setText(Integer.toString(resultados.get(position).getInsignias()));
        txtPuntos.setText(Integer.toString(resultados.get(position).getPuntacion()));
        return (item);
    }
}
