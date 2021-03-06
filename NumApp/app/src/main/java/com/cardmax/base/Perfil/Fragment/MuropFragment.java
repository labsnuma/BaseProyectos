package com.cardmax.base.Perfil.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Muro.Adaptadores.AdaptadorPrincipal;
import com.cardmax.base.Muro.Objetos.ObjetoMuro;
import com.cardmax.base.Perfil.PerfilMainActivity;
import com.cardmax.base.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuropFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuropFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuropFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView contenedorx;
    private ArrayList<ObjetoMuro> Lista;
    private AdaptadorPrincipal xx;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MuropFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuropFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MuropFragment newInstance(String param1, String param2) {
        MuropFragment fragment = new MuropFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.perfil_fragment_murop, container, false);
       contenedorx = (RecyclerView) v.findViewById(R.id.recyclerview_perfil2);
         mDatabase = FirebaseDatabase.getInstance().getReference();
        Lista = new ArrayList<ObjetoMuro>();
        xx = new AdaptadorPrincipal(Lista);
        contenedorx.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity().getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        contenedorx.setAdapter(xx);
        contenedorx.setLayoutManager(layout);
        ViewCompat.setNestedScrollingEnabled(contenedorx, false);

        String iduser = ((PerfilMainActivity)this.getActivity()).iduser.toString();

        System.out.println("datos user ====  "+iduser);
        String x = iduser;



        final Query q = mDatabase.child("muro_publicaciones")
                .orderByChild("id_usuario")
                .equalTo(iduser).limitToLast(10);


        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista.removeAll(Lista);
                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                int count = 0;

                for (DataSnapshot murox : muroChildren) {
                    ObjetoMuro p = murox.getValue(ObjetoMuro.class);
                    Lista.add(0, p);
                    count += 1;
                }
                xx.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        q.keepSynced(true);
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
