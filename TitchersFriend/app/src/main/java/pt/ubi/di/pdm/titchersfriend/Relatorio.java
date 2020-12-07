package pt.ubi.di.pdm.titchersfriend;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Relatorio extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase base;
    CheckBox comer,dormir,Wc,curativo;
    EditText notas;
    Button submeter,cancelar;
    String s = "";
    String id_at = "";
    String VerRel = "";
    int v1=0,v2=0,v3=0,v4=0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        Calendar calendar = Calendar.getInstance();
        String cD = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        id_at = VerificaAtividade(cD);
        VerRel = VerificaRelatorio("1",cD);
        if(id_at.equals("vazio")){
            Toast.makeText(Relatorio.this,"Tem que criar uma atividade primeiro",Toast.LENGTH_SHORT).show();
        }
        submeter = (Button) findViewById(R.id.btnSubmeterRel);
        cancelar = (Button) findViewById(R.id.btnCancelarRel);
        comer = (CheckBox)findViewById(R.id.cboxComer);
        dormir = (CheckBox)findViewById(R.id.cboxDormir);
        Wc = (CheckBox)findViewById(R.id.cboxWc);
        curativo = (CheckBox)findViewById(R.id.cboxMagoar);
        notas = (EditText) findViewById(R.id.editTextTextPersonName);
        comer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comer.isChecked()){
                    Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
                    /*comer.setCompoundDrawablesWithIntrinsicBounds(null,d1,null,null);
                    Toast.makeText(Relatorio.this,"Clicou aqui",Toast.LENGTH_SHORT).show();*/
                    comer.setBackground(d1);
                }else{
                    Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkbox,null);
                    comer.setBackground(d1);
                }
            }
        });

        submeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id_at.equals("vazio")){
                    Toast.makeText(Relatorio.this,"Tem que criar uma atividade primeiro",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(VerRel.equals("vazio")){
                    Toast.makeText(Relatorio.this,"Já fez um relatorio para este aluno",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(comer.isChecked()){
                    v1 = 1;
                }
                if(dormir.isChecked()){
                    v2 = 1;
                }
                if(Wc.isChecked()){
                    v3 = 1;
                }
                if(curativo.isChecked()){
                    v4 = 1;
                }
                s = notas.getText().toString();

                ContentValues oCV = new ContentValues();

                oCV.put(dbHelper.COL1_T5,v1);
                oCV.put(dbHelper.COL2_T5,v2);
                oCV.put(dbHelper.COL3_T5,s);
                oCV.put(dbHelper.COL4_T5,v3);
                oCV.put(dbHelper.COL5_T5,v4);
                oCV.put(dbHelper.COL6_T5,1);
                oCV.put(dbHelper.COL7_T5,id_at);

                base.insert(dbHelper.TABLE_NAME5,null,oCV);
                Intent i = new Intent(Relatorio.this,ConferelatorioActivity.class);
                i.putExtra("comer",v1);
                i.putExtra("dormir",v2);
                i.putExtra("notas",s);
                i.putExtra("Wc",v3);
                i.putExtra("Curativo",v4);

                startActivity(i);

                //Toast.makeText(Relatorio.this,"Sucesso",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public String VerificaRelatorio(String id,String date){

        Cursor cursor =base.query(dbHelper.TABLE_NAME5,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL6_T5));
            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL7_T5));
            if (c2.equals(id)){
                if(c1.equals(VerificaAtividade(date)))
                    return "vazio";
            }
        }
        return "ok";
    }


    public String VerificaAtividade(String date){

        Cursor cursor =base.query(dbHelper.TABLE_NAME2,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T2));
            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL3_T2));
            if (c1.equals(date)){
                return c2;
            }
        }
        return "vazio";
    }

}

