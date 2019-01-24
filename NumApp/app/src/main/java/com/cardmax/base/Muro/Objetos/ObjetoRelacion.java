package com.cardmax.base.Muro.Objetos;

public class ObjetoRelacion {


    String id_publicacion;
    String id_user;
    String key;


    public ObjetoRelacion(String id_publicacion, String id_user, String key) {
        this.id_publicacion = id_publicacion;
        this.id_user = id_user;
        this.key = key;
    }

    public String getId_publicacion() {
        return id_publicacion;
    }

    public void setId_publicacion(String id_publicacion) {
        this.id_publicacion = id_publicacion;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ObjetoRelacion(){

    }
}
