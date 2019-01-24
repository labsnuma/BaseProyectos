package com.numa.cardmax.numapp.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoMuro;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoNotificacionChat;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoUser;
import com.numa.cardmax.numapp.R;

public class MuroServ extends Service {


    private FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    private String currentUserID = mAuth.getCurrentUser().getUid();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(),"Servicio iniciado",Toast.LENGTH_LONG).show();
        final DatabaseReference mDatabase;
        final int NOTIFICATION_ID = 13545613;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final Query topnoti = mDatabase.child("muro_publicaciones")
                .orderByChild("top")
                .equalTo(1);

        topnoti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                final String[] usuario = {""};
                final String[] titulo = {""};
                int contador = 0;
                for (DataSnapshot list_comentario : muroChildren) {

                    contador+=1;
                }

                final int[] finalContador = {contador};
                topnoti.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Iterable<DataSnapshot> muroChildren2 = dataSnapshot.getChildren();

                        int contador2 = 0;
                        for (DataSnapshot list_comentario : muroChildren2) {
                            ObjetoMuro p = list_comentario.getValue(ObjetoMuro.class);

                            usuario[0] =p.getId_usuario();
                            titulo[0] =p.getTitulo_publicacion();

                            contador2+=1;
                        }

                        if(finalContador[0] != contador2){


                           final Query usernombre = mDatabase.child("Users")
                                    .orderByChild("uid")
                                    .equalTo(usuario[0]).limitToLast(50);


                            final int finalContador1 = contador2;
                            usernombre.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();

                                    for (DataSnapshot numrelacion : relacion) {
                                        ObjetoUser us =  numrelacion.getValue(ObjetoUser.class);
                                        usuario[0] = us.name;

                                    }

                                    boolean show= true;
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                    builder.setSmallIcon(R.drawable.chat);
                                    builder.setContentTitle(usuario[0]+" !Publicacion Importante!");
                                    builder.setContentText(titulo[0]);
                                    builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                                    builder.setShowWhen(show);
                                    //Vibration
                                    builder.setVibrate(new long[] { 250, 250,   250, 250 });
                                    //LED
                                    builder.setLights(Color.WHITE, 1500, 1500);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    notificationManager.notify(NOTIFICATION_ID, builder.build());



                                    finalContador[0] = finalContador1;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });





                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final Query q = mDatabase.child("notification_user_chat")
                .child(currentUserID)
                ;

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                final String[] idusuario = {""};
                final String[] mensaje = {""};
                int count = 0;
                for (DataSnapshot list_comentario : muroChildren) {

                    count+=1;
                }

                final int[] finalCount = {count};


                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Iterable<DataSnapshot> muroChildren2 = dataSnapshot.getChildren();

                        int count2 = 0;
                        for (DataSnapshot list_comentario : muroChildren2) {

                                ObjetoNotificacionChat p = list_comentario.getValue(ObjetoNotificacionChat.class);

                            idusuario[0] =p.getUser();
                            mensaje[0] =p.getMensage();

                            count2+=1;


                        }

                        if(finalCount[0] != count2){


                            final Query username = mDatabase.child("Users")
                                    .orderByChild("uid")
                                    .equalTo(idusuario[0]).limitToLast(50);


                            final int finalCount1 = count2;
                            username.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Iterable<DataSnapshot> relacion = dataSnapshot.getChildren();

                                    for (DataSnapshot numrelacion : relacion) {
                                        ObjetoUser us =  numrelacion.getValue(ObjetoUser.class);
                                        idusuario[0] = us.name;

                                    }

                                    boolean show= true;
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                    builder.setSmallIcon(R.drawable.chat);
                                    builder.setContentTitle(idusuario[0]);
                                    builder.setContentText(mensaje[0]);
                                    builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                                    builder.setShowWhen(show);
                                    //Vibration
                                    builder.setVibrate(new long[] { 250, 250,   250, 250 });
                                    //LED
                                    builder.setLights(Color.WHITE, 1500, 1500);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    notificationManager.notify(NOTIFICATION_ID, builder.build());



                                    finalCount[0] = finalCount1;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });





                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        return START_STICKY;




    }



    @Override
    public void onDestroy() {

    }
}
