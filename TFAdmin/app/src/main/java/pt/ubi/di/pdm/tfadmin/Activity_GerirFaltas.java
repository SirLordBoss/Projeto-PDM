package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_GerirFaltas extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase base;
    Spinner dia,mes,ano;
    String data = "",i,tk;
    int id,e_id;
    Button pesquisa;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerirfaltas);

        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        Log.d("tag","1");
        SharedPreferences shp = getApplicationContext().getSharedPreferences("important_variables",0);
        id = shp.getInt("id",999);

        Log.d("tag","1");
        Intent Cheguei = getIntent();
        i = Cheguei.getStringExtra("id");
        e_id = Integer.parseInt(i);

        Cursor cursor = base.query(DBHelper.TEDUCADOR,new String[]{DBHelper.COL7_TEDUCADOR},DBHelper.COL1_TEDUCADOR+"=?",new String[]{String.valueOf(e_id)},null,null,null);
        cursor.moveToFirst();
        tk = cursor.getString(0);
        cursor.close();

        dia =(Spinner)findViewById(R.id.inputDia);
        mes =(Spinner)findViewById(R.id.inputMes);
        ano =(Spinner)findViewById(R.id.inputAno);
        pesquisa = (Button)findViewById(R.id.button);

        String[] itemsDia = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_GerirFaltas.this, android.R.layout.simple_spinner_dropdown_item, itemsDia);
        dia.setAdapter(adapter);

        String[] itemsMes = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Activity_GerirFaltas.this, android.R.layout.simple_spinner_dropdown_item, itemsMes);
        mes.setAdapter(adapter2);

        String[] itemsAno = new String[]{"20","21"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Activity_GerirFaltas.this, android.R.layout.simple_spinner_dropdown_item, itemsAno);
        ano.setAdapter(adapter3);

        Log.d("tag",String.valueOf(id));
        Log.d("tag",String.valueOf(e_id));
        Log.d("tag",tk);

        pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = dia.getSelectedItem().toString() + "/" + mes.getSelectedItem().toString() + "/" + ano.getSelectedItem().toString();
                Log.d("tag",data);

                dbHelper.updateFalta(base,id,e_id,tk,data);

            }
        });

    }




}
