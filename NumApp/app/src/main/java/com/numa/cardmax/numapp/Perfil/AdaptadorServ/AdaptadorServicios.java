package com.numa.cardmax.numapp.Perfil.AdaptadorServ;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.numa.cardmax.numapp.Perfil.AddServ;
import com.numa.cardmax.numapp.Perfil.PerfilMainActivity;
import com.numa.cardmax.numapp.R;

import java.util.List;

public class AdaptadorServicios extends RecyclerView.Adapter<ViewHolderServicios> {
    private List<ObjetoServicio> listaObjeto;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference refDb = mDatabase.getReference();


    public AdaptadorServicios(List<ObjetoServicio> listaObjeto) {
        this.listaObjeto = listaObjeto;
    }



    @NonNull
    @Override
    public ViewHolderServicios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.perfil_layout_servicios, parent, false);
        return new ViewHolderServicios(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderServicios holder, final int position) {


        holder.nameserv.setText(listaObjeto.get(position).getNombre_serv());
        holder.fechaserv.setText(listaObjeto.get(position).getFechapublicacion_serv());
        if(listaObjeto.get(position).getPrecio_serv() == 0){
            holder.precioserv.setText("FREE");
        }else{
            holder.precioserv.setText("Precio : "+listaObjeto.get(position).getPrecio_serv()+" pesos.");
        }


        if(listaObjeto.get(position).getOnduracion_serv() == 0){
            holder.duracionserv.setVisibility(View.GONE);
        }else{

            holder.duracionserv.setText("Duracion "+listaObjeto.get(position).getHoraini_serv()+" - "+listaObjeto.get(position).getHorafin_serv()+" - Tiempo Extra : "+listaObjeto.get(position).getTiempo_extra());

        }
        if(listaObjeto.get(position).getDescripcion_serv().equals("")){
            holder.descripcionserv.setVisibility(View.GONE);
        }else{
            holder.descripcionserv.setText(listaObjeto.get(position).getDescripcion_serv());
        }

        if(listaObjeto.get(position).getOnimage_Serv() == 0){
            holder.imgServ.setVisibility(View.GONE);
        }
        if(listaObjeto.get(position).getUrlimage_serv().equals("")){
            holder.progres.setVisibility(View.GONE);
            holder.imgServ.setVisibility(View.GONE);

        }else{
            Glide.with(holder.imgServ.getContext())
                    .load(listaObjeto.get(position).getUrlimage_serv())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            System.out.println("nuevo error ->"+e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progres.setVisibility(View.GONE);
                            holder.imgServ.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.imgServ);
        }




        if(listaObjeto.get(position).getOnservicio_Serv() == 0){

            holder.switchserv.setChecked(false);
        }else{
            holder.switchserv.setChecked(true);
        }


        holder.eliminarserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refDb.child("perfil_servicios").child(listaObjeto.get(position).getKey_serv()).removeValue();
                Toast.makeText(holder.eliminarserv.getContext(), "en desarrollo", Toast.LENGTH_LONG).show();

            }
        });


        holder.switchserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.switchserv.isChecked()){
                     refDb.child("perfil_servicios").child(listaObjeto.get(position).getKey_serv()).child("onservicio_Serv").setValue(1);
                }else{
                    refDb.child("perfil_servicios").child(listaObjeto.get(position).getKey_serv()).child("onservicio_Serv").setValue(0);

                }
            }
        });


        holder.editarserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intento;
                intento = new Intent(holder.editarserv.getContext(), AddServ.class);
                intento.putExtra("tipo", "1");
                intento.putExtra("key", listaObjeto.get(position).getKey_serv().toString());
                holder.editarserv.getContext().startActivity(intento);
              //  ((PerfilMainActivity)holder.editarserv.getContext()).finish();
            }
        });




    }

    @Override
    public int getItemCount() {
        return listaObjeto.size();
    }
}
