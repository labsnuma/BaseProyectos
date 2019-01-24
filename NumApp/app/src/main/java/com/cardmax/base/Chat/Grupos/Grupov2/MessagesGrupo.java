package com.cardmax.base.Chat.Grupos.Grupov2;

public class MessagesGrupo {

    private String audio,from,ngrupo, mensaje, tipo, fecha,hora,foto,nombre,imagen;



    public MessagesGrupo() {

    }


    public String getNgrupo() {
        return ngrupo;
    }

    public void setNgrupo(String ngrupo) {
        this.ngrupo = ngrupo;
    }

    public MessagesGrupo(String audio, String from, String ngrupo, String mensaje, String tipo, String hora, String foto, String nombre, String imagen, String fecha) {
        this.audio = audio;
        this.from = from;
        this.ngrupo = ngrupo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.hora = hora;
        this.foto = foto;
        this.nombre = nombre;
        this.imagen = imagen;
        this.fecha = fecha;


    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
