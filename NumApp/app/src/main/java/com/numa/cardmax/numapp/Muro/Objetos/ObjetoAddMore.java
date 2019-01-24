package com.numa.cardmax.numapp.Muro.Objetos;

public class ObjetoAddMore {


    String id_publicacion;
    String key_detais;
    String contenidotxt;
    String contenidovisual;

    public ObjetoAddMore(String id_publicacion, String key_detais, String contenidotxt, String contenidovisual) {
        this.id_publicacion = id_publicacion;
        this.key_detais = key_detais;
        this.contenidotxt = contenidotxt;
        this.contenidovisual = contenidovisual;
    }

    public String getId_publicacion() {
        return id_publicacion;
    }

    public void setId_publicacion(String id_publicacion) {
        this.id_publicacion = id_publicacion;
    }

    public String getKey_detais() {
        return key_detais;
    }

    public void setKey_detais(String key_detais) {
        this.key_detais = key_detais;
    }

    public String getContenidotxt() {
        return contenidotxt;
    }

    public void setContenidotxt(String contenidotxt) {
        this.contenidotxt = contenidotxt;
    }

    public String getContenidovisual() {
        return contenidovisual;
    }

    public void setContenidovisual(String contenidovisual) {
        this.contenidovisual = contenidovisual;
    }

    public  ObjetoAddMore(){


    }

}
