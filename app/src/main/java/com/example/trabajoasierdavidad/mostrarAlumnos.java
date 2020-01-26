package com.example.trabajoasierdavidad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class mostrarAlumnos extends AppCompatActivity {
    Adaptador adap;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_alumnos);

        adap = new Adaptador ();
        adap.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mostrarAlumnos.this,InsertaroMostarAlumno.class);
                intent.putExtra("origen","Visualizar");
                intent.putExtra("Alumno",recyclerView.getChildAdapterPosition(v));
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setAdapter(adap);
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }

}
