package com.numa.cardmax.numapp.Muro.Objetos;

public class ObjetoNotificacionChat {


    String user;
    String mensage;

    public ObjetoNotificacionChat(String user, String mensage) {
        this.user = user;
        this.mensage = mensage;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMensage() {
        return mensage;
    }

    public void setMensage(String mensage) {
        this.mensage = mensage;
    }

    public ObjetoNotificacionChat(){

    }

}
