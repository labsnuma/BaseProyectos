package com.numa.cardmax.numapp.Perfil.AdaptadorServ;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.numa.cardmax.numapp.R;

public class ViewHolderServicios extends RecyclerView.ViewHolder {

    TextView nameserv, fechaserv, precioserv, duracionserv, descripcionserv;
    ImageView imgServ;
    Switch switchserv;
    Button editarserv, eliminarserv;
    ProgressBar progres;


    public ViewHolderServicios(View itemView) {
        super(itemView);
        nameserv = (TextView) itemView.findViewById(R.id.txtname_serv);
        fechaserv = (TextView) itemView.findViewById(R.id.txtfecha_serv);
        precioserv = (TextView) itemView.findViewById(R.id.txtprecio_serv);
        duracionserv = (TextView) itemView.findViewById(R.id.txtduracion_serv2);
        descripcionserv = (TextView) itemView.findViewById(R.id.txtdescripcion_serv);
        editarserv = (Button) itemView.findViewById(R.id.btn_editar_serv);
        eliminarserv = (Button) itemView.findViewById(R.id.btn_eliminar_serv);
        imgServ = (ImageView) itemView.findViewById(R.id.img_Serv);
        switchserv = (Switch) itemView.findViewById(R.id.switch_serv);
        progres = (ProgressBar)itemView.findViewById(R.id.progressBarserv) ;
    }
}
