package pt.ubi.di.pdm.tfadmin;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_GerirFaltas extends AppCompatActivity {

    Spinner dia,mes,ano;
    String data = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerirfaltas);

        dia =(Spinner)findViewById(R.id.inputDia);
        mes =(Spinner)findViewById(R.id.inputMes);
        ano =(Spinner)findViewById(R.id.inputAno);

        String[] itemsDia = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_GerirFaltas.this, android.R.layout.simple_spinner_dropdown_item, itemsDia);
        dia.setAdapter(adapter);

        String[] itemsMes = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Activity_GerirFaltas.this, android.R.layout.simple_spinner_dropdown_item, itemsMes);
        mes.setAdapter(adapter2);

        String[] itemsAno = new String[]{"2020","2021"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Activity_GerirFaltas.this, android.R.layout.simple_spinner_dropdown_item, itemsAno);
        ano.setAdapter(adapter3);

        data = dia.getSelectedItem().toString() + "/" + mes.getSelectedItem().toString() + "/" + ano.getSelectedItem().toString();
        Log.d("tag",data);

    }




}
