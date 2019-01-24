package com.cardmax.base.Chat.Grupos;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cardmax.base.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.cardmax.base.Chat.Grupos.Grupov2.GrupoActivityv2;
import com.cardmax.base.Chat.InicioActivity;
import com.cardmax.base.Chat.Recursos.CrearGrupo;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



public class GroupsFragment extends Fragment {
    private View gropFragmentview;
    private ListView list_View;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private DatabaseReference GroupsRef;
    //
    FloatingActionButton fab2;
    private InicioActivity esconder;
    private float y;
    private float y1;

    public GroupsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gropFragmentview = inflater.inflate(R.layout.chat_fragment_groups, container, false);

        GroupsRef = FirebaseDatabase.getInstance().getReference().child("Grupos");

        initializeFields();

        RetriveAndDisplayGroups();

        list_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentGroupName = parent.getItemAtPosition(position).toString();
              //  Intent groupChatIntent = new Intent(getContext(), GroupChatActivityFinal.class);
                Intent groupChatIntent = new Intent(getContext(), GrupoActivityv2.class);
                groupChatIntent.putExtra("groupName", currentGroupName);
                startActivity(groupChatIntent);
            }
        });

        list_View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    y = event.getY();
                    float scroll;
                    scroll = y1 - y;
                    if (scroll > -1) {
                        esconder.baroculta(v);
                        ocultar();
                    } else {
                        esconder.barmuestra1(v);
                        mostrar();
                    }
                }
                return false;
            }
        });
        //iniciar();
        esconder = (InicioActivity) getActivity();
        esconder.barmuestra1(gropFragmentview);

        mostrar1();

        return gropFragmentview;
    }

    private void mostrar1() {

        esconder.barmuestra1(gropFragmentview);
    }
    private void ocultar() {
        fab2.setVisibility(View.INVISIBLE);
        fab2.setEnabled(false);
    }

    private void mostrar() {
        fab2.setVisibility(View.VISIBLE);
        fab2.setEnabled(true);
    }


    private void initializeFields() {
        list_View = gropFragmentview.findViewById(R.id.list_view_fragment);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
        list_View.setAdapter(arrayAdapter);
        fab2 = gropFragmentview.findViewById(R.id.fab_grupo1);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(esconder, "si sirvio", Toast.LENGTH_SHORT).show();
              /*  esconder.SeRequestNewGroup();
*/
                CrearGrupo crear=new CrearGrupo();
                crear.grupo(getContext());
            }
        });

        mostrar();

    }

    private void RetriveAndDisplayGroups() {
        GroupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
                }
                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
