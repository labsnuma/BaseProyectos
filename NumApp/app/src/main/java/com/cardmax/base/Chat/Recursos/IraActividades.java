package com.cardmax.base.Chat.Recursos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.cardmax.base.Chat.ContactosAceptados.ContactosActivity;
import com.cardmax.base.Chat.InicioActivity;

import com.cardmax.base.Perfil.OpcionesActivity;

import com.cardmax.base.Chat.NotifiacionesActivity;
import com.cardmax.base.Perfil.OpcionesActivity;

import com.cardmax.base.Chat.UsuariosB.BuscarAmigosActivity;
import com.cardmax.base.Muro.MuroMainActivity;

public class IraActividades extends AppCompatActivity {

    public void iraContactos(Context context) {
        Intent Contactosintent = new Intent(context, ContactosActivity.class);
        context.startActivity(Contactosintent);
    }

    public void iraBuscarAmigosActivity(Context context) {
        Intent BuscarAIntent = new Intent(context, BuscarAmigosActivity.class);
        context.startActivity(BuscarAIntent);
    }

    public void iraOpcionesActivity(Context context) {
        Intent OpcionesIntent = new Intent(context, OpcionesActivity.class);
        context.startActivity(OpcionesIntent);
    }

    public void iraChatActivity(Context context) {
        Intent ChatIntent = new Intent(context, InicioActivity.class);
        context.startActivity(ChatIntent);
    }

    public void iraMuroActivity(Context context) {
        Intent MuroIntent = new Intent(context, MuroMainActivity.class);
        context.startActivity(MuroIntent);
    }

    public void iraFotos(Context context) {
        Intent FotoIntent = new Intent(context, SelectorImagenes.class);
        context.startActivity(FotoIntent);
    }
    public void iraImagen(Context context) {
        Intent FotoIntent = new Intent(context, RSelectorImagenes.class);
        context.startActivity(FotoIntent);
    }
    public void iraNotificaciones(Context context) {
        Intent Notificaciones = new Intent(context, NotifiacionesActivity.class);
        context.startActivity(Notificaciones);
    }

}
