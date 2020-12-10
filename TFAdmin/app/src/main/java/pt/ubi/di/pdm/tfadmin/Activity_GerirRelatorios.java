package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_GerirRelatorios extends AppCompatActivity {

    Cursor oCursor;
    LinearLayout oLL;
    DBHelper dbHelper;
    SQLiteDatabase base;
    int id,at_id,e_id;
    String i,tk,i2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);

        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();

        SharedPreferences shp = getApplicationContext().getSharedPreferences("important_variables",0);
        id = shp.getInt("id",999);

        Intent Cheguei = getIntent();
        i = Cheguei.getStringExtra("id");
        at_id = Integer.parseInt(i);

        i2 = Cheguei.getStringExtra("id");
        e_id = Integer.parseInt(i2);

        //displayRelatorios();

    }

    protected void onResume() {
        super.onResume();
        Cursor cursor = base.query(DBHelper.TEDUCADOR,new String[]{DBHelper.COL7_TEDUCADOR},DBHelper.COL1_TEDUCADOR+"=?",new String[]{String.valueOf(e_id)},null,null,null);
        cursor.moveToFirst();
        tk = cursor.getString(0);
        cursor.close();
        int aux = dbHelper.updateRelatorio(base,id,e_id,tk,at_id);
        //oLL.removeAllViews();
        //displayRelatorios();
    }

    public void displayRelatorios() {
        oLL = (LinearLayout) findViewById(R.id.visualizar);
        oCursor = base.query(DBHelper.TRELATORIO, new String[]{"*"}, null, null, null, null, null, null);
        boolean bCarryOn = oCursor.moveToFirst();
        while (bCarryOn) {
            LinearLayout oLL1 = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_aulas, null);
            oLL1.setId(oCursor.getInt(0) * 10 + 1);

            TextView E1 = (TextView) oLL1.findViewById(R.id.nomeAluno);
            E1.setId(oCursor.getInt(0) * 10 );
            E1.setText(oCursor.getString(2));




            oLL.addView(oLL1);
            bCarryOn = oCursor.moveToNext();
        }

    }


}
