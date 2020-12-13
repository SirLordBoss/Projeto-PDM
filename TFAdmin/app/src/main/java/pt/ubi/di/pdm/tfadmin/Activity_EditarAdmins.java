package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_EditarAdmins extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase admin_db;

    EditText nome, idade, morada, contacto;

    Spinner sexo;

    Button submeter,cancelar;

    String id;

    int admin_id,id_admin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarusers);

        dbHelper = new DBHelper(this);
        admin_db = dbHelper.getWritableDatabase();

        Intent Cheguei = getIntent();
        id = Cheguei.getStringExtra("id");
        id_admin = Integer.parseInt(Cheguei.getStringExtra("id"));

        SharedPreferences shp = getApplicationContext().getSharedPreferences("important_variables",0);
        admin_id = shp.getInt("id",999);

        nome = findViewById(R.id.editNome);
        idade = findViewById(R.id.editIdade);
        sexo = findViewById(R.id.editSexo);
        morada = findViewById(R.id.editMorada);
        contacto = findViewById(R.id.editContacto);

        submeter = findViewById(R.id.btnRelatorio);
        cancelar = findViewById(R.id.btnCancelarRel);

        String[] items = new String[]{"Sexo: Feminino", "Sexo: Masculino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_EditarAdmins.this, android.R.layout.simple_spinner_dropdown_item, items);
        sexo.setAdapter(adapter);

        Cursor cursor = admin_db.query(DBHelper.TEDUCADOR,new String[]{"*"},DBHelper.COL1_TEDUCADOR+"=?",new String[]{id},null,null,null);
        cursor.moveToNext();

        nome.setText(cursor.getString(1));
        idade.setText(cursor.getString(2));

        if (cursor.getString(4).equals("1"))
            sexo.setSelection(1);
        else
            sexo.setSelection(0);

        morada.setText(cursor.getString(3));
        contacto.setText(cursor.getString(5));
        cursor.close();
        submeter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String n = nome.getText().toString();
                int i = Integer.parseInt(idade.getText().toString());
                String m = morada.getText().toString();
                int s = sexo.getSelectedItemPosition();
                String c = contacto.getText().toString();

                int aux;
                aux = dbHelper.editUser(admin_id,id_admin,n,i,m,s,c);

                if (aux == 1)
                    Toast.makeText(Activity_EditarAdmins.this,"Sucesso",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Activity_EditarAdmins.this,"Erro",Toast.LENGTH_SHORT).show();

                dbHelper.close();
                finish();
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
        admin_db.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        dbHelper = new DBHelper(this);
        admin_db = dbHelper.getWritableDatabase();
    }
}
