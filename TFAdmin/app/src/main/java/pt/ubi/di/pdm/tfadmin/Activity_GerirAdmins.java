package pt.ubi.di.pmd.tfadmin;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class Activity_GerirAdmins extends AppCompatActivity {

    DBHelper db_helper;
    SQLiteDatabase admin_db;

    LinearLayout visualizer;

    Button add_admin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geriradmins);

        add_admin = (Button) findViewById(R.id.btnAddAdmin);

        db_helper = new DBHelper(this);
        admin_db = db_helper.getWritableDatabase();

        displayAdmin();

        add_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adicionar_admin = new Intent(Activity_GerirAdmins.this,Activity_AdicionarAdmin.class);
                finish();
                startActivity(adicionar_admin);
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
        admin_db = db_helper.getWritableDatabase();
    }

    public void displayAdmin() {
        String x = "false";

        try{
            x = new Sender(Activity_GerirAdmins.this, "201", "id=1", null).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            if (x == null) {

            }
        } finally {

        }

        /*catch (ExecutionException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }*/
    }
}
