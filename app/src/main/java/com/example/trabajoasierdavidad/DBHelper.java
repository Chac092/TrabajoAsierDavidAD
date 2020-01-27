package com.example.trabajoasierdavidad;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;
//En esta clase lo que hacemos es crear la base de datos de los alumnos
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TrabajoAseierDavidAD.db";
    //Creamos los parametros que tendra la tabla, asi como su nombre y atributos
    public static class entidadAlumnado implements BaseColumns{
        public static final String NOMBRE_TABLA = "Alumnos";
        public static final String COLUMNA_DNI = "Dni" ;
        public static final String COLUMNA_NOMBRE="Nombre";
        public static final String COLUMNA_APELLIDO="Apellido";
        public static final String COLUMNA_MODULO="Modulo";
    }

    //Esta sera la sentencia que cree la tabla anteriormente indicada
    private static final String SQL_CREAR_TABLA_ALUMNO =
            "CREATE TABLE " + entidadAlumnado.NOMBRE_TABLA + " (" +
                    entidadAlumnado.COLUMNA_DNI + " TEXT PRIMARY KEY," +
                    entidadAlumnado.COLUMNA_NOMBRE + " TEXT," +
                    entidadAlumnado.COLUMNA_APELLIDO + " TEXT," +
                    entidadAlumnado.COLUMNA_MODULO + " TEXT)";
    //Esta sentencia borrara la tabla
    private static final String SQL_BORRAR_TABLA_ALUMNO =
            "DROP TABLE IF EXISTS " + entidadAlumnado.NOMBRE_TABLA;

    //Aqui lo que haremos sera rellenar la tabla creada para que no este vacia
    public void crearAlumnos(SQLiteDatabase db){
        //En este caso ponemos un for para que nos cree 5 alumnos
        for(int i =0; i<5;i++){
            String Nombre = "Alumno" +i;
            String Apellido = "Apellido"+i;
            String DNI = ""+i+""+""+i+""+""+i+""+""+i+""+i+""+i+""+i+""+"P";
            String Modulo = "Modulo"+i;

            ContentValues valores = new ContentValues();
            valores.put(entidadAlumnado.COLUMNA_DNI, DNI);
            valores.put(entidadAlumnado.COLUMNA_NOMBRE,Nombre);
            valores.put(entidadAlumnado.COLUMNA_APELLIDO,Apellido);
            valores.put(entidadAlumnado.COLUMNA_MODULO,Modulo);
            //Insertamos el registro en la base de datos
            db.insert(entidadAlumnado.NOMBRE_TABLA, null, valores);
        }
    }
    //Aqui lo que haremos sera indicar las propiedades del DBHelper
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //Aqui lo que ejecutaremos sera para crear la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR_TABLA_ALUMNO);
        crearAlumnos(db);
    }
    //Aqui lo que ejecutaremos sera para actualizar esta base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_BORRAR_TABLA_ALUMNO);
        onCreate(db);
    }

}
