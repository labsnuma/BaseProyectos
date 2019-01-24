package com.cardmax.base.Muro.Objetos;

public class ObjetoSaveCard {

    public String id_user;
    public String id_publicacion;


    public ObjetoSaveCard(String id_user, String id_publicacion) {
        this.id_user = id_user;
        this.id_publicacion = id_publicacion;
    }


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_publicacion() {
        return id_publicacion;
    }

    public void setId_publicacion(String id_publicacion) {
        this.id_publicacion = id_publicacion;
    }

    public ObjetoSaveCard(){

    }



}
