package com.numa.cardmax.numapp.Muro.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.numa.cardmax.numapp.Chat.MensajesChats.Messages;
import com.numa.cardmax.numapp.Muro.MuroMainActivity;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoChat;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoNotificacionChat;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoUser;
import com.numa.cardmax.numapp.R;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdaptadorShare extends RecyclerView.Adapter<ViewHolderShare> {

    private List<String> listaObjeto;
    private DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference();
    private String fotoimg, name, estado, keypublicacion, fecha_publicacion;
    private ObjetoChat mensaje;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private String currentUserID= mAuth.getCurrentUser().getUid();



    public AdaptadorShare(List<String> listaObjeto) {
        this.listaObjeto = listaObjeto;
    }

    @NonNull
    @Override
    public ViewHolderShare onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.muro_layout_share, parent, false);
        return new ViewHolderShare(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderShare holder, final int position) {


        System.out.println(position);
        if(position == listaObjeto.size()-1){
            keypublicacion = listaObjeto.get(position);
            holder.layout.setVisibility(View.GONE);


        } else {

            holder.layout.setVisibility(View.VISIBLE);

            String id_user = listaObjeto.get(position);

            Query user = mDatabase.child("Users")
                    .orderByChild("uid")
                    .equalTo(id_user);

            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();


                    for (DataSnapshot murox : muroChildren) {

                        name = murox.child("name").getValue().toString();
                        estado =  murox.child("status").getValue().toString();
                        fotoimg = murox.child("image").getValue().toString();
                        holder.nombre.setText(name);
                        holder.estado.setText(estado);
                        Glide.with(holder.perfil.getContext())
                                .load(fotoimg)
                                .into(holder.perfil);


                    }

                    System.out.println(name);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            holder.enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(keypublicacion.indexOf("Reenviado:publicacion ") != -1){

                        String mensajex;
                        mensajex = keypublicacion.replace("Reenviado:publicacion ","");
                        insertdb(holder,position,mensajex,"publicacion");



                    }else if(keypublicacion.indexOf("Reenviado:texto ") != -1){

                        String mensajex;
                        mensajex = keypublicacion.replace("Reenviado:texto ","");
                        insertdb(holder,position,mensajex,"texto");






                    }else if(keypublicacion.indexOf("Reenviado:audio ") != -1){

                        String mensajex;
                        mensajex = keypublicacion.replace("Reenviado:audio ","");
                        insertdb(holder,position,mensajex,"audio");






                    }else if(keypublicacion.indexOf("Reenviado:imagen ") != -1){

                        String mensajex;
                        mensajex = keypublicacion.replace("Reenviado:imagen ","");
                        insertdb(holder,position,mensajex,"imagen");






                    }else {


                        insertdb(holder,position,keypublicacion,"publicacion");









                    }

                }
            });
        }





    }


    public void insertdb(ViewHolderShare holder, int position, String mensajex, String tipo){

        String id ;
        id = mDatabase.push().getKey();
        Date currentTime = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        fecha_publicacion = DateFormat.getDateInstance().format(c.getTime());

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        String dateString = dateFormat.format(new Date()).toString();


        if(tipo == "audio"){

            Messages mensajeaudio = new Messages(mensajex, fecha_publicacion, currentUserID, "", "", tipo, dateString, "", "0", id, "", "", "");

            mDatabase.child("Chat Mensajes").child(currentUserID).child(listaObjeto.get(position)).child(id).setValue(mensajeaudio);
            mDatabase.child("Chat Mensajes").child(listaObjeto.get(position)).child(currentUserID).child(id).setValue(mensajeaudio);



            ObjetoNotificacionChat registro = new ObjetoNotificacionChat(currentUserID, "Envio una publicacion");
            mDatabase.child("notification_user_chat").child(listaObjeto.get(position)).child(id).setValue(registro);


            Toast.makeText(holder.enviar.getContext(), "Publicacion Enviada", Toast.LENGTH_SHORT).show();




        }else if(tipo == "imagen"){

            Messages mensajeaudio = new Messages("", fecha_publicacion, currentUserID, "", "", tipo, dateString, mensajex, "0", id, "", "", "");

            mDatabase.child("Chat Mensajes").child(currentUserID).child(listaObjeto.get(position)).child(id).setValue(mensajeaudio);
            mDatabase.child("Chat Mensajes").child(listaObjeto.get(position)).child(currentUserID).child(id).setValue(mensajeaudio);



            ObjetoNotificacionChat registro = new ObjetoNotificacionChat(currentUserID, "Envio una publicacion");
            mDatabase.child("notification_user_chat").child(listaObjeto.get(position)).child(id).setValue(registro);


            Toast.makeText(holder.enviar.getContext(), "Publicacion Enviada", Toast.LENGTH_SHORT).show();




        }else{

            mensaje = new ObjetoChat(fecha_publicacion,currentUserID,dateString,mensajex,tipo,"0",id,"","","");

            mDatabase.child("Chat Mensajes").child(currentUserID).child(listaObjeto.get(position)).child(id).setValue(mensaje);
            mDatabase.child("Chat Mensajes").child(listaObjeto.get(position)).child(currentUserID).child(id).setValue(mensaje);



            ObjetoNotificacionChat registro = new ObjetoNotificacionChat(currentUserID, "Envio una publicacion");
            mDatabase.child("notification_user_chat").child(listaObjeto.get(position)).child(id).setValue(registro);


            Toast.makeText(holder.enviar.getContext(), "Publicacion Enviada", Toast.LENGTH_SHORT).show();




        }










    }




    @Override
    public int getItemCount() {
        return listaObjeto.size();
    }
}
