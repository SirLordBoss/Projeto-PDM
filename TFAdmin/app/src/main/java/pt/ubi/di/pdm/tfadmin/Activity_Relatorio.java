package pt.ubi.di.pdm.tfadmin;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.text.DateFormat;
import java.util.Calendar;

public class Activity_Relatorio extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase base;
    CheckBox comer, dormir, Wc, curativo, chorar;
    EditText notas;
    TextView nome;
    Button submeter, cancelar;
    String s = "";
    String id_at = "";
    String VerRel = "";
    String id = "";
    int v1 = 0, v2 = 0, v3 = 0, v4 = 0, modo = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        Calendar calendar = Calendar.getInstance();
        String cD = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        Intent Cheguei = getIntent();
        id = Cheguei.getStringExtra("id");

        nome = (TextView) findViewById(R.id.titulo_homeeduc);
        submeter = (Button) findViewById(R.id.btnSubmeterRel);
        cancelar = (Button) findViewById(R.id.btnCancelarRel);
        comer = (CheckBox) findViewById(R.id.cboxComer);
        dormir = (CheckBox) findViewById(R.id.cboxDormir);
        Wc = (CheckBox) findViewById(R.id.cboxWc);
        curativo = (CheckBox) findViewById(R.id.cboxMagoar);
        chorar = (CheckBox) findViewById(R.id.cboxChorar);
        notas = (EditText) findViewById(R.id.editTextTextPersonName);

        Cursor cursor = base.query(DBHelper.TRELATORIO,new String[]{"*"},DBHelper.COL0_TRELATORIO+"=?",new String[]{id},null,null,null);
        cursor.moveToFirst();

        nome.setText(cursor.getString(1));

        notas.setText(cursor.getString(4));

        if(cursor.getString(2).equals("1")){
            comer.setChecked(true);
            Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
            comer.setBackground(d1);
        }
        if(cursor.getString(3).equals("1")){
            dormir.setChecked(true);
            Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
            dormir.setBackground(d1);
        }
        if(cursor.getString(5).equals("1")){
            Wc.setChecked(true);
            Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
            Wc.setBackground(d1);
        }
        if(cursor.getString(6).equals("1")){
            curativo.setChecked(true);
            Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
            curativo.setBackground(d1);
        }
        if(cursor.getString(6).equals("2")){
           chorar.setChecked(true);
            Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
           chorar.setBackground(d1);
        }
        if(cursor.getString(6).equals("3")){
            curativo.setChecked(true);
            Drawable d1 = ResourcesCompat.getDrawable(getResources(),R.drawable.checkmark,null);
            curativo.setBackground(d1);
            chorar.setChecked(true);
            chorar.setBackground(d1);
        }

    }
}
