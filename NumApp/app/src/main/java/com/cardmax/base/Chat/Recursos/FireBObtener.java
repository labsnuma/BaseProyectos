package com.cardmax.base.Chat.Recursos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireBObtener extends AppCompatActivity {

   public String currentUserID, currentUserName, currentUserfoto;



    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private  DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
    public String currentUserIDs = mAuth.getCurrentUser().getUid();

    private DatabaseReference ChatMensajesRef = FirebaseDatabase.getInstance().getReference().child("Chat Mensajes");
    private   StorageReference AudioChatMensajesRef = FirebaseStorage.getInstance().getReference().child("Chat audios");

  //  private AlertDialog dialogo;

  //  public void ObtenerUserInfo(final Context context, final Class clase) {
    public void ObtenerUserInfo(final Context context ){
        UserRef.child(currentUserIDs).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                    currentUserID = dataSnapshot.child("uid").getValue().toString();
                    currentUserfoto = dataSnapshot.child("image").getValue().toString();

                    /*Intent intent = new Intent(FireBObtener.this, clase);

                    Bundle bundle = new Bundle();
                    bundle.putString("foto", currentUserfoto);

                    intent.putExtras(bundle);startActivity(intent);*/

                    SharedPreferences sharedPreferences = context.getSharedPreferences(ConfigGuardarShared.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ConfigGuardarShared.NOMBRE_PREF, currentUserName);
                    editor.putString(ConfigGuardarShared.ID_PREF, currentUserID);
                    editor.putString(ConfigGuardarShared.FOTO_PREF, currentUserfoto);
                    editor.apply();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error 2",databaseError.getMessage());
            }
        });



    }


    public void eliminarMensajes(final Context context, final String sender, final String reciver) {


        ChatMensajesRef.child(sender).child(reciver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage(" Desea eliminar los mensajes");
                    alertDialogBuilder.setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //PARA ELIMINAR MENSAJES DE DATABASE

                                    ChatMensajesRef.child(sender).child(reciver)
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        ChatMensajesRef.child(reciver).child(sender)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                    @Override
                                                                    public void onComplete(@NonNull final Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            //PARA ELIMINAR AUDIO DE STORAGE no se puede toca 1 por 1
                                                                       /*     AudioChatMensajesRef.child(sender).child(reciver)
                                                                                    .delete()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if(task.isSuccessful()){
                                                                                                Toast.makeText(context, "Contacto y Mensajes eliminados ", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                        }
                                                                                    });*/

                                                                            Toast.makeText(context, "Contacto y Mensajes eliminados ", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                });

                                                    }


                                                }

                                            });

                                }
                            });
                    alertDialogBuilder.setNegativeButton("No ",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } else {
                    Toast.makeText(context, "Contacto Eliminado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //GET AN SETTER currentUserID currentUserName currentUserfoto
    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getCurrentUserfoto() {
        return currentUserfoto;
    }

    public void setCurrentUserfoto(String currentUserfoto) {
        this.currentUserfoto = currentUserfoto;
    }


}

