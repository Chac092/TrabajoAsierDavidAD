package com.example.trabajoasierdavidad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//Esta Activity lo que hace es cargar un Recycler con los datos del arraylist que hemos llenado anteriormente
public class mostrarAlumnos extends AppCompatActivity {
    Adaptador adap;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_alumnos);
        //Cargamos el Adaptador
        adap = new Adaptador ();
        //Ponemos un ClickListener para cuando alguien pulse en un Alumno pueda ver su informacion en otra pantalla
        adap.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mostrarAlumnos.this,InsertaroMostarAlumno.class);
                intent.putExtra("origen","Visualizar");
                intent.putExtra("Alumno",recyclerView.getChildAdapterPosition(v));
                startActivity(intent);
            }
        });
        //Asignamos el recycler a su elemento
        recyclerView = findViewById(R.id.RecyclerView);
        //Enlazamos el recycler con su adaptador
        recyclerView.setAdapter(adap);
        //Le damos el formato deseado a nuestro Recycler en este caso un linear Layout
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        //Cargamos el Recycler
        recyclerView.setLayoutManager(layoutManager);
    }

}
