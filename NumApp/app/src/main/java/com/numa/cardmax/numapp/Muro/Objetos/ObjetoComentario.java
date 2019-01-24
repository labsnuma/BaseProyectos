package com.numa.cardmax.numapp.Muro.Objetos;

public class ObjetoComentario {

    String id_publicacion;
    String id_usuario;
    String comentario;
    String tiempo;

    public ObjetoComentario(String id_publicacion, String id_usuario, String comentario, String tiempo) {
        this.id_publicacion = id_publicacion;
        this.id_usuario = id_usuario;
        this.comentario = comentario;
        this.tiempo = tiempo;
    }


    public String getId_publicacion() {
        return id_publicacion;
    }

    public void setId_publicacion(String id_publicacion) {
        this.id_publicacion = id_publicacion;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public  ObjetoComentario(){


    }



}
