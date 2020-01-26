package com.example.trabajoasierdavidad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button Local;
    Button Online;
    Button Mezcla;
    Button Insertar;
    boolean Descargado = false;
    boolean Mezclado =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(getBaseContext());
        db = dbHelper.getWritableDatabase();
        Local = findViewById(R.id.enseñarLocal);
        Local.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Alumno.getAlumnos().clear();
                 descargarBdLocal();
                 Intent intent = new Intent(MainActivity.this,mostrarAlumnos.class);
                 startActivity(intent);
             }
         });
        Online = findViewById(R.id.enseñarOnline);
        Online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alumno.getAlumnos().clear();
                Toast.makeText(getApplicationContext(),"Descargando. Porfavor espere.",Toast.LENGTH_LONG).show();
                descargarBdOnline();
                intentBDonline();

            }
        });
        Mezcla = findViewById(R.id.enseñarMezcla);
        Mezcla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Mezclando bases de datos. Porfavor espere.",Toast.LENGTH_LONG).show();
                Alumno.getAlumnos().clear();
                descargarBdOnline();
                mezclarBases();
                intentMezcla();
            }
        });

        Insertar = findViewById(R.id.insertar);
        Insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InsertaroMostarAlumno.class);
                intent.putExtra("origen","Insertar");
                startActivity(intent);
            }
        });


    }
    public void descargarBdOnline(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> docRef = db.collection("Alumnos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String DNI = document.getString("DNI");
                            String nombre = document.getString("Nombre");
                            String Apellido = document.getString("Apellido");
                            String Modulo = document.getString("Modulo");
                            Alumno A = new Alumno(DNI, nombre, Apellido, Modulo);
                            Alumno.getAlumnos().add(A);
                        }
                        Descargado = true;
                    }

                });

    }

    public void descargarBdLocal(){
        Cursor cursor = db.query(DBHelper.entidadAlumnado.NOMBRE_TABLA,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String DNI = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_DNI));
            String Nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_NOMBRE));
            String Apellido = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_APELLIDO));
            String Modulo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_MODULO));
            Alumno A = new Alumno(DNI,Nombre,Apellido,Modulo);
            Alumno.getAlumnos().add(A);

        }
    }

    public void intentBDonline(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(Descargado){
                    Intent intent = new Intent(MainActivity.this,mostrarAlumnos.class);
                    startActivity(intent);
                }else{
                    intentBDonline();
                }
            }
        }, 500);
            }

    public void mezclarBases(){

        Cursor cursor = db.query(DBHelper.entidadAlumnado.NOMBRE_TABLA,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            boolean encontrado = false;
            String DNI = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_DNI));
            String Nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_NOMBRE));
            String Apellido = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_APELLIDO));
            String Modulo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_MODULO));
            for(int i = 0;Alumno.getAlumnos().size()>i;i++){
                if(Alumno.getAlumnos().get(i).DNI.equals(DNI)){
                    encontrado = true;
                }
            }
            if (!encontrado){
                Alumno A = new Alumno(DNI,Nombre,Apellido,Modulo);
                Alumno.getAlumnos().add(A);
            }
        }
        Mezclado = true;
    }

    public void intentMezcla(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(Mezclado || Descargado){
                    Intent intent = new Intent(MainActivity.this,mostrarAlumnos.class);
                    startActivity(intent);
                }else{
                    intentBDonline();
                }
            }
        }, 500);
    }



}
