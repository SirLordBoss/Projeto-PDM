package pt.ubi.di.pdm.titchersfriend;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditAlunoActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase base;
    EditText nome,idade,morada,contacto;
    Spinner sexo;
    Button submeter,cancelar;
    String id = "";
    int a1;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaraluno);

        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        Intent Cheguei = getIntent();
        id = Cheguei.getStringExtra("id");

        submeter = (Button)findViewById(R.id.btnRelatorio);
        cancelar = (Button)findViewById(R.id.btnCancelarRel);

        nome = (EditText) findViewById(R.id.editNome);
        idade = (EditText) findViewById(R.id.editIdade);
        sexo = (Spinner) findViewById(R.id.editSexo);
        morada = (EditText) findViewById(R.id.editMorada);
        contacto = (EditText) findViewById(R.id.editContacto);

        String[] items = new String[]{"Sexo: Feminino", "Sexo: Masculino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditAlunoActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        sexo.setAdapter(adapter);
        sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a1 = sexo.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        Cursor cursor =base.query(dbHelper.TABLE_NAME1,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){
            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T1));
            if (c1.equals(id)){
                nome.setText(cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T1)));
                idade.setText(cursor.getString(cursor.getColumnIndex(dbHelper.COL3_T1)));

                String Ssexo = cursor.getString(cursor.getColumnIndex(dbHelper.COL5_T1));

                if(Ssexo.equals("1"))
                    sexo.setSelection(1);
                else
                    sexo.setSelection(0);

                morada.setText(cursor.getString(cursor.getColumnIndex(dbHelper.COL4_T1)));
                contacto.setText(cursor.getString(cursor.getColumnIndex(dbHelper.COL6_T1)));
                break;
            }
        }

        submeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues oCV = new ContentValues();

                oCV.put(dbHelper.COL1_T1,id);
                oCV.put(dbHelper.COL2_T1,nome.getText().toString());
                oCV.put(dbHelper.COL3_T1,idade.getText().toString());
                oCV.put(dbHelper.COL4_T1,morada.getText().toString());
                oCV.put(dbHelper.COL5_T1,String.valueOf(a1));
                oCV.put(dbHelper.COL6_T1,contacto.getText().toString());


                base.update(dbHelper.TABLE_NAME1,oCV,dbHelper.COL1_T1+"=?",new String[]{String.valueOf(id)});
                finish();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
    @Override
    public void onResume() {
        super.onResume();
        base = dbHelper.getWritableDatabase();
    }
}
