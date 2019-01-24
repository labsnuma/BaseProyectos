package com.numa.cardmax.numapp.Chat.Recursos;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FechaHora {

    private String guardarhora1, guardarFecha1, guardarhora2, guardarFecha2, guardarhora3;
    private Calendar calendar = Calendar.getInstance();


    public void getHora() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fechaActual = new SimpleDateFormat("MMM dd,yyyy");
        guardarFecha1 = fechaActual.format(calendar.getTime()).toUpperCase();
        setGuardarFecha1(guardarFecha1);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat horaAcutal = new SimpleDateFormat("hh:mm a");
        guardarhora1 = horaAcutal.format(calendar.getTime())
                .toUpperCase()
                .replace(".", "")
                .replace("M", "")
                .replace("A", "AM")
                .replace("P", "PM");
        setGuardarhora1(guardarhora1);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fechaActua2 = new SimpleDateFormat("MM dd, yyyy");
        guardarFecha2 = fechaActua2.format(calendar.getTime());
        setGuardarFecha2(guardarFecha2);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat horaAcutal2 = new SimpleDateFormat("HH:mm:ss");
        guardarhora2 = horaAcutal2.format(calendar.getTime());
        setGuardarhora2(guardarhora2);


        @SuppressLint("SimpleDateFormat") SimpleDateFormat horaAcutal3 = new SimpleDateFormat("dd-MM-yyy_HH-mm-ss");
        guardarhora3 = horaAcutal3.format(calendar.getTime());
        int mili = calendar.get(Calendar.MILLISECOND);
        setGuardarhora3(guardarhora3 + "-" + mili);


    }

    public String getGuardarhora1() {
        return guardarhora1;
    }

    public void setGuardarhora1(String guardarhora1) {
        this.guardarhora1 = guardarhora1;
    }

    public String getGuardarFecha1() {
        return guardarFecha1;
    }

    public void setGuardarFecha1(String guardarFecha1) {
        this.guardarFecha1 = guardarFecha1;
    }

    public String getGuardarhora2() {
        return guardarhora2;
    }

    public void setGuardarhora2(String guardarhora2) {
        this.guardarhora2 = guardarhora2;
    }

    public String getGuardarFecha2() {
        return guardarFecha2;
    }

    public void setGuardarFecha2(String guardarFecha2) {
        this.guardarFecha2 = guardarFecha2;
    }

    public String getGuardarhora3() {
        return guardarhora3;
    }

    public void setGuardarhora3(String guardarhora3) {
        this.guardarhora3 = guardarhora3;
    }
}
