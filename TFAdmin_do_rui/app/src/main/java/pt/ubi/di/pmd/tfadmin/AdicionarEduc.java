package pt.ubi.di.pmd.tfadmin;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdicionarEduc extends AppCompatActivity {
    
    LinearLayout pending_educ_visualizer = (LinearLayout) findViewById(R.id.visualizar);
    
    Button registo;
    DBHelper dbHelper;
    SQLiteDatabase educ_db;
    Button create_button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addeduc);

        dbHelper = new DBHelper(AdicionarEduc.this);
        educ_db = dbHelper.getWritableDatabase();
        
        create_button = (Button)findViewById(R.id.btn);
        //waiting room logic goes here
    }
}
