package com.cardmax.base.Protocolo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.cardmax.base.R;

public class SeguridadElectronica extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_seguridad_electronica);
        setActionBar();
    }

    private void setActionBar() {
    setTitle("Seguridad electrónica");

    }
}
