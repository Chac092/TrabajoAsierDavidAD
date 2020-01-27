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

//Esta activity tiene 2 funcionalidades
    //La primera es mostrar la informacion del usuario que han clickado en el Recycler
    //La segunda es insertar el usuario

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
        //Damos a los atributos sus correspondientes elementos
        Nombre = findViewById(R.id.editNombre2);
        Apellido = findViewById(R.id.editApellido2);
        DNI = findViewById(R.id.editDNI2);
        Modulo = findViewById(R.id.editModulo2);
        Insertar = findViewById(R.id.insercion);
        Intent intent = getIntent();//Recibimos el intent de la activity de la que hemos venido
        //Recojemos la informacion de este intent
        Origen = intent.getStringExtra("origen");
        alumnoElejido = intent.getIntExtra("Alumno",-1);
        //comprobamos su origen
        comprobarOrigen();
        //Ponemos el listener de insertar usuario
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
        //Ponemos un listener que nos devolvera a la pantalla anterior
        Atras = findViewById(R.id.atras);
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertaroMostarAlumno.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Aqui lo que hacemos es insertar al usuario en la base de datos
    public void Insercion(){
        //Insertamos el usuario
        dbHelper = new DBHelper(getBaseContext());
        db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(DBHelper.entidadAlumnado.COLUMNA_DNI, DNI.getText().toString());
        valores.put(DBHelper.entidadAlumnado.COLUMNA_NOMBRE,Nombre.getText().toString());
        valores.put(DBHelper.entidadAlumnado.COLUMNA_APELLIDO,Apellido.getText().toString());
        valores.put(DBHelper.entidadAlumnado.COLUMNA_MODULO,Modulo.getText().toString());
        //Insertamos el registro en la base de datos
        db.insert(DBHelper.entidadAlumnado.NOMBRE_TABLA, null, valores);
        //Avisamos de que ha finalizado correctamente
        Toast.makeText(getApplicationContext(),"Insertado correctamente",Toast.LENGTH_LONG).show();
    }
    //Aqui lo que hacemos es comprobar si el Alumno a insertar se encuentra en la base de datos local
    public void comprobarLocal(){
        //Comprobamos si esta
        Local = false;//Esta variable la utilizaremos despues para el mensaje final
        dbHelper = new DBHelper(getBaseContext());
        db = dbHelper.getWritableDatabase();
        String selection = DBHelper.entidadAlumnado.COLUMNA_DNI + " = ?" ;
        String[] selectionArgs = {DNI.getText().toString()};
        Cursor cursor = db.query(DBHelper.entidadAlumnado.NOMBRE_TABLA,null,selection,selectionArgs,null,null,null);
        if (cursor.getCount()>0){
            Local = true;//Si el Alumno se encuentra en la base de datos lo indicamos en esta variable
        }

    }
    //Aqui comprobaremos si el Alumno se encuentra en la Bd online
    public void pedirOnline(){
        //pedimos los datos
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        docRef = db.collection("Alumnos")
                .whereEqualTo("DNI", DNI.getText().toString())
                .get();
        descarga = true;//indicamos que los datos se han descargado
    }
    //Una vez que los datos pedidos a la BD online se carguen comprobaremos si el Alumno existe
    public void comprobacionOnline(){
        Online = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(descarga&&docRef.isComplete()) {
                    if(!docRef.getResult().isEmpty()){
                        Online = true;//En caso de que exista lo indicamos
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
    //Una vez comprobadas las dos Bases de datos lo que haremos sera indicar en que base se encuentran
    public void comprobacionFinal(){
        //Para indicar en que base de datos se encuentran usaremos las variables boolean que hemos ido rellenando anteriormente
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
    //Aqui lo que hacemos es comprobar de donde venimos depende de la activity tendremos que hacer una serie de cosas
    public void comprobarOrigen(){
        //En caso de que vengamos del Recycler lo que haremos sera poner los editText en false ya que solo vamos a mostrar y no se podra editar
        //Tambien cargaremos los datos de ese alumno
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
    }
}
