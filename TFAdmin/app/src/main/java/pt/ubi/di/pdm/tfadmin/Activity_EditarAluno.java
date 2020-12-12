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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_EditarAluno extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase base;
    EditText nome,idade,morada,contacto;
    Spinner sexo;
    Button submeter,cancelar;
    String id_aluno;
    int admin_id,id_al,id_educ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaraluno);

        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        Intent Cheguei = getIntent();
        id_aluno = Cheguei.getStringExtra("id");
        id_educ = Integer.parseInt(Cheguei.getStringExtra("id_educadora"));
        id_al = Integer.parseInt(id_aluno);

        SharedPreferences shp = getApplicationContext().getSharedPreferences("important_variables",0);
        admin_id = shp.getInt("id",999);

        nome = (EditText) findViewById(R.id.editNome);
        idade = (EditText) findViewById(R.id.editIdade);
        sexo = (Spinner) findViewById(R.id.editSexo);
        morada = (EditText) findViewById(R.id.editMorada);
        contacto = (EditText) findViewById(R.id.editContacto);

        submeter = (Button) findViewById(R.id.btnRelatorio);
        cancelar = (Button) findViewById(R.id.btnCancelarRel);

        String[] items = new String[]{"Sexo: Feminino", "Sexo: Masculino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_EditarAluno.this, android.R.layout.simple_spinner_dropdown_item, items);
        sexo.setAdapter(adapter);

        Cursor cursor = base.query(DBHelper.TEDUCANDO,new String[]{"*"},DBHelper.COL1_TEDUCANDO+"=?",new String[]{id_aluno},null,null,null);
        cursor.moveToNext();

        nome.setText(cursor.getString(1));
        idade.setText(cursor.getString(2));

        if (cursor.getString(4).equals("1"))
            sexo.setSelection(1);
        else
            sexo.setSelection(0);

        morada.setText(cursor.getString(3));
        contacto.setText(cursor.getString(5));

        submeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = nome.getText().toString();
                int i = Integer.parseInt(idade.getText().toString());
                String m = morada.getText().toString();
                int s = sexo.getSelectedItemPosition();
                String c = contacto.getText().toString();


                Cursor cursor = base.query(DBHelper.TALERGIA,new String[]{"*"},DBHelper.COL1_TALERGIA+"=?",new String[]{id_aluno},null,null,null);
                int aux = 0;
                while (cursor.moveToNext()){
                    aux++;
                }
                int[] aler = new int[aux];
                cursor.moveToFirst();
                int aux2=0;
                while (cursor.moveToNext()){
                    aler[aux2] = Integer.parseInt(cursor.getString(1));
                    aux2++;
                }

                Log.d("tag",String.valueOf(admin_id));
                Log.d("tag",String.valueOf(id_educ));
                Log.d("tag",String.valueOf(id_al));

                dbHelper.editEducando(base,admin_id,id_educ,id_al,n,i,m,s,c,aler);
            }
        });

    }


}
