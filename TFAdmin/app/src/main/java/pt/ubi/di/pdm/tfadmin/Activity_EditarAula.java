package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_EditarAula extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase base;
    EditText sumario,notas;
    TextView data;
    Button submeter;
    String id_at = "",aux,ed_id;
    int admin_id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaraula);

        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        SharedPreferences shp = getApplicationContext().getSharedPreferences("important_variables",0);
        admin_id = shp.getInt("id",999);

        Intent Cheguei = getIntent();
        id_at = Cheguei.getStringExtra("id");
        ed_id = Cheguei.getStringExtra("ed_id");

        data = (TextView) findViewById(R.id.dia_aula);
        sumario = (EditText) findViewById(R.id.inputSumario);
        notas = (EditText) findViewById(R.id.inputNotas);
        submeter = (Button)findViewById(R.id.btnSubmeterRel);

        Cursor cursor = base.query(DBHelper.TATIVIDADE,new String[]{"*"},DBHelper.COL1_TATIVIDADE+"=?",new String[]{id_at},null,null,null);
        cursor.moveToFirst();

        data.setText(cursor.getString(2));
        aux = cursor.getString(1);

        String[] s = aux.split("//");

        sumario.setText(s[0]);
        notas.setText(s[1]);

        submeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sum = sumario.getText().toString()+"//"+notas.getText().toString();
                dbHelper.editAtividade(base,admin_id,Integer.parseInt(ed_id),Integer.parseInt(id_at),sum);
            }
        });


    }
}
