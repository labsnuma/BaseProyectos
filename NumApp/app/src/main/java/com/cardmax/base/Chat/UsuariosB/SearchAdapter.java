package com.cardmax.base.Chat.UsuariosB;


import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.cardmax.base.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dushyant Mainwal on 29-Oct-17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> fullNameList;
    ArrayList<String> userNameList;
    ArrayList<String> profilePicList;
    Dialog myDialog;


    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage1;
        CircleImageView profileImage;
        TextView full_name, user_name;

        public SearchViewHolder(View itemView) {
            super(itemView);
            //  profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
            profileImage = itemView.findViewById(R.id.profileImage);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
            user_name = (TextView) itemView.findViewById(R.id.user_name);

        }
    }

    public SearchAdapter(Context context, ArrayList<String> fullNameList, ArrayList<String> userNameList, ArrayList<String> profilePicList) {
        this.context = context;
        this.fullNameList = fullNameList;
        this.userNameList = userNameList;
        this.profilePicList = profilePicList;

    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_prueba_activity_2, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        holder.full_name.setText(fullNameList.get(position));
        final String nombre_guardado = fullNameList.get(position);
        holder.user_name.setText(userNameList.get(position));
        final String estado_guardado = userNameList.get(position);
        //  Glide.with(context).load(profilePicList.get(position)).asBitmap().placeholder(R.mipmap.ic_launcher_round).into(holder.profileImage);
        Picasso.get().load(profilePicList.get(position)).placeholder(R.drawable.profile_image).into(holder.profileImage);
        final String imagend_guarado = profilePicList.get(position);


        //  Picasso.with(context).load(profilePicList.get(position).getFotoPerfil()).error(R.drawable.ic_account_circle).into(holder.profileImage());
        //  Picasso.get().load(profilePicList.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);

        holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click en el nombre" + nombre_guardado, Toast.LENGTH_SHORT).show();
                // String vist_user_id=getRef(position).getKey();
                // Intent PefilvIntent = new Intent(SearchAdapter.this, PerfilViewActivity.class);
                // PefilvIntent.putExtra("visit_user_id",vist_user_id);
                // startActivity(PefilvIntent);
            }
        });
        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click en el estado" + estado_guardado, Toast.LENGTH_SHORT).show();
                // String vist_user_id=getRef(position).getKey();
                // Intent PefilvIntent = new Intent(SearchAdapter.this, PerfilViewActivity.class);
                // PefilvIntent.putExtra("visit_user_id",vist_user_id);
                // startActivity(PefilvIntent);
            }
        });
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarImagen();
                Toast.makeText(context, "click en la imagen", Toast.LENGTH_SHORT).show();
                myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.chat_dialogo_mostrar_imagen);
                TextView dialog_name = (TextView) myDialog.findViewById(R.id.titulo_dialogo_texto);
                CircleImageView imagen_name = (CircleImageView) myDialog.findViewById(R.id.imagen_dialogo);
                dialog_name.setText(nombre_guardado);
//                final Uri myUri = Uri.parse(imagend_guarado);
              //  Picasso.get().load(imagend_guarado);
                Picasso.get().load(imagend_guarado).placeholder(R.drawable.profile_image).into(imagen_name);



                myDialog.show();
            }
        });



     /*   holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Full Name Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/


    }

    private void MostrarImagen() {

    }


    @Override
    public int getItemCount() {
        return fullNameList.size();
    }
}
