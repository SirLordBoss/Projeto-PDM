package pt.ubi.di.pdm.titchersfriend;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MarcarFaltas extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase base;
    LinearLayout oLL;
    Button submeter,cancelar;
    String id_at = "";
    ArrayList<String> faltas = new ArrayList<String>();
    ArrayList<String> pres = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcarfaltas);

        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        submeter = (Button)findViewById(R.id.btnSubmeterFaltas);
        cancelar = (Button)findViewById(R.id.btnCancelarFaltas);

        Calendar calendar = Calendar.getInstance();
        String cD = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        displayAlunos();

        id_at = VerificaAtividade(cD);

        if(id_at.equals("vazio")){
            Toast.makeText(MarcarFaltas.this,"Tem que criar uma atividade primeiro",Toast.LENGTH_SHORT).show();
        }


        submeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_at.equals("vazio")){
                    Toast.makeText(MarcarFaltas.this,"Tem que criar uma atividade primeiro",Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor =base.query(dbHelper.TABLE_NAME4,new String[]{"*"},null,null,null,null,null);
                while (cursor.moveToNext()){
                    String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T4));
                    String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T4));
                    for(int i=0;i<faltas.size();i++){
                        if (c2.equals(faltas.get(i))&&c1.equals(id_at)){
                            faltas.remove(i);
                        }
                    }
                    for(int i=0;i<pres.size();i++){
                        if(c2.equals(pres.get(i))&&c1.equals(id_at)){
                            base.delete(dbHelper.TABLE_NAME4,
                                    dbHelper.COL1_T4 + "=?", new String[]{c2});
                        }
                    }
                }

                ContentValues oCV = new ContentValues();
                for (int i=0;i<faltas.size();i++){
                    oCV.put(dbHelper.COL1_T4,faltas.get(i));
                    oCV.put(dbHelper.COL2_T4,id_at);

                    base.insert(dbHelper.TABLE_NAME4,null,oCV);
                    Toast.makeText(MarcarFaltas.this,"Faltas marcadas!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }

    protected void onResume() {
        super.onResume();
        base = dbHelper.getWritableDatabase();
        oLL.removeAllViews();
        displayAlunos();
    }


    public void displayAlunos() {
        oLL = (LinearLayout) findViewById(R.id.verfaltas);
        Cursor oCursor = base.query(dbHelper.TABLE_NAME1, new String[]{"*"}, null, null, null, null, null, null);

        boolean bCarryOn = oCursor.moveToFirst();
        while (bCarryOn) {
            LinearLayout oLL1 = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_faltas, null);
            oLL1.setId(oCursor.getInt(0) * 10 + 2);

            TextView E1 = (TextView) oLL1.findViewById(R.id.nomeAluno);
            E1.setId(oCursor.getInt(0) * 10 + 1);
            E1.setText(oCursor.getString(1));


            final CheckBox oB1 = (CheckBox) oLL1.findViewById(R.id.checkBox);
            oB1.setId(oCursor.getInt(0) * 10);
            if (VerificaFalta(id_at,oCursor.getString(0))){
                oB1.setChecked(true);
                Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
                oB1.setBackground(d1);
            }

            oB1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(oB1.isChecked()){
                        Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
                        oB1.setBackground(d1);
                        faltas.add(String.valueOf((v.getId())/10));
                        pres.remove(String.valueOf((v.getId())/10));

                    }else{
                        Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkbox,null);
                        oB1.setBackground(d1);
                        faltas.remove(String.valueOf((v.getId())/10));
                        pres.add(String.valueOf((v.getId())/10));
                    }
                }
            });


            oLL.addView(oLL1);
            bCarryOn = oCursor.moveToNext();
        }

    }

    public boolean VerificaFalta(String id_at,String id){
        Cursor cursor =base.query(dbHelper.TABLE_NAME4,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){
            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T4));
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T4));
            if(c1.equals(id) && c2.equals(id_at)){
                return true;
            }
        }
        return false;
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
