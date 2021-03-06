package com.cardmax.base.Perfil.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Perfil.AdaptadorServ.AdaptadorServicios;
import com.cardmax.base.Perfil.AdaptadorServ.ObjetoServicio;
import com.cardmax.base.Perfil.AddServ;
import com.cardmax.base.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiciosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiciosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiciosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private RecyclerView lista;
    private ArrayList<ObjetoServicio> listobject;
    private AdaptadorServicios calladapter;
    private Button agregarserv;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public String currentUserID = mAuth.getCurrentUser().getUid();
    private Calendar c = Calendar.getInstance();
    private DatabaseReference mDatabase;

    public ServiciosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiciosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiciosFragment newInstance(String param1, String param2) {
        ServiciosFragment fragment = new ServiciosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.perfil_fragment_servicios, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        lista = (RecyclerView)v.findViewById(R.id.recycler_serv);
        listobject =new ArrayList<ObjetoServicio>();
        calladapter =  new AdaptadorServicios(listobject);
        lista.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        lista.setAdapter(calladapter);
        lista.setLayoutManager(layout);
        agregarserv=(Button)v.findViewById(R.id.btn_addserv) ;

        agregarserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento;
                intento = new Intent(getContext(), AddServ.class);
                intento.putExtra("tipo", "0");
                startActivity(intento);
            }
        });


        Query consulta_servicios  = mDatabase.child("perfil_servicios")
                .orderByChild("iduser_serv")
                .equalTo(currentUserID);

       consulta_servicios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                listobject.removeAll(listobject);
                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                int count = 0;

                for (DataSnapshot murox : muroChildren) {
                    ObjetoServicio p = murox.getValue(ObjetoServicio.class);


                        listobject.add(0, p);

                    count += 1;
                }
                calladapter.notifyDataSetChanged();

                }catch (Exception e ){
                    System.out.println(e);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        consulta_servicios.keepSynced(true);



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
