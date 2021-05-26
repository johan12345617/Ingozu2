package com.example.ingozu2.ui.Cajas;

public class Cajas {

    private int nivel;
    private String nombre;
    private String urlFoto;
    private String Descripcion;

    public Cajas(){
        this.setNivel(0);
        this.setNombre("");
        this.setUrlFoto("");
        this.setDescripcion("");
    }

    public Cajas(int nivel, String nombre,String urlFoto,String descripcion){
        this.setNivel(nivel);
        this.setNombre(nombre);
       this.setUrlFoto(urlFoto);
       this.setDescripcion(descripcion);
    }


    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
