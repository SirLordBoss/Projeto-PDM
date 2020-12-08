package pt.ubi.di.pdm.titchersfriend;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdicionarAluno extends AppCompatActivity {
    Spinner dropdown;
    EditText educando, idade, morada, email;
    Button registo,alergia;
    DBHelper dbHelper;
    SQLiteDatabase base;
    int a1;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaluno);
        dbHelper = new DBHelper(AdicionarAluno.this);
        base= dbHelper.getWritableDatabase();
        alergia = (Button)findViewById(R.id.btnAddAlergia);
        registo = (Button)findViewById(R.id.btnCriar);
        educando = (EditText)findViewById(R.id.inputUser);
        idade = (EditText)findViewById(R.id.inputIdade);
        morada = (EditText)findViewById(R.id.inputMorada);
        email = (EditText)findViewById((R.id.inputEmail));

        dropdown = (Spinner) findViewById(R.id.inputSexo);

        String[] items = new String[]{"Feminino", "Masculino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AdicionarAluno.this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a1 = dropdown.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        registo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String ed = educando.getText().toString();
                String id = idade.getText().toString();
                String mor = morada.getText().toString();
                String em = email.getText().toString();
                /*if(!em.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                   email.setText("");
                    Toast.makeText(AdicionarAluno.this,"Email inválido!",Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }*/
                ContentValues oCV = new ContentValues();
                Log.d("TAG", ed+id+mor+em);
                oCV.put(dbHelper.COL2_T1,ed);
                oCV.put(dbHelper.COL3_T1,id);
                oCV.put(dbHelper.COL4_T1,mor);
                oCV.put(dbHelper.COL5_T1,String.valueOf(a1));
                oCV.put(dbHelper.COL6_T1,em);
                base.insert(dbHelper.TABLE_NAME1,null,oCV);
                Toast.makeText(AdicionarAluno.this,"Inserido Com Sucesso",Toast.LENGTH_SHORT).show();
                educando.setText("");
                idade.setText("");
                morada.setText("");
                email.setText("");
            }
        });

        alergia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdicionarAluno.this,Alergias.class);
                startActivity(i);
            }
        });
        /*dropdown = (Spinner) findViewById(R.id.inputAlergia);

        String[] items = new String[]{"Sem Alergia", "Alergia1"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AdicionarAluno.this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);*/


    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }
    @Override
    public void onResume() {
        super.onResume();
        base=dbHelper.getWritableDatabase();
    }
}
