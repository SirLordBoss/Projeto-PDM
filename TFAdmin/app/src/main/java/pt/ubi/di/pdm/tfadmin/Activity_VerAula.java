package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_VerAula extends AppCompatActivity{

    DBHelper dbHelper;
    SQLiteDatabase base;
    TextView data,sumario,notas;
    Button edit;
    String id_at = "",aux;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veraula);
        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        Intent Cheguei = getIntent();
        id_at = Cheguei.getStringExtra("id");

        data = (TextView)findViewById(R.id.dia_aula);
        sumario = (TextView)findViewById(R.id.inputSumario);
        notas = (TextView)findViewById(R.id.inputNotas);


        Cursor cursor = base.query(DBHelper.TATIVIDADE,new String[]{"*"},DBHelper.COL1_TATIVIDADE+"=?",new String[]{id_at},null,null,null);
        cursor.moveToFirst();

        data.setText(cursor.getString(2));
        aux = cursor.getString(1);

        String[] s = aux.split("//");

        sumario.setText(s[0]);
        notas.setText(s[1]);

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
