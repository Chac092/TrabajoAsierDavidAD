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

//En esta activity lo que haremos sera mostrar 4 botones que seran las opciones que de el menu
//Estos botones descargaran los datos que necesiten y rellenaran un arraylist para mas tarde mostrarlo en la activity que abrira
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
        //Este boton corresponde a descargar la base local
        Local = findViewById(R.id.enseñarLocal);
        Local.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Alumno.getAlumnos().clear();//Limpiamos el array para que no se dupliquen datos
                 descargarBdLocal();
                 //Llamamos a la siguiente activity con todos los datos cargados en el array
                 Intent intent = new Intent(MainActivity.this,mostrarAlumnos.class);
                 startActivity(intent);
             }
         });
        //Este boton corresponde a descargar la base online
        Online = findViewById(R.id.enseñarOnline);
        Online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alumno.getAlumnos().clear();//Limpiamos el array para que no se dupliquen datos
                Toast.makeText(getApplicationContext(),"Descargando. Porfavor espere.",Toast.LENGTH_LONG).show();//Ponemos un toast ya que la base de datos tarda apenas un segundo en abrir
                //Llamamos a la siguiente activity con todos los datos cargados en el array
                descargarBdOnline();
                intentBDonline();

            }
        });
        //Este boton corresponde a mezclar ambas bases de datos
        Mezcla = findViewById(R.id.enseñarMezcla);
        Mezcla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alumno.getAlumnos().clear();//Limpiamos el array para que no se dupliquen datos
                Toast.makeText(getApplicationContext(),"Mezclando bases de datos. Porfavor espere.",Toast.LENGTH_LONG).show();//Ponemos un toast ya que la base de datos tarda apenas un segundo en abrir
                descargarBdOnline();
                mezclarBases();
                intentMezcla();
            }
        });
        //Este boton corresponde a insertar
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
    //Con este metodo nos descargaremos la base de datos online y la insertaremos en el arraylist
    public void descargarBdOnline(){
        //descargamos la base
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
                            Alumno.getAlumnos().add(A);//Añadimos el Alumno al arraylist
                        }
                        Descargado = true;//Ponemos la variable descargado en true ya que la necesitaremos despues
                    }

                });

    }
    //Con este metodo nos descargaremos la base de datos local y la insertaremos en el arraylist
    public void descargarBdLocal(){
        //descargamos la base
        Cursor cursor = db.query(DBHelper.entidadAlumnado.NOMBRE_TABLA,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String DNI = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_DNI));
            String Nombre = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_NOMBRE));
            String Apellido = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_APELLIDO));
            String Modulo = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.entidadAlumnado.COLUMNA_MODULO));
            Alumno A = new Alumno(DNI,Nombre,Apellido,Modulo);
            Alumno.getAlumnos().add(A);//Añadimos el Alumno al arraylist

        }
    }
    //Este metodo lo que hace es comprobar si la base de datos online esta cargada. Si es asi
    //lo que hara sera llevarnos a la siguiente activity que nos mostrara todos los elementos de la BD
    //En caso de que no sea asi esperara 500 milisegundos y volvera a comprobar si la base esta descargada
    public void intentBDonline(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(Descargado){//con la variable Descargado anteriormente puesta en true comprobaremos si la bd se ha descargado
                    Intent intent = new Intent(MainActivity.this,mostrarAlumnos.class);
                    startActivity(intent);//Entramos en la siguiente activity
                }else{
                    intentBDonline();
                }
            }
        }, 500);//Los 500 milisegundos de espera
            }
    //En este metodo lo que haremos sera mezclar las bases de datos y insertar los datos de ambas en el arraylist
    public void mezclarBases(){
        //Aqui lo que hacemos es descargarnos la base de datos local para ir comprobando a traves del DNI si el alumno existe o no en caso de que sea asi no pasa nada
        //de lo contrario lo insertara en el arraylist formando asi un arraylist con ambas bases de datos
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
    //Este metodo sirve para mirar si la mezcla de las bases de datos ha acabado y si es asi llevarnos a la siguiente activity
    public void intentMezcla(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(Mezclado && Descargado){//Comprobamos si esta la base de datos online descargada y si esta mezclada
                    Intent intent = new Intent(MainActivity.this,mostrarAlumnos.class);
                    startActivity(intent);//Abrimos la siguiente activity
                }else{
                    intentBDonline();
                }
            }
        }, 500);//El retraso de 500 milisegundos
    }



}
