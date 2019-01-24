package com.numa.cardmax.numapp.Chat.Recursos;

import android.app.Activity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EstadoOnline extends Activity {

    private DatabaseReference RootRef;
    FechaHora calendario = new FechaHora();

    public void actualizarStatus(String estado,String mAuth1) {

        RootRef = FirebaseDatabase.getInstance().getReference();

        calendario.getHora();
        String fecha = calendario.getGuardarFecha1();
        String hora = calendario.getGuardarhora1();

        HashMap<String, Object> onlineEstado = new HashMap<>();
        onlineEstado.put("hora", hora);
        onlineEstado.put("fecha", fecha);
        onlineEstado.put("estado", estado);

        RootRef.child("Users").child(mAuth1).child("estadoUsuario")
                .updateChildren(onlineEstado);
    }

    public void actualizarStatusAmigos(String estado,String mAuth1,String mSender) {

        RootRef = FirebaseDatabase.getInstance().getReference();

        calendario.getHora();
        String fecha = calendario.getGuardarFecha1();
        String hora = calendario.getGuardarhora1();

        HashMap<String, Object> onlineEstado = new HashMap<>();
        onlineEstado.put("hora", hora);
        onlineEstado.put("fecha", fecha);
        onlineEstado.put("estado", estado);

        RootRef.child("Contactos").child(mAuth1).child(mSender).child("estadoUsuario")
                .updateChildren(onlineEstado);
    }

}
