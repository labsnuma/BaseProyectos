package com.cardmax.base.Muro.Objetos;

public class ObjetoChat {




    private String fecha;
    private String from;
    private String hora;
    private String mensaje;
    private String tipo;
    private String visto;
    private String key;
    private String answer_mensagge;
    private String answer_multimedia;
    private String answer_user;

    public ObjetoChat(String fecha, String from, String hora, String mensaje, String tipo, String visto, String key, String answer_mensagge, String answer_multimedia, String answer_user) {
        this.fecha = fecha;
        this.from = from;
        this.hora = hora;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.visto = visto;
        this.key = key;
        this.answer_mensagge = answer_mensagge;
        this.answer_multimedia = answer_multimedia;
        this.answer_user = answer_user;
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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
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

    public String getAnswer_mensagge() {
        return answer_mensagge;
    }

    public void setAnswer_mensagge(String answer_mensagge) {
        this.answer_mensagge = answer_mensagge;
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
}
