package com.numa.cardmax.numapp.Protocolo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.numa.cardmax.numapp.R;

public class ProteccionMercancia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_proteccion_mercancia);
        setActionBar();
    }

    private void setActionBar() {
    setTitle("Protección de mercancia");

    }
}
