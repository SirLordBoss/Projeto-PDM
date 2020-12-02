package pt.ubi.di.pdm.titchersfriend;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

    public void displayAlunos() {
        oLL = (LinearLayout) findViewById(R.id.visualizar);
        Cursor oCursor = oSQLDB.query(oDBH.TABLE_NAME1, new String[]{"*"}, null, null, null, null, null, null);


        boolean bCarryOn = oCursor.moveToFirst();
        while (bCarryOn) {
            LinearLayout oLL1 = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_visualizar, null);
            oLL1.setId(oCursor.getInt(0) * 10 + 5);

            EditText E1 = (EditText) oLL1.findViewById(R.id.nomeAluno);
            E1.setId(oCursor.getInt(0) * 10 + 2);
            E1.setText(oCursor.getString(1));


            Button oB1 = (Button) oLL1.findViewById(R.id.btnVerAluno);
            oB1.setId(oCursor.getInt(0) * 10 + 1);

            oLL.addView(oLL1);

            bCarryOn = oCursor.moveToNext();
        }

    }
}
