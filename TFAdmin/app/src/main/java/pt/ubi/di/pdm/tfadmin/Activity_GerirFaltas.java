package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class Activity_GerirFaltas extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase base;
    Cursor oCursor;
    LinearLayout oLL;
    Spinner dia,mes,ano;
    String data = "",i,tk;
    int id,e_id;
    Button pesquisa,submeter,cancelar;
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
        submeter = (Button)findViewById(R.id.btnSubmeterRel);
        cancelar = (Button)findViewById(R.id.btnCancelarRel);


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
                displayFaltas();
            }
        });

        submeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void displayFaltas() {
        oLL = (LinearLayout) findViewById(R.id.visualizar);
        oLL.removeAllViews();
        oCursor = base.query(DBHelper.TFALTA, new String[]{"*"}, null, null, null, null, null, null);
        boolean bCarryOn = oCursor.moveToFirst();
        while (bCarryOn) {
            LinearLayout oLL1 = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_faltas, null);
            oLL1.setId(oCursor.getInt(0) * 10 + 2);

            TextView E1 = (TextView) oLL1.findViewById(R.id.nomeAluno);
            E1.setId(oCursor.getInt(0) * 10 );
            E1.setText(oCursor.getString(1));

            CheckBox B1 = (CheckBox) oLL1.findViewById(R.id.checkBox);
            B1.setId(oCursor.getInt(0)*10+1);

            if(oCursor.getString(2).equals("1")) {
                B1.setChecked(true);
                Drawable d1 = ResourcesCompat.getDrawable(getResources(), R.drawable.checkmark, null);
                B1.setBackground(d1);
            }

            B1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(B1.isChecked()){
                        Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
                        B1.setBackground(d1);
                    }else{
                        Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkbox,null);
                        B1.setBackground(d1);
                    }
                }
            });


            oLL.addView(oLL1);
            bCarryOn = oCursor.moveToNext();
        }

    }




}
