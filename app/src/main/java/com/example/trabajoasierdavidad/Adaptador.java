package com.example.trabajoasierdavidad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter <Adaptador.ViewHolder> {
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;
    private LayoutInflater inflador; //crea layout a partir de xml
    protected ArrayList<Alumno> lista;//libros a visualizar
    public int contador = 0;

    public Adaptador() {

    }

    //creamos nuestro ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public TextView Apellido;
        public TextView DNI;
        public TextView Modulo;
        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.editNombre);
            Apellido = itemView.findViewById(R.id.editApellido);
            DNI = itemView.findViewById(R.id.editDNI);
            Modulo = itemView.findViewById(R.id.editModulo);
        }
    }
    //creamos el viewholder con la vista de un elemento sin personalizar

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        //inflamos vista desde XML
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elementorecicler, parent, false);
        v.setId(contador);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.DNI.setText(Alumno.getAlumnos().get(position).DNI);
        holder.nombre.setText(Alumno.getAlumnos().get(position).Nombre);
        holder.Apellido.setText(Alumno.getAlumnos().get(position).Apellido);
        holder.Modulo.setText(Alumno.getAlumnos().get(position).Modulo);
    }

    @Override
    public int getItemCount() {
        return Alumno.getAlumnos().size();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
