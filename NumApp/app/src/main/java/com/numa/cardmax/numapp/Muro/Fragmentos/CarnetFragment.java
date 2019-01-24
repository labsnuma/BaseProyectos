package com.numa.cardmax.numapp.Muro.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.numa.cardmax.numapp.Muro.MuroMainActivity;
import com.numa.cardmax.numapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarnetFragment extends Fragment {

private View view;
private ImageView fotoview;
private TextView nombre;

    public CarnetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.muro_fragment_carnet, container, false);
        fotoview=(ImageView)view.findViewById(R.id.img_identificacion);
        nombre=(TextView)view.findViewById(R.id.img_nombre_user);
        String urlimag = ((MuroMainActivity)this.getActivity()).fotoperfil.toString();
        String name = ((MuroMainActivity)this.getActivity()).nombreuser.toString();

        nombre.setText(name);
        Glide.with(getActivity())
                .load(urlimag)
                .into(fotoview);

        return view;

    }

}
