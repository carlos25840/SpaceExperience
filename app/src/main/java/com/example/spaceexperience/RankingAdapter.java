package com.example.spaceexperience;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import JSONs.Resultado;
//Constructor personalizado que hereda de ArrayAdapter
public class RankingAdapter extends ArrayAdapter<Resultado> {
    private Context context;
    private ArrayList<Resultado> resultados;
    //Constructor al que le pasamos el contexto y un ArrayList del tipo Resultado
    public RankingAdapter(Context context, ArrayList<Resultado> resultados) {
        //Se pasa al constructor super, el contexto recibido, el layout personalizado y el ArrayList de resultadps
        super(context, R.layout.ranking_player, resultados);
        this.context = context;
        this.resultados = resultados;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.ranking_player,null);
        //Views
        TextView txtNombre = item.findViewById(R.id.TxtName);
        TextView txtPuntos = item.findViewById(R.id.TxtPoints);
        TextView txtInsignias = item.findViewById(R.id.TxtInsignias);
        TextView txtPos = item.findViewById(R.id.TxtPos);

        //Para llenar las posiciones del ranking se incrementa la position en 1 para no empezar el ranking desde 0
        txtPos.setText(Integer.toString(position +1));

        //Se van cargando los TextView del layout personalizado
        txtNombre.setText(resultados.get(position).getNombre());
        txtInsignias.setText(Integer.toString(resultados.get(position).getInsignias()));
        txtPuntos.setText(Integer.toString(resultados.get(position).getPuntacion()));
        return item;
    }
}
