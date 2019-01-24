package com.numa.cardmax.numapp.Muro.Fragmentos;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.numa.cardmax.numapp.Muro.MuroMainActivity;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoMuro;
import com.numa.cardmax.numapp.Muro.Adaptadores.AdaptadorPrincipal;
import com.numa.cardmax.numapp.Muro.Objetos.ObjetoUser;
import com.numa.cardmax.numapp.R;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MuroFragment extends Fragment implements RecyclerView.OnTouchListener {

    private DatabaseReference mDatabase, mDatabase2;
    private RecyclerView contenedorx, contenedorx2, contenedorx3;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ObjetoMuro> Lista, Lista2, Lista3;
    private AdaptadorPrincipal xx, xx2, xx3;
    private long startClickTime;
    private float y;
    private float y1;
    private MuroMainActivity esconder;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView ns;
    private SwipyRefreshLayout refreshBottom;
    private ScrollView scrollx;
    private TextView finalx;
    private FirebaseAuth mAuth;
    private Calendar c = Calendar.getInstance();
    private int fecha_actual;
    public String currentUserID = "";
    public String info_programas = "";
    public String info_ciudades = "";
    public String info_genero = "";
    public String info_intereses = "";
    public String info_estadocivil = "";
    public int info_edad = 0;
    public Integer info_semestre = 0;


    private FirebaseUser currentUser;
    private DatabaseReference UserRef;


    public MuroFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserID = mAuth.getCurrentUser().getUid();
        fecha_actual = c.get(Calendar.YEAR);
        view = inflater.inflate(R.layout.muro_fragment_muro, container, false);
        setHasOptionsMenu(true);

        scrollx = (ScrollView) view.findViewById(R.id.nuevoscroll);
        finalx = (TextView) view.findViewById(R.id.final_txt);

        esconder = (MuroMainActivity) getActivity();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("muro_publicaciones");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");

        Lista = new ArrayList<ObjetoMuro>();
        xx = new AdaptadorPrincipal(Lista);
        contenedorx = (RecyclerView) view.findViewById(R.id.recycler_muro);
        contenedorx2 = (RecyclerView) view.findViewById(R.id.recycler_muro2);
        contenedorx3 = (RecyclerView) view.findViewById(R.id.recycler_muro3);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        contenedorx.setHasFixedSize(true);

        contenedorx.setItemViewCacheSize(20);
        contenedorx.setDrawingCacheEnabled(true);
        contenedorx.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);

        contenedorx.setAdapter(xx);
        contenedorx.setLayoutManager(layout);
        ViewCompat.setNestedScrollingEnabled(contenedorx, false);
        ViewCompat.setNestedScrollingEnabled(contenedorx2, false);
        ViewCompat.setNestedScrollingEnabled(contenedorx3, false);
        ns = (NestedScrollView) view.findViewById(R.id.nest);
        refreshBottom = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);


        mDatabase2.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ObjetoUser userx = dataSnapshot.getValue(ObjetoUser.class);

                    info_programas = userx.carrera;
                    info_ciudades = userx.ciudad;
                    info_estadocivil = userx.estado_civil;
                    info_edad = userx.fecha;
                    info_genero = userx.genero;
                    info_intereses = userx.intereses;
                    info_semestre = userx.semestre;
                } catch (Exception e) {
                    System.out.println("array -> " + e);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDatabase.limitToLast(50)//50
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                        int contador = 0;
                        Lista = new ArrayList<ObjetoMuro>();
                        xx = new AdaptadorPrincipal(Lista);
                        contenedorx.setHasFixedSize(true);
                        contenedorx.setItemViewCacheSize(20);
                        contenedorx.setDrawingCacheEnabled(true);
                        contenedorx.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                        LinearLayoutManager layout = new LinearLayoutManager(getContext());
                        layout.setOrientation(LinearLayoutManager.VERTICAL);
                        contenedorx.setAdapter(xx);
                        contenedorx.setLayoutManager(layout);
                        Lista.removeAll(Lista);


                        for (DataSnapshot murox : muroChildren) {
                            try {
                                ObjetoMuro p = murox.getValue(ObjetoMuro.class);
                                System.out.println(p);
                                int topmuro;
                                topmuro = p.top;
                                String semestre = p.semestre.replace(", ", ",");
                                String intereses = p.intereses.replace(", ", ",");
                                String ciudades = p.ciudades.replace(", ", ",");
                                String programas = p.programas.replace(", ", ",");
                                String[] semestresplit = semestre.split(",");
                                String[] interesplit = intereses.split(",");
                                String[] ciudadessplit = ciudades.split(",");
                                String[] programassplit = programas.split(",");

                                if (contador > 1) {
                                    if (topmuro == 1) {
                                        Lista.remove(0);

                                        for (String valuesemestre : semestresplit) {
                                            for (String valueinteres : interesplit) {
                                                for (String valueciudades : ciudadessplit) {
                                                    for (String valueprogram : programassplit) {
                                                        filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p.genero, valueinteres, p.estado_civil, p.edadini, p.edadfin, Lista, p, 0);

                                                    }
                                                }

                                            }
                                        }


                                    } else {

                                        for (String valuesemestre : semestresplit) {
                                            for (String valueinteres : interesplit) {
                                                for (String valueciudades : ciudadessplit) {
                                                    for (String valueprogram : programassplit) {
                                                        filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p.genero, valueinteres, p.estado_civil, p.edadini, p.edadfin, Lista, p, 1);

                                                    }
                                                }

                                            }
                                        }



                                    }
                                } else {


                                    for (String valuesemestre : semestresplit) {
                                        for (String valueinteres : interesplit) {
                                            for (String valueciudades : ciudadessplit) {
                                                for (String valueprogram : programassplit) {
                                                    filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p.genero, valueinteres, p.estado_civil, p.edadini, p.edadfin, Lista, p, 0);

                                                }
                                            }

                                        }
                                    }



                                }
                                contador = contador + 1;


                            } catch (Exception e) {
                                System.out.println("hooooooola" + e);
                            }


                        }
                        try {
                            if (Lista.size() > 15) {
                                Lista.subList(15, Lista.size()).clear();

                            }

                            xx.notifyDataSetChanged();
                        } catch (Exception e) {
                            System.out.println("array2 -->" + e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        final int[] button_refresh = {0};
        refreshBottom.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {


                if (button_refresh[0] == 0) {

                    mDatabase.limitToLast(25)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                                    int contador = 0;
                                    Lista2 = new ArrayList<ObjetoMuro>();
                                    xx2 = new AdaptadorPrincipal(Lista2);

                                    contenedorx2.setHasFixedSize(true);
                                    contenedorx2.setItemViewCacheSize(20);
                                    contenedorx2.setDrawingCacheEnabled(true);
                                    contenedorx2.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                                    LinearLayoutManager layout2 = new LinearLayoutManager(getContext());
                                    layout2.setOrientation(LinearLayoutManager.VERTICAL);
                                    contenedorx2.setAdapter(xx2);
                                    contenedorx2.setLayoutManager(layout2);
                                    Lista2.removeAll(Lista2);


                                    for (DataSnapshot murox : muroChildren) {
                                        contador = contador + 1;
                                    }
                                    Iterable<DataSnapshot> muroChildren1 = dataSnapshot.getChildren();
                                    if ((contador - 15) == 10) {
                                        contador = 0;
                                        for (DataSnapshot murox : muroChildren1) {
                                            ObjetoMuro p2 = murox.getValue(ObjetoMuro.class);
                                            contador = contador + 1;

                                            String semestre = p2.semestre.replace(", ", ",");
                                            String intereses = p2.intereses.replace(", ", ",");
                                            String ciudades = p2.ciudades.replace(", ", ",");
                                            String programas = p2.programas.replace(", ", ",");
                                            String[] semestresplit = semestre.split(",");
                                            String[] interesplit = intereses.split(",");
                                            String[] ciudadessplit = ciudades.split(",");
                                            String[] programassplit = programas.split(",");

                                            if (contador <= 10) {

                                                for (String valuesemestre : semestresplit) {
                                                    for (String valueinteres : interesplit) {
                                                        for (String valueciudades : ciudadessplit) {
                                                            for (String valueprogram : programassplit) {
                                                                filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p2.genero, valueinteres, p2.estado_civil, p2.edadini, p2.edadfin, Lista2, p2, 0);

                                                            }
                                                        }

                                                    }
                                                }



                                            }

                                        }
                                    } else if ((contador - 15) < 10 && contador > 14) {
                                        int x = contador - 15, countint = 0;
                                        contador = 0;
                                        try {
                                            for (DataSnapshot murox : muroChildren1) {

                                                ObjetoMuro p2 = murox.getValue(ObjetoMuro.class);
                                                contador = contador + 1;
                                                String semestre = p2.semestre.replace(", ", ",");
                                                String intereses = p2.intereses.replace(", ", ",");
                                                String ciudades = p2.ciudades.replace(", ", ",");
                                                String programas = p2.programas.replace(", ", ",");
                                                String[] semestresplit = semestre.split(",");
                                                String[] interesplit = intereses.split(",");
                                                String[] ciudadessplit = ciudades.split(",");
                                                String[] programassplit = programas.split(",");

                                                if (contador <= 10) {


                                                    for (String valuesemestre : semestresplit) {
                                                        for (String valueinteres : interesplit) {
                                                            for (String valueciudades : ciudadessplit) {
                                                                for (String valueprogram : programassplit) {
                                                                    filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p2.genero, valueinteres, p2.estado_civil, p2.edadini, p2.edadfin, Lista2, p2, 0);

                                                                }
                                                            }

                                                        }
                                                    }


                                                    countint += 1;

                                                    if (countint == x) {
                                                        break;
                                                    }

                                                }


                                            }
                                        } catch (Exception e) {

                                            System.out.println("Tengo un error ->" + e);
                                        }


                                    }


                                    xx2.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                    for (int i = 0; i < 7000; i++) {

                        if (i == 6999) {

                            refreshBottom.setRefreshing(false);
                        }

                    }

                    button_refresh[0] = button_refresh[0] + 1;


                } else if (button_refresh[0] == 1) {


                    mDatabase.limitToLast(35)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                                    int contador = 0;
                                    Lista3 = new ArrayList<ObjetoMuro>();
                                    xx3 = new AdaptadorPrincipal(Lista3);

                                    contenedorx3.setHasFixedSize(true);
                                    contenedorx3.setItemViewCacheSize(20);
                                    contenedorx3.setDrawingCacheEnabled(true);
                                    contenedorx3.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                                    LinearLayoutManager layout3 = new LinearLayoutManager(getContext());
                                    layout3.setOrientation(LinearLayoutManager.VERTICAL);
                                    contenedorx3.setAdapter(xx3);
                                    contenedorx3.setLayoutManager(layout3);
                                    Lista3.removeAll(Lista3);
                                    for (DataSnapshot murox : muroChildren) {

                                        contador = contador + 1;
                                    }

                                    Iterable<DataSnapshot> muroChildren2 = dataSnapshot.getChildren();


                                    if ((contador - 25) == 10) {
                                        contador = 0;
                                        for (DataSnapshot murox : muroChildren2) {
                                            ObjetoMuro p3 = murox.getValue(ObjetoMuro.class);
                                            String semestre = p3.semestre.replace(", ", ",");
                                            String intereses = p3.intereses.replace(", ", ",");
                                            String ciudades = p3.ciudades.replace(", ", ",");
                                            String programas = p3.programas.replace(", ", ",");
                                            String[] semestresplit = semestre.split(",");
                                            String[] interesplit = intereses.split(",");
                                            String[] ciudadessplit = ciudades.split(",");
                                            String[] programassplit = programas.split(",");



                                            contador = contador + 1;

                                            if (contador <= 10) {

                                                for (String valuesemestre : semestresplit) {
                                                    for (String valueinteres : interesplit) {
                                                        for (String valueciudades : ciudadessplit) {
                                                            for (String valueprogram : programassplit) {
                                                                filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p3.genero, valueinteres, p3.estado_civil, p3.edadini, p3.edadfin, Lista3, p3, 0);

                                                            }
                                                        }

                                                    }
                                                }


                                            }
                                            if (contador == 10) {
                                                break;
                                            }

                                        }
                                    } else if ((contador - 25) < 10 && contador > 25) {

                                        int x = contador - 25, countint = 0;
                                        contador = 0;
                                        for (DataSnapshot murox : muroChildren2) {
                                            ObjetoMuro p3 = murox.getValue(ObjetoMuro.class);
                                            String semestre = p3.semestre.replace(", ", ",");
                                            String intereses = p3.intereses.replace(", ", ",");
                                            String ciudades = p3.ciudades.replace(", ", ",");
                                            String programas = p3.programas.replace(", ", ",");
                                            String[] semestresplit = semestre.split(",");
                                            String[] interesplit = intereses.split(",");
                                            String[] ciudadessplit = ciudades.split(",");
                                            String[] programassplit = programas.split(",");


                                            contador = contador + 1;

                                            if (contador <= 10) {

                                                for (String valuesemestre : semestresplit) {
                                                    for (String valueinteres : interesplit) {
                                                        for (String valueciudades : ciudadessplit) {
                                                            for (String valueprogram : programassplit) {
                                                                filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p3.genero, valueinteres, p3.estado_civil, p3.edadini, p3.edadfin, Lista3, p3, 0);

                                                            }
                                                        }

                                                    }
                                                }


                                                countint += 1;
                                            }
                                            if (countint == x) {
                                                break;
                                            }

                                        }

                                    }

                                    xx3.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                    button_refresh[0] = button_refresh[0] + 1;

                    for (int i = 0; i < 7000; i++) {

                        if (i == 6999) {

                            refreshBottom.setRefreshing(false);
                        }

                    }

                } else if (button_refresh[0] > 1) {

                    button_refresh[0] = button_refresh[0] + 1;
                    for (int i = 0; i < 5000; i++) {

                        if (i == 4999) {

                            refreshBottom.setRefreshing(false);
                            finalx.setText("Fin");
                            finalx.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.megusta, 0);
                        }

                    }


                }


            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                button_refresh[0] = 0;
                if (Lista2 != null) {
                    Lista2.removeAll(Lista2);
                    xx2.notifyDataSetChanged();
                    finalx.setText("Cargar mas");
                    finalx.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_file_upload_black_24dp, 0);

                }

                if (Lista3 != null) {
                    Lista3.removeAll(Lista3);
                    xx3.notifyDataSetChanged();
                }


                mDatabase.limitToLast(50)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> muroChildren = dataSnapshot.getChildren();
                                int contador = 0;
                                Lista = new ArrayList<ObjetoMuro>();
                                xx = new AdaptadorPrincipal(Lista);
                                contenedorx.setHasFixedSize(true);
                                LinearLayoutManager layout = new LinearLayoutManager(getContext());
                                layout.setOrientation(LinearLayoutManager.VERTICAL);
                                contenedorx.setAdapter(xx);
                                contenedorx.setLayoutManager(layout);
                                Lista.removeAll(Lista);


                                for (DataSnapshot murox : muroChildren) {
                                    ObjetoMuro p = murox.getValue(ObjetoMuro.class);
                                    int topmuro;
                                    topmuro = p.top;
                                    String semestre = p.semestre.replace(", ", ",");
                                    String intereses = p.intereses.replace(", ", ",");
                                    String ciudades = p.ciudades.replace(", ", ",");
                                    String programas = p.programas.replace(", ", ",");
                                    String[] semestresplit = semestre.split(",");
                                    String[] interesplit = intereses.split(",");
                                    String[] ciudadessplit = ciudades.split(",");
                                    String[] programassplit = programas.split(",");

                                    if (contador > 1) {
                                        if (topmuro == 1) {

                                            for (String valuesemestre : semestresplit) {
                                                for (String valueinteres : interesplit) {
                                                    for (String valueciudades : ciudadessplit) {
                                                        for (String valueprogram : programassplit) {
                                                            filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p.genero, valueinteres, p.estado_civil, p.edadini, p.edadfin, Lista, p, 0);

                                                        }
                                                    }

                                                }
                                            }


                                        } else {

                                            for (String valuesemestre : semestresplit) {
                                                for (String valueinteres : interesplit) {
                                                    for (String valueciudades : ciudadessplit) {
                                                        for (String valueprogram : programassplit) {
                                                            filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p.genero, valueinteres, p.estado_civil, p.edadini, p.edadfin, Lista, p, 1);

                                                        }
                                                    }

                                                }
                                            }


                                        }
                                    } else {


                                        for (String valuesemestre : semestresplit) {
                                            for (String valueinteres : interesplit) {
                                                for (String valueciudades : ciudadessplit) {
                                                    for (String valueprogram : programassplit) {
                                                        filtro(valueprogram, Integer.parseInt(valuesemestre), valueciudades, p.genero, valueinteres, p.estado_civil, p.edadini, p.edadfin, Lista, p, 0);

                                                    }
                                                }

                                            }
                                        }

                                    }
                                    contador = contador + 1;


                                }
                                try {
                                    if (Lista.size() > 15) {

                                        Lista.subList(15, Lista.size()).clear();


                                    }

                                    xx.notifyDataSetChanged();
                                } catch (Exception e) {
                                    System.out.println("array2 -->" + e);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                Toast.makeText(getContext(), "Actualizando...", Toast.LENGTH_SHORT).show();
                mDatabase.keepSynced(true);
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        ns.setOnTouchListener(new View.OnTouchListener() {
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
                    } else {
                        esconder.barmuestra(v);

                    }

                }


                return false;
            }
        });


        return view;

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    public void filtro(String programas, int semestre, String ciudades, String genero, String intereses, String estado_civil, int edadini, int edadfinal, ArrayList<ObjetoMuro> Lista, ObjetoMuro p, int ubicacion) {

        try {
            if (programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (ubicacion == 3) {
                    Lista.add(p);
                } else {
                    Lista.add(ubicacion, p);
                }
            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre > 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre > 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && ciudades.equals(info_ciudades)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre > 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades) && semestre == info_semestre && ciudades.equals(info_ciudades)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre > 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && ciudades.equals(info_ciudades)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (genero.equals(info_genero)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && genero.equals(info_genero)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && genero.equals(info_genero)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && genero.equals(info_genero)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades) && genero.equals(info_genero)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && ciudades.equals(info_ciudades) && genero.equals(info_genero)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && ciudades.equals(info_ciudades) && genero.equals(info_genero)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && ciudades.equals(info_ciudades) && genero.equals(info_genero)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && ciudades.equals(info_ciudades) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && ciudades.equals(info_ciudades) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && ciudades.equals(info_ciudades) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (genero.equals(info_genero) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && genero.equals(info_genero) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && genero.equals(info_genero) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && genero.equals(info_genero) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades) && genero.equals(info_genero) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && ciudades.equals(info_ciudades) && genero.equals(info_genero) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && ciudades.equals(info_ciudades) && genero.equals(info_genero) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && ciudades.equals(info_ciudades) && genero.equals(info_genero) && intereses.equals(info_intereses)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && ciudades.equals(info_ciudades) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && ciudades.equals(info_ciudades) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && ciudades.equals(info_ciudades) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (genero.equals(info_genero) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && genero.equals(info_genero) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && genero.equals(info_genero) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && genero.equals(info_genero) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades) && genero.equals(info_genero) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && ciudades.equals(info_ciudades) && genero.equals(info_genero) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && ciudades.equals(info_ciudades) && genero.equals(info_genero) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && ciudades.equals(info_ciudades) && genero.equals(info_genero) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && ciudades.equals(info_ciudades) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && ciudades.equals(info_ciudades) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && ciudades.equals(info_ciudades) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (genero.equals(info_genero) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && genero.equals(info_genero) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && genero.equals(info_genero) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && genero.equals(info_genero) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (ciudades.equals(info_ciudades) && genero.equals(info_genero) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && ciudades.equals(info_ciudades) && genero.equals(info_genero) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (semestre == info_semestre && ciudades.equals(info_ciudades) && genero.equals(info_genero) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini == 0) {
                if (programas.equals(info_programas) && semestre == info_semestre && ciudades.equals(info_ciudades) && genero.equals(info_genero) && intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {

                if (edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (ciudades.equals(info_ciudades) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        ciudades.equals(info_ciudades) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        ciudades.equals(info_ciudades) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (genero.equals(info_genero) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        genero.equals(info_genero) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        genero.equals(info_genero) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        genero.equals(info_genero) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {

                if (intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {

                if (semestre == info_semestre &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (ciudades.equals(info_ciudades) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        ciudades.equals(info_ciudades) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        ciudades.equals(info_ciudades) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (genero.equals(info_genero) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        genero.equals(info_genero) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        genero.equals(info_genero) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (ciudades.equals(info_ciudades) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        ciudades.equals(info_ciudades) &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && genero.equals(info_genero) &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        genero.equals(info_genero) &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        genero.equals(info_genero) &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        genero.equals(info_genero) &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (ciudades.equals(info_ciudades) && genero.equals(info_genero) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) &&
                        ciudades.equals(info_ciudades) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        ciudades.equals(info_ciudades) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {

                if (genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }

            } else if (programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre == 0 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                if (semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        edadini >= (info_edad - fecha_actual) && edadfinal <= (info_edad - fecha_actual)) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            } else if (!programas.equals("") && semestre >= 1 && !ciudades.equals("") && !genero.equals("") && !intereses.equals("") && !estado_civil.equals("") && edadini > 1) {
                int x = fecha_actual - info_edad;

                if (programas.equals(info_programas) && semestre == info_semestre &&
                        ciudades.equals(info_ciudades) && genero.equals(info_genero) &&
                        intereses.equals(info_intereses) && estado_civil.equals(info_estadocivil) &&
                        x > edadini && x < edadfinal) {
                    if (ubicacion == 3) {
                        Lista.add(p);
                    } else {
                        Lista.add(ubicacion, p);
                    }
                }


            }
        } catch (Exception e) {

            System.out.println("array erre ->" + e);

        }
    }


}
