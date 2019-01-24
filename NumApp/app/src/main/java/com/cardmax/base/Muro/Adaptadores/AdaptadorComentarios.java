package com.cardmax.base.Muro.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cardmax.base.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Muro.Objetos.ObjetoComentario;
import com.cardmax.base.Muro.Objetos.ObjetoUser;


import java.util.List;

public class AdaptadorComentarios extends RecyclerView.Adapter<ViewHolderComentarios> {

    private DatabaseReference mDatabase;
    List<ObjetoComentario> listaObjeto;
    public String name;
    public String fotoperfil, nombreuser;

    public AdaptadorComentarios(List<ObjetoComentario> listaObjeto) {
        this.listaObjeto = listaObjeto;
    }

    @NonNull
    @Override
    public ViewHolderComentarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.muro_layout_comentarios, parent, false);
        return new ViewHolderComentarios(vista);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderComentarios holder, int position) {

        holder.contenido.setText(listaObjeto.get(position).getComentario());
        holder.tiempo.setText(listaObjeto.get(position).getTiempo());





        mDatabase = FirebaseDatabase.getInstance().getReference();
        try {
            mDatabase.child("Users").child(listaObjeto.get(position).getId_usuario()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ObjetoUser userx = dataSnapshot.getValue(ObjetoUser.class);
                            name = userx.name; // "John Doe"
                            String foto = userx.image; // "Texas"
                            holder.nombre_user.setText(name);
                            Glide.with(holder.perfil_user.getContext())
                                    .load(foto)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(holder.perfil_user);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
            System.out.println(e);
        }








    }



    @Override
    public int getItemCount() {
        return listaObjeto.size();
    }
}
