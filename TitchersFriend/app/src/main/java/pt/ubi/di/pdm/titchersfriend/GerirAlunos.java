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
    LinearLayout oLL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geriralunos);

        oDBH = new DBHelper(this);
        oSQLDB = oDBH.getWritableDatabase();

        displayAlunos();
    }

    protected void onPause() {
        super.onPause();
        oDBH.close();
    }

    public void displayAlunos() {
        oLL = (LinearLayout) findViewById(R.id.visualizar);
        Cursor oCursor = oSQLDB.query(oDBH.TABLE_NAME1, new String[]{"*"}, null, null, null, null, null, null);

        boolean bCarryOn = oCursor.moveToFirst();
        while (bCarryOn) {
            LinearLayout oLL1 = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_visualizar, null);
            oLL1.setId(oCursor.getInt(0) * 10 + 2);

            EditText E1 = (EditText) oLL1.findViewById(R.id.nomeAluno);
            E1.setId(oCursor.getInt(0)*10+1);
            E1.setText(oCursor.getString(1));


            ImageButton oB1 = (ImageButton) oLL1.findViewById(R.id.btnVerAluno);
            oB1.setId(oCursor.getInt(0)*10);
            oB1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profile = new Intent(GerirAlunos.this,RegisterActivity.class);//ignorem o intent, foi so para teste!
                    startActivity(profile);
                }
            });
            oLL.addView(oLL1);
            bCarryOn = oCursor.moveToNext();
        }

    }
}
