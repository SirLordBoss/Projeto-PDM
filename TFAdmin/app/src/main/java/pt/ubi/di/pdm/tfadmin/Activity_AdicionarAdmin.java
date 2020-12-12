package pt.ubi.di.pdm.tfadmin;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_AdicionarAdmin extends AppCompatActivity {

    DBHelper db_helper;
    SQLiteDatabase educ_db;
    LinearLayout visualizer;

    int id, aux;

    Button btn_submeter, btn_cancelar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espera);

        db_helper = new DBHelper(this);
        educ_db = db_helper.getWritableDatabase();

        SharedPreferences oSP = getApplicationContext().getSharedPreferences("important_variables", 0);
        id = oSP.getInt("id", 999);

    }
}
