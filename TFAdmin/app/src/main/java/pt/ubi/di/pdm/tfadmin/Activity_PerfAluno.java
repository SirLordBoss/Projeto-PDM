package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_PerfAluno extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase base;
    TextView nome,idade,sexo,morada,contacto;
    Button rel,edit;
    String id_aluno;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfaluno);
        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        Intent Cheguei = getIntent();
        id_aluno = Cheguei.getStringExtra("id");

        nome = (TextView)findViewById(R.id.txtNome);
        idade = (TextView)findViewById(R.id.txtIdade);
        sexo = (TextView)findViewById(R.id.txtSexo);
        morada = (TextView)findViewById(R.id.txtMorada);
        contacto = (TextView)findViewById(R.id.txtContacto);

        rel = (Button) findViewById(R.id.btnRelatorio);
        edit = (Button) findViewById(R.id.btnCancelarRel);

        Cursor cursor =base.query(DBHelper.TEDUCANDO,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){
            String c1 =cursor.getString(cursor.getColumnIndex(DBHelper.COL1_TEDUCANDO));
            if (c1.equals(id_aluno)){
                nome.setText("Nome: "+cursor.getString(cursor.getColumnIndex(DBHelper.COL2_TEDUCANDO)));
                idade.setText("Idade: "+cursor.getString(cursor.getColumnIndex(DBHelper.COL3_TEDUCANDO)));

                if(cursor.getString(cursor.getColumnIndex(DBHelper.COL5_TEDUCANDO)).equals("1"))
                    sexo.setText("Sexo: Masculino");
                else
                    sexo.setText("Sexo: Feminino");

                morada.setText("morada: "+cursor.getString(cursor.getColumnIndex(DBHelper.COL4_TEDUCANDO)));
                contacto.setText("contacto: "+cursor.getString(cursor.getColumnIndex(DBHelper.COL6_TEDUCANDO)));
                break;
            }
        }

    }
}
