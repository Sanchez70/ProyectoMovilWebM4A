package com.ista.zhotel.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ista.zhotel.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Servi> clientes;

    public MyAdapter(Context c, ArrayList<Servi> p) {
        context = c;
        clientes = p;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nombre.setText(clientes.get(position).getTitulo());
        holder.apellido.setText(clientes.get(position).getDescripcion());
        //holder.email.setText(clientes.get(position).getFoto());
    }
    @Override
    public int getItemCount() {
        return clientes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, apellido, email;
        public MyViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.titulo);
            apellido = itemView.findViewById(R.id.descripcion);
            //email = itemView.findViewById(R.id.foto);
        }
    }
}
