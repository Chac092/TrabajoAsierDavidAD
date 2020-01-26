package com.example.trabajoasierdavidad;

import java.util.ArrayList;

public class Alumno {
    String DNI;
    String Nombre;
    String Apellido;

    public static ArrayList<Alumno> getAlumnos() {
        return alumnos;
    }

    public static void setAlumnos(ArrayList<Alumno> alumnos) {
        Alumno.alumnos = alumnos;
    }

    public static ArrayList <Alumno> alumnos = new ArrayList<>();

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getModulo() {
        return Modulo;
    }

    public void setModulo(String modulo) {
        Modulo = modulo;
    }

    String Modulo;

    public Alumno(String dni,String Nom, String Ape,String Mod){
        DNI = dni;
        Nombre = Nom;
        Apellido = Ape;
        Modulo = Mod;
    }
}

