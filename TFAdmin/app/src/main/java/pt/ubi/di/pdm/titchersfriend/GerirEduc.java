package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GerirEduc extends AppCompatActivity {

    DBHelper db_helper;
    SQLiteDatabase educ_db;
    LinearLayout visualizer;
    
    Button add_educ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geriralunos);

        add_educ = (Button) findViewByID(R.id.btnAddEduc);
        
        db_helper = new DBHelper(this);
        educ_db = db_helper.getWritableDatabase();
        
        displayEduc();
        
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adicionar_educ = new Intent(GerirEduc.this,AdicionarEduc.class);
                finish();
                startActivity(adicionar_educ);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        db_helper.close();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        educ_db = db_helper.getWritableDatabase();
    }

    public void displayEduc() {
        visualizer = (LinearLayout) findViewById(R.id.visualizar_educ);
        Cursor oCursor = oSQLDB.query(oDBH.TABLE_NAME7, new String[]{"*"}, null, null, null, null, null, null);

        boolean bCarryOn = oCursor.moveToFirst();
        while (bCarryOn) {
            LinearLayout new_educ = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_visualizar_educ, null);
            new_educ.setId(oCursor.getInt(0) * 10 + 2);

            TextView T1 = (TextView) new_educ.findViewById(R.id.nomeEduc);
            T1.setId(oCursor.getInt(0)*10+1);
            T1.setText(oCursor.getString(1));

            ImageButton oB1 = (ImageButton) new_educ.findViewById(R.id.btnVerEduc);
            oB1.setId(oCursor.getInt(0)*10);
            oB1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profile = new Intent(GerirEduc.this, RegisterActivity.class);//espero que isto ajude os testes
                    startActivity(profile);
                }
            });
            visualizer.addView(new_educ);
            bCarryOn = oCursor.moveToNext();
        }

    }
}
