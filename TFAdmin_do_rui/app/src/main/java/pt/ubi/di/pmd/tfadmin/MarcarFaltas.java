package pt.ubi.di.pmd.tfadmin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MarcarFaltas extends AppCompatActivity {
    DBHelper oDBH;
    SQLiteDatabase oSQLDB;
    LinearLayout oLL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcarfaltas);

        oDBH = new DBHelper(this);
        oSQLDB = oDBH.getWritableDatabase();

        displayAlunos();
    }


    protected void onPause() {
        super.onPause();
        oDBH.close();
    }


    public void displayAlunos() {
        oLL = (LinearLayout) findViewById(R.id.verfaltas);
        Cursor oCursor = oSQLDB.query(oDBH.TABLE_NAME1, new String[]{"*"}, null, null, null, null, null, null);

        boolean bCarryOn = oCursor.moveToFirst();
        while (bCarryOn) {
            LinearLayout oLL1 = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_faltas, null);
            oLL1.setId(oCursor.getInt(0) * 10 + 2);

            TextView T1 = (TextView) oLL1.findViewById(R.id.nomeAluno);
            T1.setId(oCursor.getInt(0) * 10 + 1);
            T1.setText(oCursor.getString(1));


            ImageButton oB1 = (ImageButton) oLL1.findViewById(R.id.btnVerAluno);
            oB1.setId(oCursor.getInt(0) * 10);

            oLL.addView(oLL1);
            bCarryOn = oCursor.moveToNext();
        }
    }
}
