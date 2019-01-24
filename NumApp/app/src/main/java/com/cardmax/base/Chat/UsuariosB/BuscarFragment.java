
package com.cardmax.base.Chat.UsuariosB;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cardmax.base.R;


public class BuscarFragment extends Fragment {

    private RecyclerView rv;
    private Button searchButton;
    private EditText searchInputText;
    private RecyclerView searchResultList;

    public BuscarFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.chat_fragment_buscar, container, false);
    }

}

