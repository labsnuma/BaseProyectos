package com.numa.cardmax.numapp.Chat;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.numa.cardmax.numapp.Chat.ContactosAceptados.Contactos;
import com.numa.cardmax.numapp.Chat.Services.ConfigShared;
import com.numa.cardmax.numapp.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class SolicitudesFragment extends Fragment {

    private View SolicitudesFragmentView;
    private RecyclerView myRequesttList;
    private DatabaseReference UserRef, ChatRequestRef, ContactsRef, ChatMensajesRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    Dialog myDialog;
    private String nombreRecibido;
    private LinearLayout layoutvacio;

    //
    private InicioActivity esconder;
    private float y;
    private float y1;

    public SolicitudesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SolicitudesFragmentView = inflater.inflate(R.layout.chat_fragment_solicitudes, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contactos");
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Respuestas");
        ChatMensajesRef = FirebaseDatabase.getInstance().getReference().child("Chat Mensajes");


        myRequesttList = (RecyclerView) SolicitudesFragmentView.findViewById(R.id.chat_request_list);
        myRequesttList.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(ConfigShared.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        nombreRecibido = sharedPreferences.getString(ConfigShared.SHARED_NAME_CARGADO, "No Disponible");

        layoutvacio = (LinearLayout) SolicitudesFragmentView.findViewById(R.id.layoutVacio_solicitudes);
        layoutvacio.setVisibility(View.VISIBLE);
    //    layoutvacio.setVisibility(View.INVISIBLE);

        esconder = (InicioActivity) getActivity();
        esconder.barmuestra1(SolicitudesFragmentView);
        mostrar();
        solicitudesMostrar();
        return SolicitudesFragmentView;
    }

    private void solicitudesMostrar() {

        final FirebaseRecyclerOptions<Contactos> options =
                new FirebaseRecyclerOptions.Builder<Contactos>()
                        .setQuery(ChatRequestRef.child(currentUserID), Contactos.class)
                        .build();

        FirebaseRecyclerAdapter<Contactos, SolicitudesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contactos, SolicitudesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SolicitudesViewHolder holder, int position, @NonNull Contactos model) {

                        final String list_user_id = getRef(position).getKey();
                        DatabaseReference getTypeRef = getRef(position).child("tipo").getRef();

                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    String type = dataSnapshot.getValue().toString();
                                    if (type.equals("recibido")) {
                                        layoutvacio.setVisibility(View.INVISIBLE);
                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("image")) {

                                                    final String usersImage = dataSnapshot.child("image").getValue().toString();
                                                    Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
                                                }
                                                final String usersImage = dataSnapshot.child("image").getValue().toString();
                                                final String profileName = dataSnapshot.child("name").getValue().toString();
                                                final String profileStatus = dataSnapshot.child("status").getValue().toString();
                                                holder.userName.setText(profileName);

                                                holder.userStatus.setText("Quiere ser tu amigo");

                                                ///los 2 en 1
                                                final HashMap<String, String> profileMap = new HashMap<>();
                                                profileMap.put("name", profileName);
                                                profileMap.put("Contactos", "Guardado");

                                                final HashMap<String, String> profileMap2 = new HashMap<>();
                                                profileMap2.put("name", nombreRecibido);
                                                profileMap2.put("Contactos", "Guardado");
                                                //flos 2 en 1

                                                holder.AceptarBoton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        //desde
                                                        ContactsRef.child(currentUserID).child(list_user_id)
                                                                .setValue(profileMap)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            ContactsRef.child(list_user_id).child(currentUserID)
                                                                                    .setValue(profileMap2)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                                                        .removeValue()
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                                                            .removeValue()
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        Toast.makeText(getContext(), "Contacto agregado", Toast.LENGTH_SHORT).show();
                                                                                                                                        resetFragment();


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
                                                                });


                                                    }


                                                });


                                                holder.CancelarBoton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Toast.makeText(getContext(), "Solicitud Eliminada", Toast.LENGTH_SHORT).show();
                                                                                                resetFragment();


                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }

                                                                    }
                                                                });

                                                    }
                                                });

                                                holder.profileImage.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        myDialog = new Dialog(getContext());
                                                        myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                                                        TextView dialog_name = (TextView) myDialog.findViewById(R.id.titulo_dialogo_texto);
                                                        CircleImageView imagen_name = (CircleImageView) myDialog.findViewById(R.id.imagen_dialogo);
                                                        dialog_name.setText(profileName);
                                                        Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(imagen_name);


                                                        myDialog.show();
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    } else if (type.equals("enviado")) {
                                        layoutvacio.setVisibility(View.INVISIBLE);
                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("image")) {
                                                    final String usersImage = dataSnapshot.child("image").getValue().toString();
                                                    Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
                                                }

                                                final String usersImage = dataSnapshot.child("image").getValue().toString();
                                                final String profileName = dataSnapshot.child("name").getValue().toString();
                                                holder.userName.setText(profileName);

                                                holder.userStatus.setText("Has enviado la solicitud");
                                                holder.AceptarBoton.setVisibility(View.INVISIBLE);

                                                holder.CancelarBoton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Toast.makeText(getContext(), "Solicitud de amistad Cancelada", Toast.LENGTH_SHORT).show();
                                                                                                resetFragment();
                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }

                                                                    }
                                                                });

                                                    }
                                                });

                                                holder.profileImage.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        myDialog = new Dialog(getContext());
                                                        myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                                                        TextView dialog_name = (TextView) myDialog.findViewById(R.id.titulo_dialogo_texto);
                                                        CircleImageView imagen_name = (CircleImageView) myDialog.findViewById(R.id.imagen_dialogo);
                                                        dialog_name.setText(profileName);
                                                        Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(imagen_name);


                                                        myDialog.show();
                                                    }
                                                });
                                             //   notifyDataSetChanged();

                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                /*    else if(type.isEmpty()){
                                        layoutvacio.setVisibility(View.VISIBLE);

                                    }*/

                                } /*else if (!dataSnapshot.exists()) {
                                    layoutvacio.setVisibility(View.VISIBLE);

                                }*/

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int viewType) {
                        View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.chat_users_display_layout_1, viewgroup, false);
                        SolicitudesViewHolder holder = new SolicitudesViewHolder(view);

                        return holder;
                    }
                };

        myRequesttList.setAdapter(adapter);
        adapter.startListening();

        myRequesttList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    y = event.getY();
                    float scroll;
                    scroll = y1 - y;
                    if (scroll > -1) {
                        esconder.baroculta(v);
                    } else {
                        esconder.barmuestra1(v);
                    }
                }
                return false;
            }
        });

    }

    private void mostrar() {
        esconder.barmuestra1(SolicitudesFragmentView);
    }

    private void resetFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    @Override
    public void onStart() {
        super.onStart();
/*

        final FirebaseRecyclerOptions<Contactos> options =
                new FirebaseRecyclerOptions.Builder<Contactos>()
                        .setQuery(ChatRequestRef.child(currentUserID), Contactos.class)
                        .build();
        FirebaseRecyclerAdapter<Contactos, SolicitudesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contactos, SolicitudesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SolicitudesViewHolder holder, int position, @NonNull Contactos model) {

                        final String list_user_id = getRef(position).getKey();
                        DatabaseReference getTypeRef = getRef(position).child("tipo").getRef();

                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    String type = dataSnapshot.getValue().toString();
                                    if (type.equals("recibido")) {
                                        layoutvacio.setVisibility(View.INVISIBLE);
                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.hasChild("image")) {

                                                    final String usersImage = dataSnapshot.child("image").getValue().toString();
                                                    Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
                                                }
                                                final String usersImage = dataSnapshot.child("image").getValue().toString();
                                                final String profileName = dataSnapshot.child("name").getValue().toString();
                                                final String profileStatus = dataSnapshot.child("status").getValue().toString();
                                                holder.userName.setText(profileName);

                                                holder.userStatus.setText("Quiere ser tu amigo");

                                                ///los 2 en 1
                                                final HashMap<String, String> profileMap = new HashMap<>();
                                                profileMap.put("name", profileName);
                                                profileMap.put("Contactos", "Guardado");

                                                final HashMap<String, String> profileMap2 = new HashMap<>();
                                                profileMap2.put("name", nombreRecibido);
                                                profileMap2.put("Contactos", "Guardado");
                                                //flos 2 en 1

                                                holder.AceptarBoton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        //desde
                                                        ContactsRef.child(currentUserID).child(list_user_id)
                                                                .setValue(profileMap)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            ContactsRef.child(list_user_id).child(currentUserID)
                                                                                    .setValue(profileMap2)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                                                        .removeValue()
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                                                            .removeValue()
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                    if (task.isSuccessful()) {
                                                                                                                                        Toast.makeText(getContext(), "Contacto agregado", Toast.LENGTH_SHORT).show();


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
                                                                });


                                                    }


                                                });


                                                holder.CancelarBoton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Toast.makeText(getContext(), "Solicitud Eliminada", Toast.LENGTH_SHORT).show();


                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }

                                                                    }
                                                                });

                                                    }
                                                });

                                                holder.profileImage.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        myDialog = new Dialog(getContext());
                                                        myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                                                        TextView dialog_name = (TextView) myDialog.findViewById(R.id.titulo_dialogo_texto);
                                                        CircleImageView imagen_name = (CircleImageView) myDialog.findViewById(R.id.imagen_dialogo);
                                                        dialog_name.setText(profileName);
                                                        Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(imagen_name);


                                                        myDialog.show();
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    else if (type.equals("enviado")) {
                                        layoutvacio.setVisibility(View.INVISIBLE);
                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("image")) {
                                                    final String usersImage = dataSnapshot.child("image").getValue().toString();
                                                    Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
                                                }

                                                final String usersImage = dataSnapshot.child("image").getValue().toString();
                                                final String profileName = dataSnapshot.child("name").getValue().toString();
                                                holder.userName.setText(profileName);

                                                holder.userStatus.setText("Has enviado la solicitud");
                                                holder.AceptarBoton.setVisibility(View.INVISIBLE);

                                                holder.CancelarBoton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ChatRequestRef.child(currentUserID).child(list_user_id)
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            ChatRequestRef.child(list_user_id).child(currentUserID)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Toast.makeText(getContext(), "Solicitud de amistad Cancelada", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                        }
                                                                                    });
                                                                        }

                                                                    }
                                                                });

                                                    }
                                                });

                                                holder.profileImage.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        myDialog = new Dialog(getContext());
                                                        myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                                                        TextView dialog_name = (TextView) myDialog.findViewById(R.id.titulo_dialogo_texto);
                                                        CircleImageView imagen_name = (CircleImageView) myDialog.findViewById(R.id.imagen_dialogo);
                                                        dialog_name.setText(profileName);
                                                        Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(imagen_name);


                                                        myDialog.show();
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                    else if(type.isEmpty()){
                                        layoutvacio.setVisibility(View.VISIBLE);

                                    }
                                }
                                else if (!dataSnapshot.exists()) {
                                    layoutvacio.setVisibility(View.VISIBLE);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int viewType) {
                        View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.chat_users_display_layout_1, viewgroup, false);
                        SolicitudesViewHolder holder = new SolicitudesViewHolder(view);

                        return holder;
                    }
                };

        myRequesttList.setAdapter(adapter);
        adapter.startListening();

        myRequesttList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    y = event.getY();
                    float scroll;
                    scroll = y1 - y;
                    if (scroll > -1) {
                        esconder.baroculta(v);
                    } else {
                        esconder.barmuestra1(v);
                    }
                }
                return false;
            }
        });
*/


    }


    public static class SolicitudesViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button AceptarBoton, CancelarBoton;

        public SolicitudesViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_profile_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            AceptarBoton = itemView.findViewById(R.id.solicitud_aceptar_btn);
            CancelarBoton = itemView.findViewById(R.id.solicitud_cancelar_btn);

        }
    }


}
