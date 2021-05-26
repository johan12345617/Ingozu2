package com.example.ingozu2.ui.Herramientas;

public class Herramientas {

    private int cantidad;
    private String descripcion;
    private String idCaja;
    private String nombre;
    private String urlFoto;

    public Herramientas(){
        this.nombre = "";
        this.urlFoto = "";
        this.cantidad=0;
        this.descripcion="";
        this.idCaja="";
    }


    public Herramientas(String nombre, int cantidad, String descripcion,
                 String idCaja, String urlFoto){

        this.nombre = nombre;
        this.urlFoto = urlFoto;
        this.cantidad=cantidad;
        this.descripcion=descripcion;
        this.idCaja=idCaja;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(String idCaja) {
        this.idCaja = idCaja;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

}
