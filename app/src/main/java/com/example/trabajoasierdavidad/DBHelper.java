package com.example.trabajoasierdavidad;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TrabajoAseierDavidAD.db";

    public static class entidadAlumnado implements BaseColumns{
        public static final String NOMBRE_TABLA = "Alumnos";
        public static final String COLUMNA_DNI = "Dni" ;
        public static final String COLUMNA_NOMBRE="Nombre";
        public static final String COLUMNA_APELLIDO="Apellido";
        public static final String COLUMNA_MODULO="Modulo";
    }


    private static final String SQL_CREAR_TABLA_ALUMNO =
            "CREATE TABLE " + entidadAlumnado.NOMBRE_TABLA + " (" +
                    entidadAlumnado.COLUMNA_DNI + " TEXT PRIMARY KEY," +
                    entidadAlumnado.COLUMNA_NOMBRE + " TEXT," +
                    entidadAlumnado.COLUMNA_APELLIDO + " TEXT," +
                    entidadAlumnado.COLUMNA_MODULO + " TEXT)";

    private static final String SQL_BORRAR_TABLA_ALUMNO =
            "DROP TABLE IF EXISTS " + entidadAlumnado.NOMBRE_TABLA;


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR_TABLA_ALUMNO);
        crearAlumnos(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_BORRAR_TABLA_ALUMNO);
        onCreate(db);
    }
    public void crearAlumnos(SQLiteDatabase db){
        for(int i =0; i<5;i++){
            String Nombre = "Alumno" +i;
            String Apellido = "Apellido"+i;
            String DNI = ""+i+""+""+i+""+""+i+""+""+i+""+i+""+i+""+i+""+"P";
            String Modulo = "Modulo"+i;
            Alumno A = new Alumno(DNI,Nombre,Apellido,Modulo);

            ContentValues valores = new ContentValues();
            valores.put(entidadAlumnado.COLUMNA_DNI, DNI);
            valores.put(entidadAlumnado.COLUMNA_NOMBRE,Nombre);
            valores.put(entidadAlumnado.COLUMNA_APELLIDO,Apellido);
            valores.put(entidadAlumnado.COLUMNA_MODULO,Modulo);
            //Insertamos el registro en la base de datos
            db.insert(entidadAlumnado.NOMBRE_TABLA, null, valores);
        }
    }
}
