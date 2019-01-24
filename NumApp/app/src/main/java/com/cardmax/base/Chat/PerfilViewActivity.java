package com.cardmax.base.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cardmax.base.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Chat.Services.ConfigShared;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;;

public class PerfilViewActivity extends AppCompatActivity {

    private String reciverUserID, senderUserID, Current_State;
    private CircleImageView userProfileImage;

    private TextView userProfileName, userProfileSatus;
    private Button SendMessageRequestButton, DeclineMessageRequestButton;

    private DatabaseReference UserRef, ChatRequestRef, ContactsRef, NotificacionRef;
    private FirebaseAuth mAuth;

    private String userName;

    private String nombreRecibido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_activity_perfil_view);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Respuestas");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contactos");
        NotificacionRef = FirebaseDatabase.getInstance().getReference().child("Notificaciones SA");


        reciverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();


        SharedPreferences sharedPreferences = getSharedPreferences(ConfigShared.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nombreRecibido = sharedPreferences.getString(ConfigShared.SHARED_NAME_CARGADO, "No Disponible");



        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_image);

        userProfileName = (TextView) findViewById(R.id.visit_user_name);
        userProfileSatus = (TextView) findViewById(R.id.visit_profile_status);
        SendMessageRequestButton = (Button) findViewById(R.id.send_message_request_button);
        DeclineMessageRequestButton = (Button) findViewById(R.id.decline_message_request_button);

        Current_State = "new";


        RetriveUserInfo();
    }

    private void RetriveUserInfo() {
        UserRef.child(reciverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {

                    String userImage = dataSnapshot.child("image").getValue().toString();
                    userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();

                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                    userProfileName.setText(userName);
                    userProfileSatus.setText(userStatus);

                    ManageChatRequest();

                } else {// si no tiene imagen

                    userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();
                    userProfileName.setText(userName);
                    userProfileSatus.setText(userStatus);

                    ManageChatRequest();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ManageChatRequest() {

        ChatRequestRef.child(senderUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(reciverUserID)) {

                            String request_Type = dataSnapshot.child(reciverUserID).child("tipo").getValue().toString();

                            if (request_Type.equals("enviado")) {

                                Current_State = "respuesta_enviado";
                                SendMessageRequestButton.setText("Cancelar Solicitud");


                            } else if (request_Type.equals("recibido")) {

                                Current_State = "respuesta_recibida";
                                SendMessageRequestButton.setText("Aceptar Solicitud");

                                DeclineMessageRequestButton.setVisibility(View.VISIBLE);
                                DeclineMessageRequestButton.setEnabled(true);

                                DeclineMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CancelChatRequest();

                                    }
                                });

                            }


                        } else {
                            ContactsRef.child(senderUserID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.hasChild(reciverUserID)) {

                                                Current_State = "amigos";
                                                SendMessageRequestButton.setText("Eliminar Amigo");

                                            }
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


        if (!senderUserID.equals(reciverUserID)) {
            SendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SendMessageRequestButton.setEnabled(false);

                    if (Current_State.equals("new")) {
                        SendChatRequest();
                    }
                    if (Current_State.equals("respuesta_enviado")) {
                        CancelChatRequest();
                    }
                    if (Current_State.equals("respuesta_recibida")) {
                        AcceptChatRequest();
                    }
                    if (Current_State.equals("amigos")) {
                        RemoveThisContact();
                    }


                }
            });
        } else {
            SendMessageRequestButton.setVisibility(View.INVISIBLE);
        }


    }

    private void RemoveThisContact() {

        ContactsRef.child(senderUserID).child(reciverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            ContactsRef.child(reciverUserID).child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                DeclineMessageRequestButton.setEnabled(false);

                                                SendMessageRequestButton.setEnabled(true);
                                                Current_State = "new";
                                                SendMessageRequestButton.setText("Enviar Solicitud");

                                            }
                                        }
                                    });

                        }


                    }

                });

    }

    private void AcceptChatRequest() {

        final HashMap<String, String> profileMap = new HashMap<>();
        profileMap.put("name", userName);
        profileMap.put("Contactos", "Guardado");

        final HashMap<String, String> profileMap2 = new HashMap<>();
        profileMap2.put("name", nombreRecibido);
        profileMap2.put("Contactos", "Guardado");


        ContactsRef.child(senderUserID).child(reciverUserID)
                .setValue(profileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            ContactsRef.child(reciverUserID).child(senderUserID)
                                    .setValue(profileMap2)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                ChatRequestRef.child(senderUserID).child(reciverUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {


                                                                    ChatRequestRef.child(reciverUserID).child(senderUserID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    SendMessageRequestButton.setEnabled(true);
                                                                                    Current_State = "amigos";
                                                                                    SendMessageRequestButton.setText("Eliminar Amigo");

                                                                                    DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                                                    DeclineMessageRequestButton.setEnabled(false);


                                                                                }
                                                                            });


                                                                }


                                                            }
                                                        });


                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    private void CancelChatRequest() {

        ChatRequestRef.child(senderUserID).child(reciverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            ChatRequestRef.child(reciverUserID).child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                DeclineMessageRequestButton.setVisibility(View.INVISIBLE);
                                                DeclineMessageRequestButton.setEnabled(false);

                                                SendMessageRequestButton.setEnabled(true);
                                                Current_State = "new";
                                                SendMessageRequestButton.setText("Enviar Solicitud");

                                            }
                                        }
                                    });

                        }


                    }

                });

    }

    private void SendChatRequest() {
        ChatRequestRef.child(senderUserID).child(reciverUserID)
                .child("tipo").setValue("enviado")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ChatRequestRef.child(reciverUserID).child(senderUserID)
                                    .child("tipo").setValue("recibido")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                HashMap<String, String> chatnotificacion = new HashMap<>();
                                                chatnotificacion.put("from", senderUserID);
                                                chatnotificacion.put("tipo", "respuesta");
                                                NotificacionRef.child(reciverUserID).push()
                                                        .setValue(chatnotificacion)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {


                                                                    SendMessageRequestButton.setEnabled(true);
                                                                    Current_State = "respuesta_enviado";
                                                                    SendMessageRequestButton.setText("Cancelar Solicitud");

                                                                }

                                                            }
                                                        });


                                            }
                                        }
                                    });


                        }
                    }
                });
    }


}
