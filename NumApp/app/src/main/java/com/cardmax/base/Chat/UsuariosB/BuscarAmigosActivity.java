package com.cardmax.base.Chat.UsuariosB;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Chat.ContactosAceptados.Contactos;
import com.cardmax.base.Chat.PerfilViewActivity;
import com.cardmax.base.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuscarAmigosActivity extends AppCompatActivity {



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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_buscar_amigos);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        FindFrendsRecyclerList = findViewById(R.id.find_friends_recycler_list);
        FindFrendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        search_edit_text = findViewById(R.id.searchUsuarios);
        layoutvacio= findViewById(R.id.layoutVacio);

      mToolbar = findViewById(R.id.find_friends_tooblar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Buscar amigos");


          /*
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
       */




        fullNameList = new ArrayList<>();
        userNameList = new ArrayList<>();
        profilePicList = new ArrayList<>();
        cargarlistadefaul();
        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cargarlistadefaul();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verificarSiExisteEsaPersona();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                //    cargarlistadefaul();
                    setAdapter(s.toString());
                } else {

                    /*
                     * Clear the list when editText is empty
                     * */
                    fullNameList.clear();
                    userNameList.clear();
                    profilePicList.clear();
                    FindFrendsRecyclerList.removeAllViews();
                    //cargarlistadefaul();
                  //  layoutvacio.setVisibility(View.VISIBLE);


                }
            }
        });





    }

    public void verificarSiExisteEsaPersona(){
        if(fullNameList.isEmpty() && userNameList.isEmpty()&& profilePicList.isEmpty()){
            layoutvacio.setVisibility(View.VISIBLE);
            FindFrendsRecyclerList.setVisibility(View.GONE);
        }else{
            layoutvacio.setVisibility(View.GONE);
            FindFrendsRecyclerList.setVisibility(View.VISIBLE);
        }
    }


    private void setAdapter(final String searchedString) {
        UsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                 * Clear the list for every new search
                 * */
                fullNameList.clear();
                userNameList.clear();
                profilePicList.clear();
                FindFrendsRecyclerList.removeAllViews();

                int counter = 0;

                /*
                 * Search all users for matching searched string
                 * */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    String full_name = snapshot.child("name").getValue(String.class);
                    String user_name = snapshot.child("status").getValue(String.class);
                    String profile_pic = snapshot.child("image").getValue(String.class);

                    if (full_name.toLowerCase().contains(searchedString.toLowerCase())) {
                        fullNameList.add(full_name);
                        userNameList.add(user_name);
                        profilePicList.add(profile_pic);
                        counter++;
                    } else if (user_name.toLowerCase().contains(searchedString.toLowerCase())) {
                        fullNameList.add(full_name);
                        userNameList.add(user_name);
                        profilePicList.add(profile_pic);
                        counter++;
                    }

                    /*
                     * Get maximum of 15 searched results only
                     * */
                    if (counter == 15)
                        break;
                }

                searchAdapter = new SearchAdapter(BuscarAmigosActivity.this, fullNameList, userNameList, profilePicList);
                FindFrendsRecyclerList.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






    private void cargarlistadefaul() {

        FirebaseRecyclerOptions<Contactos> options =
                new FirebaseRecyclerOptions.Builder<Contactos>()
                        .setQuery(UsersRef.orderByChild("name"),Contactos.class)
                        .build();

      //  super.onStart();
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
                                Intent PefilvIntent = new Intent(BuscarAmigosActivity.this, PerfilViewActivity.class);
                                PefilvIntent.putExtra("visit_user_id",vist_user_id);
                                startActivity(PefilvIntent);
                            }
                        });
                        holder.profileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(BuscarAmigosActivity.this, "click en la imagen", Toast.LENGTH_SHORT).show();
                                myDialog = new Dialog(BuscarAmigosActivity.this);
                                myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                                TextView dialog_name = myDialog.findViewById(R.id.titulo_dialogo_texto);
                                CircleImageView imagen_name = myDialog.findViewById(R.id.imagen_dialogo);
                                dialog_name.setText(nombre_guardado);
                                Picasso.get().load(imagend_guarado).placeholder(R.drawable.profile_image).into(imagen_name);



                                myDialog.show();
                            }
                        });



                    }

                    @NonNull
                    @Override
                    public EncontrarAmigosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_users_display_layout, viewGroup, false);
                        EncontrarAmigosHolder viewHolder = new EncontrarAmigosHolder(view);
                        return viewHolder;
                    }
                };

        FindFrendsRecyclerList.setAdapter(adapter);
        adapter.startListening();
        //FindFrendsRecyclerList.setStackFromEnd(true);

    }


    @Override
    protected void onStart() {
        super.onStart();

/*
        FirebaseRecyclerOptions<Contactos> options =
                new FirebaseRecyclerOptions.Builder<Contactos>()
                        .setQuery(UsersRef,Contactos.class)
                        .build();

        super.onStart();
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
                                Intent PefilvIntent = new Intent(BuscarAmigosActivity.this, PerfilViewActivity.class);
                               PefilvIntent.putExtra("visit_user_id",vist_user_id);
                                startActivity(PefilvIntent);
                            }
                        });
                        holder.profileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(BuscarAmigosActivity.this, "click en la imagen", Toast.LENGTH_SHORT).show();
                                myDialog = new Dialog(BuscarAmigosActivity.this);
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
                adapter.startListening();*/

    }


    public static class EncontrarAmigosHolder extends RecyclerView.ViewHolder {

        TextView userName, userStatus;
        CircleImageView profileImage;


        public EncontrarAmigosHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_profile_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }

}









