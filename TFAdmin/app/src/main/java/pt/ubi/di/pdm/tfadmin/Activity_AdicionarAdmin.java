package pt.ubi.di.pdm.tfadmin;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Activity_AdicionarAdmin extends AppCompatActivity {
    DBHelper db_helper;
    SQLiteDatabase admin_db;
    LinearLayout visualizer;

    int id, aux, sexo_dropdown_selection;

    EditText nome_admin, idade_admin, morada_admin, email_admin, password_admin;

    Spinner dropdown;

    ArrayList<String> sexList = new ArrayList<>();

    Button registo, btn_submeter, btn_cancelar;

    public static String getM5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }   // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        db_helper = new DBHelper(this);
        admin_db = db_helper.getWritableDatabase();

        SharedPreferences oSP = getApplicationContext().getSharedPreferences("important_variables", 0);
        id = oSP.getInt("id", 999);

        registo = findViewById(R.id.btnCriar);

        nome_admin = findViewById(R.id.inputUser);
        idade_admin = findViewById(R.id.inputIdade);
        morada_admin = findViewById(R.id.inputMorada);
        email_admin = findViewById((R.id.inputEmail));

        password_admin = findViewById(R.id.inputPassword);

        dropdown = findViewById(R.id.inputSexo);

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

                String pwd = getM5(password_admin.getText().toString());

                Log.v("DEBUG", "parametros: id=" + id + ", nome=" + nome + ", idade=" + idade + ", morada=" + mor + ", sexo=" + sexo + ", email=" + em);

                if(db_helper.addAdmin(id, nome, idade, mor, sexo, em, pwd) == 1){
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
