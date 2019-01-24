package com.numa.cardmax.numapp.Protocolo;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.numa.cardmax.numapp.R;

import java.util.Calendar;

public class ReporteNovedad extends AppCompatActivity implements View.OnClickListener {

    Button bFecha, bHora;
    EditText efecha, ehora;
    private int dia, mes, ano, hora, minutos, segundos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protocolo_reporte_novedad);

        bFecha  =   (Button)    findViewById(R.id.btn_Fecha);
        bHora   =   (Button)    findViewById(R.id.btn_hora);
        efecha  =   (EditText)  findViewById(R.id.tx_Fecha);
        ehora   =   (EditText)  findViewById(R.id.tx_Hora);

        bFecha.setOnClickListener(this);
        bHora.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if  (v==bFecha){
            final   Calendar    c=   Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);


            //Crear instancia para la hora
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    efecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                }
            }
            ,dia,mes,ano);
            datePickerDialog.show();

        }
        if (v==bHora){
            final Calendar c= Calendar.getInstance();
            hora=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);
            segundos=c.get(Calendar.SECOND);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ehora.setText(hourOfDay+":"+minute);

                }
            },hora, minutos,false);
                timePickerDialog.show();
        }
    }
}
