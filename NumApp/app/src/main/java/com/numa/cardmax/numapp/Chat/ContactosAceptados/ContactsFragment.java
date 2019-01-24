package com.numa.cardmax.numapp.Chat.ContactosAceptados;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.numa.cardmax.numapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;



public class ContactsFragment extends Fragment {

    private View ContactsView;
    private RecyclerView myContactList;
    private DatabaseReference UserRef, ChatRequestRef, ContactsRef, ContactsRefEmilnar;
    private FirebaseAuth mAuth;
    private String currentUserID, senderUserID;
    Dialog myDialog;

    private LinearLayout layoutvacio;


    public ContactsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ContactsView = inflater.inflate(R.layout.chat_fragment_contacts, container, false);
        myContactList = (RecyclerView) ContactsView.findViewById(R.id.contacts_list);
        myContactList.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //  ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Respuestas");
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contactos").child(currentUserID);

        ContactsRefEmilnar = FirebaseDatabase.getInstance().getReference().child("Contactos");
        senderUserID = mAuth.getCurrentUser().getUid();


        layoutvacio = (LinearLayout) ContactsView.findViewById(R.id.layoutVacio_contactos);
        layoutvacio.setVisibility(View.VISIBLE);

        return ContactsView;
    }

    @Override
    public void onStart() {

        super.onStart();
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contactos>()
                        .setQuery(ContactsRef.orderByChild("name"), Contactos.class)
                        .build();

        FirebaseRecyclerAdapter<Contactos, ContactsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Contactos, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull Contactos model) {
                layoutvacio.setVisibility(View.VISIBLE);
                final String usersIDs = getRef(position).getKey();
                layoutvacio.setVisibility(View.VISIBLE);
                holder.number_message.setVisibility(View.VISIBLE);
               UserRef.child(usersIDs).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                          //  layoutvacio.setVisibility(View.VISIBLE);
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
                                        myDialog = new Dialog(getContext());
                                        myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                                        TextView dialog_name = (TextView) myDialog.findViewById(R.id.titulo_dialogo_texto);
                                        CircleImageView imagen_name = (CircleImageView) myDialog.findViewById(R.id.imagen_dialogo);
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
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


                                                                                            Toast.makeText(getContext(), "Contacto eliminado", Toast.LENGTH_SHORT).show();

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

                       /*     holder.profileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                 //   Toast.makeText(getContext(), "click en la imagen", Toast.LENGTH_SHORT).show();
                                    myDialog = new Dialog(getContext());
                                    myDialog.setContentView(R.layout.dialogo_mostrar_imagen);
                                    TextView dialog_name = (TextView) myDialog.findViewById(R.id.titulo_dialogo_texto);
                                    CircleImageView imagen_name = (CircleImageView) myDialog.findViewById(R.id.imagen_dialogo);
                                    dialog_name.setText(profileName);
                                    Picasso.get().load(usersImage).placeholder(R.drawable.profile_image).into(imagen_name);


                                    myDialog.show();
                                }
                            });*/


                            }


                        }
                        if(!dataSnapshot.hasChildren()){
                            layoutvacio.setVisibility(View.VISIBLE);
                        }
                        if (!dataSnapshot.exists()) {
                            layoutvacio.setVisibility(View.VISIBLE);

                            if(!dataSnapshot.hasChild("name") ){
                                layoutvacio.setVisibility(View.VISIBLE);
                            }
                            if(!dataSnapshot.hasChild("uid") ){
                                layoutvacio.setVisibility(View.VISIBLE);
                            }
                        }

                        if(!dataSnapshot.hasChild("status") ){
                            layoutvacio.setVisibility(View.VISIBLE);
                        }
                        if(!dataSnapshot.hasChild("image") ){
                            layoutvacio.setVisibility(View.VISIBLE);
                        }

                        if( dataSnapshot.equals("")){
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
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int viewType) {

                View view = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.chat_users_display_layout, viewgroup, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };
      //  myContactList.setAdapter(adapter);
        adapter.startListening();
        layoutvacio.setVisibility(View.VISIBLE);
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userStatus;
        CircleImageView profileImage;
        Button number_message;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_profile_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            number_message = (Button)itemView.findViewById(R.id.number_menssage);

        }
    }
}
