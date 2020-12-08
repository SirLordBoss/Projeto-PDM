package pt.ubi.di.pdm.titchersfriend;

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

public class GerirAlunos extends AppCompatActivity {

    DBHelper oDBH;
    SQLiteDatabase oSQLDB;
    Cursor oCursor;
    LinearLayout oLL;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geriralunos);
        add = (Button)findViewById(R.id.btnAddAluno);
        oDBH = new DBHelper(this);
        oSQLDB = oDBH.getWritableDatabase();

        displayAlunos();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A2 = new Intent(GerirAlunos.this,AdicionarAluno.class);
                finish();
                startActivity(A2) ;
            }
        });
    }
        @Override
        protected void onPause() {
            super.onPause();
            oDBH.close();
        }
        @Override
        public void onResume() {
            super.onResume();
            oSQLDB= oDBH.getWritableDatabase();
            oLL.removeAllViews();
            displayAlunos();
        }

    public void displayAlunos() {
        oLL = (LinearLayout) findViewById(R.id.visualizar);
        oCursor = oSQLDB.query(oDBH.TABLE_NAME1, new String[]{"*"}, null, null, null, null, null, null);

        boolean bCarryOn = oCursor.moveToFirst();
        while (bCarryOn) {
            LinearLayout oLL1 = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_visualizar, null);
            oLL1.setId(oCursor.getInt(0) * 10 + 3);

            TextView E1 = (TextView) oLL1.findViewById(R.id.nomeAluno);
            E1.setId(oCursor.getInt(0) * 10 + 2);
            E1.setText(oCursor.getString(1));

            TextView T2 = (TextView) oLL1.findViewById(R.id.idAluno);
            T2.setId(oCursor.getInt(0) * 10 + 1);
            T2.setText(oCursor.getString(0));

            ImageButton oB1 = (ImageButton) oLL1.findViewById(R.id.btnVerAluno);
            oB1.setId(oCursor.getInt(0) * 10);
            oB1.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                        int id = (v.getId())/10;
                        Intent profile = new Intent(GerirAlunos.this, PerfAlunoActivity.class);
                        profile.putExtra("id",String.valueOf(id));
                        startActivity(profile);
                    }
                });
                oLL.addView(oLL1);
                bCarryOn = oCursor.moveToNext();
            }

    }
}
