package com.numa.cardmax.numapp.Chat;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.numa.cardmax.numapp.Chat.ContactosAceptados.Contactos;
import com.numa.cardmax.numapp.Chat.MensajesChats.ChatActivity;
import com.numa.cardmax.numapp.Chat.MensajesChats.Messages;
import com.numa.cardmax.numapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsFragment extends Fragment {

    private RecyclerView chatsLit;
    private DatabaseReference UserRef, ChatsRef;
    private String CurrentUserID;
    Dialog myDialog;

    private DatabaseReference RootRef;

    private InicioActivity esconder;
    private float y;
    private float y1;




    public ChatsFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View PrivateFragmentview = inflater.inflate(R.layout.chat_fragment_chats, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            Intent LoginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(LoginIntent);
        }
        assert mAuth != null;
        CurrentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        ChatsRef = FirebaseDatabase.getInstance().getReference().child("Contactos").child(CurrentUserID);
        ChatsRef.keepSynced(true);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        chatsLit = PrivateFragmentview.findViewById(R.id.chats_list);
        chatsLit.setLayoutManager(new LinearLayoutManager(getContext()));

         RootRef = FirebaseDatabase.getInstance().getReference();

        esconder = (InicioActivity) getActivity();
        assert esconder != null;
        esconder.barmuestra1(PrivateFragmentview);

        return PrivateFragmentview;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contactos> options = new FirebaseRecyclerOptions.Builder<Contactos>()
                .setQuery(ChatsRef, Contactos.class)
                .build();

        FirebaseRecyclerAdapter<Contactos, ChatsViewHolder> adapter = new FirebaseRecyclerAdapter<Contactos, ChatsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, final int position, @NonNull Contactos model) {
                final String usersIDs = getRef(position).getKey();
                final String[] usersImage = {"default_imagen"};

                RootRef.child("Chat Mensajes")
                        .child(CurrentUserID)
                        .child(usersIDs)
                        .orderByChild("visto")
                        .equalTo("0").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                        int contador = 0;

                        for (DataSnapshot murox : muroChildren) {
                            Messages m = murox.getValue(Messages.class);
                            if(!m.getFrom().equals(CurrentUserID)) {
                                contador += 1;
                            }
                        }

                        if(contador > 0){
                            holder.conteo.setVisibility(View.VISIBLE);
                            holder.conteo.setText(""+contador);

                        }else{

                            holder.conteo.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







                RootRef.child("Contactos").child(Objects.requireNonNull(usersIDs)).child(CurrentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("estadoUsuario").hasChild("estado")) {
                            String estado = Objects.requireNonNull(dataSnapshot.child("estadoUsuario").child("estado").getValue()).toString();
                            String fecha = Objects.requireNonNull(dataSnapshot.child("estadoUsuario").child("fecha").getValue()).toString();
                            String hora = Objects.requireNonNull(dataSnapshot.child("estadoUsuario").child("hora").getValue()).toString();

                            if (estado.equals("Online")) {
                                holder.userStatus.setText("Online");
                                /*   holder.userStatus.setTextColor(getResources().getColor(R.color.colorTextoMensaje));*/
                            }
                            else if (estado.equals("Offline")) {
                                holder.userStatus.setText(fecha + " " + hora);
                            } else if (estado.equals("Escribiendo")) {
                                holder.userStatus.setText("Escribiendo mensaje...");
                              /*  holder.userStatus.setTextColor(getResources().getColor(R.color.colorTextoHoraOn));*/
                            }
                        } else {
                            holder.userStatus.setText("Offline");
                           /*   holder.userStatus.setTextColor(getResources().getColor(R.color.colorTextoMensaje));*/
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







                UserRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild("image")) {

                                usersImage[0] = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                                Picasso.get().load(usersImage[0]).placeholder(R.drawable.profile_image).into(holder.profileImage);

                            }


                            final String usersImage = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                            final String profileName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                            final String profileStatus = Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString();
                            holder.userName.setText(profileName);
                            holder.userStatus.setText("Conexion ultima vez" + "\n" + "Fecha: ");


                            if (dataSnapshot.child("estadoUsuario").hasChild("estado")) {
                                String estado = Objects.requireNonNull(dataSnapshot.child("estadoUsuario").child("estado").getValue()).toString();
                                String fecha = Objects.requireNonNull(dataSnapshot.child("estadoUsuario").child("fecha").getValue()).toString();
                                String hora = Objects.requireNonNull(dataSnapshot.child("estadoUsuario").child("hora").getValue()).toString();
                                if (estado.equals("Online")) {
                                    holder.userStatus.setText("Online");
                                 //   holder.userStatus.setTextColor(getResources().getColor(R.color.colorTextoMensaje));
                                } else if (estado.equals("Offline")) {
                                    try {
                                        holder.userStatus.setText("Conexion ultima vez" + "\n" + fecha + " " + hora);
                                       /* holder.userStatus.setTextColor(getResources().getColor(R.color.colorTextoMensaje));*/
                                    } catch (Resources.NotFoundException e) {
                                        Log.e("Error#25",e.getMessage());
                                    }
                                    catch (IllegalStateException e){
                                        Log.e("Error#26",e.getMessage());
                                    }
                                } /*else if (estado.equals("Escribiendo")) {
                                    holder.userStatus.setText("Escribiendo mensaje...");
                                    holder.userStatus.setTextColor(getResources().getColor(R.color.colorTextoHoraOn));
                                }*/
                            } else {
                                holder.userStatus.setText("Offline");
                              /*  holder.userStatus.setTextColor(getResources().getColor(R.color.colorTextoMensaje));*/
                            }


                            holder.profileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    myDialog = new Dialog(Objects.requireNonNull(getContext()));
                                    myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                                    TextView dialog_name = myDialog.findViewById(R.id.titulo_dialogo_texto);
                                    final CircleImageView imagen_name =  myDialog.findViewById(R.id.imagen_dialogo);
                                    dialog_name.setText(profileName);
                                    //  Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(imagen_name);
                                    final RequestOptions opciones = new RequestOptions()
                                            .error(R.drawable.profile_image)
                                            .placeholder(R.drawable.profile_image).fitCenter()
                                            .centerCrop()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL);//para que quede guardada en chache y no vuelva a descargar

                                    Picasso.get().load(usersImage).networkPolicy(NetworkPolicy.OFFLINE).into(imagen_name, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Glide.with(getContext())
                                                    .load(usersImage).apply(opciones)
                                                    .into(imagen_name);
                                        }
                                    });


                                    myDialog.show();
                                }
                            });

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String chat_user_id = getRef(position).getKey();
                                    Intent ChatMensajeIntent = new Intent(getContext(), ChatActivity.class);
                                    ChatMensajeIntent.putExtra("chat_user_id", chat_user_id);
                                    ChatMensajeIntent.putExtra("visit_user_id", usersIDs);
                                    ChatMensajeIntent.putExtra("visit_user_name", profileName);
                                    ChatMensajeIntent.putExtra("visit_user_image", usersImage);
                                    startActivity(ChatMensajeIntent);
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
            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int viewType) {
                View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.chat_users_display_layout, viewgroup, false);
                return new ChatsViewHolder(view);
            }
        };

        chatsLit.setAdapter(adapter);
        adapter.startListening();
        chatsLit.setOnTouchListener(new View.OnTouchListener() {
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
                        esconder.barmuestra(v);

                    }
                }
                return false;
            }
        });

    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button conteo;


        private ChatsViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_profile_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            conteo = itemView.findViewById(R.id.number_menssage);
        }
    }




}
