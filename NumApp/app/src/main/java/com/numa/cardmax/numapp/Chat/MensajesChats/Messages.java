package com.numa.cardmax.numapp.Chat.MensajesChats;

public class Messages {

    private String audio,fecha,from, mensaje,nombre, tipo, hora,imagen, visto, key, answer_multimedia, answer_user, answer_mensagge;



    public Messages() {

    }

    public Messages(String audio, String fecha, String from, String mensaje, String nombre, String tipo, String hora, String imagen, String visto, String key, String answer_multimedia, String answer_user, String answer_mensagge) {
        this.audio = audio;
        this.fecha = fecha;
        this.from = from;
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.tipo = tipo;
        this.hora = hora;
        this.imagen = imagen;
        this.visto = visto;
        this.key = key;
        this.answer_multimedia = answer_multimedia;
        this.answer_user = answer_user;
        this.answer_mensagge = answer_mensagge;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVisto() {
        return visto;
    }

    public void setVisto(String visto) {
        this.visto = visto;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAnswer_multimedia() {
        return answer_multimedia;
    }

    public void setAnswer_multimedia(String answer_multimedia) {
        this.answer_multimedia = answer_multimedia;
    }

    public String getAnswer_user() {
        return answer_user;
    }

    public void setAnswer_user(String answer_user) {
        this.answer_user = answer_user;
    }

    public String getAnswer_mensagge() {
        return answer_mensagge;
    }

    public void setAnswer_mensagge(String answer_mensagge) {
        this.answer_mensagge = answer_mensagge;
    }
}
