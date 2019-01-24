package com.numa.cardmax.numapp.Chat.ContactosAceptados;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.numa.cardmax.numapp.Chat.Recursos.FireBObtener;
import com.numa.cardmax.numapp.Chat.UsuariosB.SearchAdapter;
import com.numa.cardmax.numapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactosActivity extends AppCompatActivity {


    private RecyclerView FindFrendsRecyclerList;
    private DatabaseReference UsersRef;

    private FirebaseAuth mAuth;
    Dialog myDialog;
    private EditText search_edit_text;
    ArrayList<String> fullNameList;
    ArrayList<String> userNameList;
    ArrayList<String> profilePicList;
    SearchAdapter searchAdapter;
    private LinearLayout layoutvacio;

    private Toolbar mToolbar;
    //
    private String currentUserID, senderUserID;
    private DatabaseReference ContactsRef, ContactsRefEmilnar, ChatMensajesRef;
    private DatabaseReference UserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_contactos);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        FindFrendsRecyclerList = findViewById(R.id.find_friends_recycler_list);
        FindFrendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));


    /*    myContactList = (RecyclerView) ContactsView.findViewById(R.id.contacts_list);
        myContactList.setLayoutManager(new LinearLayoutManager(getContext()));*/

        /*   search_edit_text = (EditText) findViewById(R.id.searchUsuarios);*/
        layoutvacio = findViewById(R.id.layoutVacio);
        layoutvacio.setVisibility(View.VISIBLE);

        mToolbar = findViewById(R.id.find_friends_tooblar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Contactos");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

  /*      fullNameList = new ArrayList<>();
        userNameList = new ArrayList<>();
        profilePicList = new ArrayList<>();*/
        // cargarlistadefaul();


        //cambios desde aca para que quede en activity
        currentUserID = mAuth.getCurrentUser().getUid();
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contactos").child(currentUserID);
        ContactsRefEmilnar = FirebaseDatabase.getInstance().getReference().child("Contactos");
        ChatMensajesRef = FirebaseDatabase.getInstance().getReference().child("Chat Mensajes");
        senderUserID = mAuth.getCurrentUser().getUid();


    }

    @Override
    public void onStart() {

        super.onStart();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contactos>()
                        .setQuery(ContactsRef.orderByChild("name"), Contactos.class)
                        .build();

        FirebaseRecyclerAdapter<Contactos, ContactosActivity.ContactsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Contactos, ContactosActivity.ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactosActivity.ContactsViewHolder holder, int position, @NonNull Contactos model) {
                layoutvacio.setVisibility(View.VISIBLE);
                final String usersIDs = getRef(position).getKey();
                layoutvacio.setVisibility(View.VISIBLE);


                holder.number_message.setVisibility(View.GONE);
                holder.number_message.setText("HOOOOOOOOOOOOLA");
                UserRef.child(usersIDs).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            //  layoutvacio.setVisibility(View.VISIBLE);

                            if (dataSnapshot.child("estadoUsuario").hasChild("estado")) {
                                String estado = dataSnapshot.child("estadoUsuario").child("estado").getValue().toString();
                                String fecha = dataSnapshot.child("estadoUsuario").child("fecha").getValue().toString();
                                String hora = dataSnapshot.child("estadoUsuario").child("hora").getValue().toString();
                                if (estado.equals("Online")) {
                                    holder.onlineicon.setVisibility(View.VISIBLE);
                                } else if (estado.equals("Offline")) {
                                    holder.onlineicon.setVisibility(View.INVISIBLE);

                                }
                            } else {
                                holder.onlineicon.setVisibility(View.INVISIBLE);

                            }


                            if (dataSnapshot.hasChild("image")) {
                                layoutvacio.setVisibility(View.INVISIBLE);
                                final String usersImage = dataSnapshot.child("image").getValue().toString();
                                final String profileName = dataSnapshot.child("name").getValue().toString();
                                String profileStatus = dataSnapshot.child("status").getValue().toString();


                                holder.userName.setText(profileName);
                                holder.userStatus.setText(profileStatus);
                                Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(holder.profileImage);

                                //desde cambie el string a final
                                holder.profileImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //  Toast.makeText(getContext(), "click en la imagen", Toast.LENGTH_SHORT).show();
                                        // myDialog = new Dialog(getContext());
                                        myDialog = new Dialog(ContactosActivity.this);
                                        myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                                        TextView dialog_name = myDialog.findViewById(R.id.titulo_dialogo_texto);
                                        CircleImageView imagen_name = myDialog.findViewById(R.id.imagen_dialogo);
                                        dialog_name.setText(profileName);
                                        Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(imagen_name);


                                        myDialog.show();
                                    }
                                });
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Toast.makeText(getContext(), "toda la tarjeta", Toast.LENGTH_SHORT).show();

                                        CharSequence options[] = new CharSequence[]{
                                                "Eliminar", "Cancelar"
                                        };
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ContactosActivity.this);
                                        builder.setTitle("Eliminar a: " + profileName);
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {

                                                if (i == 0) {

                                                    ContactsRefEmilnar.child(senderUserID).child(usersIDs)
                                                            .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        ContactsRefEmilnar.child(usersIDs).child(senderUserID)
                                                                                .removeValue()
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {

                                                                                            FireBObtener mensajes = new FireBObtener();
                                                                                            mensajes.eliminarMensajes(ContactosActivity.this, senderUserID, usersIDs);


                                                                                        }
                                                                                    }
                                                                                });

                                                                    }


                                                                }

                                                            });
                                                }
                                                if (i == 1) {
                                                    return;
                                                }

                                            }
                                        });
                                        builder.show();

                                    }
                                });
                                //hasta
                            } else {
                                //   String usersImage = dataSnapshot.child("image").getValue().toString();
                                final String profileName = dataSnapshot.child("name").getValue().toString();
                                String profileStatus = dataSnapshot.child("status").getValue().toString();

                                final String usersImage = dataSnapshot.child("image").getValue().toString();
                                holder.userName.setText(profileName);
                                holder.userStatus.setText(profileStatus);
                            }


                        }
                        if (!dataSnapshot.hasChildren()) {
                            layoutvacio.setVisibility(View.VISIBLE);
                        }
                        if (!dataSnapshot.exists()) {
                            layoutvacio.setVisibility(View.VISIBLE);

                            if (!dataSnapshot.hasChild("name")) {
                                layoutvacio.setVisibility(View.VISIBLE);
                            }
                            if (!dataSnapshot.hasChild("uid")) {
                                layoutvacio.setVisibility(View.VISIBLE);
                            }
                        }

                        if (!dataSnapshot.hasChild("status")) {
                            layoutvacio.setVisibility(View.VISIBLE);
                        }
                        if (!dataSnapshot.hasChild("image")) {
                            layoutvacio.setVisibility(View.VISIBLE);
                        }

                        if (dataSnapshot.equals("")) {
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
            public ContactosActivity.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int viewType) {

                View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.chat_users_display_layout, viewgroup, false);
                ContactosActivity.ContactsViewHolder viewHolder = new ContactosActivity.ContactsViewHolder(view);
                return viewHolder;
            }
        };
        FindFrendsRecyclerList.setAdapter(adapter);
        adapter.startListening();
        layoutvacio.setVisibility(View.VISIBLE);
    }


    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userStatus;
        CircleImageView profileImage;
        ImageView onlineicon;
        Button number_message;


        public ContactsViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_profile_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            onlineicon = itemView.findViewById(R.id.user_online_status);
            number_message = (Button) itemView.findViewById(R.id.number_menssage);
        }
    }


   /* private void cargarlistadefaul() {

        FirebaseRecyclerOptions<Contactos> options =
                new FirebaseRecyclerOptions.Builder<Contactos>()
                        .setQuery(UsersRef.orderByChild("name"),Contactos.class)
                        .build();
        FirebaseRecyclerAdapter<Contactos, EncontrarAmigosHolder> adapter =
                new FirebaseRecyclerAdapter<Contactos, EncontrarAmigosHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull EncontrarAmigosHolder holder, final int position, @NonNull Contactos model) {
                        holder.userName.setText(model.getName());
                        final String nombre_guardado =  holder.userName.getText().toString();
                        holder.userStatus.setText(model.getStatus());
                        final String estado_guardado =  holder.userStatus.getText().toString();
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);
                        final String imagend_guarado = model.getImage();

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String vist_user_id=getRef(position).getKey();
                                Intent PefilvIntent = new Intent(ContactosActivity.this, PerfilViewActivity.class);
                                PefilvIntent.putExtra("visit_user_id",vist_user_id);
                                startActivity(PefilvIntent);
                            }
                        });
                        holder.profileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(ContactosActivity.this, "click en la imagen", Toast.LENGTH_SHORT).show();
                                myDialog = new Dialog(ContactosActivity.this);
                                myDialog.setContentView(R.layout.dialogo_mostrar_imagen);
                                TextView dialog_name = (TextView) myDialog.findViewById(R.id.titulo_dialogo_texto);
                                CircleImageView imagen_name = (CircleImageView) myDialog.findViewById(R.id.imagen_dialogo);
                                dialog_name.setText(nombre_guardado);
                                Picasso.get().load(imagend_guarado).placeholder(R.drawable.profile_image).into(imagen_name);



                                myDialog.show();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public EncontrarAmigosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        EncontrarAmigosHolder viewHolder = new EncontrarAmigosHolder(view);
                        return viewHolder;
                    }
                };

        FindFrendsRecyclerList.setAdapter(adapter);
        adapter.startListening();

    }*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.chat_amigos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_opciones1) {

            SendUserToOpcionesActivity();
        }
        if (item.getItemId() == R.id.menu_log_out) {
            mAuth.signOut();
            SendUserToLoginActivity();
        }


        return true;
    }


    private void SendUserToOpcionesActivity() {
        Intent OpcionesIntent = new Intent(ContactosActivity.this, OpcionesActivity.class);
        startActivity(OpcionesIntent);

    }

    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(ContactosActivity.this, LoginActivity.class);
      //  LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
    }
*/


}









