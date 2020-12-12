package pt.ubi.di.pdm.tfadmin;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Activity_AdicionarAdmin extends AppCompatActivity {
    DBHelper db_helper;
    SQLiteDatabase admin_db;
    LinearLayout visualizer;

    int id, aux, sexo_dropdown_selection;

    EditText nome_admin, idade_admin, morada_admin, email_admin;

    Spinner dropdown;

    ArrayList<String> sexList = new ArrayList<>();

    Button registo, btn_submeter, btn_cancelar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        db_helper = new DBHelper(this);
        admin_db = db_helper.getWritableDatabase();

        SharedPreferences oSP = getApplicationContext().getSharedPreferences("important_variables", 0);
        id = oSP.getInt("id", 999);

        registo = (Button) findViewById(R.id.btnCriar);

        nome_admin = (EditText) findViewById(R.id.inputUser);
        idade_admin = (EditText) findViewById(R.id.inputIdade);
        morada_admin = (EditText) findViewById(R.id.inputMorada);
        email_admin = (EditText) findViewById((R.id.inputEmail));

        dropdown = (Spinner) findViewById(R.id.inputSexo);

        sexList.add("");
        String[] items = new String[]{"Feminino", "Masculino"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_AdicionarAdmin.this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sexo_dropdown_selection = dropdown.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nome_admin.getText().toString();
                int idade = Integer.parseInt(idade_admin.getText().toString());
                String mor = morada_admin.getText().toString();
                String em = email_admin.getText().toString();
                if(!em.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                    email_admin.setText("");
                    Toast.makeText(Activity_AdicionarAdmin.this,"Email inválido!",Toast.LENGTH_SHORT).show();
                    email_admin.requestFocus();
                    return;
                }

                int sexo = Integer.valueOf(sexo_dropdown_selection);

                String pwd = "pwd";

                if(db_helper.addAdmin(admin_db, id, nome, idade, mor, sexo, em, "pwd") == 1){
                    Toast.makeText(Activity_AdicionarAdmin.this, "Feito!", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Activity_AdicionarAdmin.this, "erro!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        aux = db_helper.updateAdmin(admin_db, id);
        if(aux == 0 || aux == -1){
            Toast.makeText(Activity_AdicionarAdmin.this, "Erro a atualizar a base de dades Educador", Toast.LENGTH_SHORT).show();
        }
    }
}
