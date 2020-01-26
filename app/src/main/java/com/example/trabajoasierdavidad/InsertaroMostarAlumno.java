package com.example.trabajoasierdavidad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class InsertaroMostarAlumno extends AppCompatActivity {
    String Origen;
    EditText Nombre;
    EditText Apellido;
    EditText DNI;
    EditText Modulo;
    Button Insertar;
    Button Atras;
    DBHelper dbHelper;
    SQLiteDatabase db;
    boolean Local = false;
    boolean Online = false;
    int alumnoElejido;
    boolean descarga = false;
    boolean Listo=false;
    Task<QuerySnapshot> docRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_alumno);
        Nombre = findViewById(R.id.editNombre2);
        Apellido = findViewById(R.id.editApellido2);
        DNI = findViewById(R.id.editDNI2);
        Modulo = findViewById(R.id.editModulo2);
        Insertar = findViewById(R.id.insercion);
        Intent intent = getIntent();
        Origen = intent.getStringExtra("origen");
        alumnoElejido = intent.getIntExtra("Alumno",-1);
        if (Origen.equals("Visualizar")) {
            DNI.setFocusable(false);
            DNI.setText(Alumno.getAlumnos().get(alumnoElejido).DNI);
            Nombre.setFocusable(false);
            Nombre.setText(Alumno.getAlumnos().get(alumnoElejido).Nombre);
            Apellido.setFocusable(false);
            Apellido.setText(Alumno.getAlumnos().get(alumnoElejido).Apellido);
            Modulo.setFocusable(false);
            Modulo.setText(Alumno.getAlumnos().get(alumnoElejido).Modulo);
            Insertar.setVisibility(View.INVISIBLE);
        }
        Insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Comprobando si el usuario existe. Espere Porfavor",Toast.LENGTH_LONG).show();
                comprobarLocal();
                pedirOnline();
                comprobacionOnline();
                comprobacionFinal();
            }
        });

        Atras = findViewById(R.id.atras);
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertaroMostarAlumno.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void Insercion(){
        dbHelper = new DBHelper(getBaseContext());
        db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(DBHelper.entidadAlumnado.COLUMNA_DNI, DNI.getText().toString());
        valores.put(DBHelper.entidadAlumnado.COLUMNA_NOMBRE,Nombre.getText().toString());
        valores.put(DBHelper.entidadAlumnado.COLUMNA_APELLIDO,Apellido.getText().toString());
        valores.put(DBHelper.entidadAlumnado.COLUMNA_MODULO,Modulo.getText().toString());
        //Insertamos el registro en la base de datos
        db.insert(DBHelper.entidadAlumnado.NOMBRE_TABLA, null, valores);

        Toast.makeText(getApplicationContext(),"Insertado correctamente",Toast.LENGTH_LONG).show();
    }

    public void comprobarLocal(){
        Local = false;
        dbHelper = new DBHelper(getBaseContext());
        db = dbHelper.getWritableDatabase();
        String selection = DBHelper.entidadAlumnado.COLUMNA_DNI + " = ?" ;
        String[] selectionArgs = {DNI.getText().toString()};
        Cursor cursor = db.query(DBHelper.entidadAlumnado.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);
        if (cursor.getCount()>0){
            Local = true;
        }

    }

    public void pedirOnline(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        docRef = db.collection("Alumnos")
                .whereEqualTo("DNI", DNI.getText().toString())
                .get();
        descarga = true;
    }

    public void comprobacionOnline(){
        Online = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(descarga&&docRef.isComplete()) {
                    if(!docRef.getResult().isEmpty()){
                        Online = true;
                    }

                }else{
                    comprobacionOnline();
                }
                if (docRef.isComplete()){
                    Listo = true;
                }


            }

        }, 500);

    }

    public void comprobacionFinal(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(Listo) {
                    if (Local && Online) {
                        Toast.makeText(getApplicationContext(), "Este usuario se encuentra tanto en la BD Online como en la BD local.", Toast.LENGTH_LONG).show();
                    } else if (Online) {
                        Toast.makeText(getApplicationContext(), "Este usuario se encuentra en la BD Online.", Toast.LENGTH_LONG).show();
                    } else if (Local) {
                        Toast.makeText(getApplicationContext(), "Este usuario se encuentra en la BD local.", Toast.LENGTH_LONG).show();
                    } else {
                        Insercion();
                    }
                }else{
                    comprobacionFinal();
                }
            }
        },500);

}
}
