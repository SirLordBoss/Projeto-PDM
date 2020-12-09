package pt.ubi.di.pmd.tfadmin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Activity_GerirEducadores extends AppCompatActivity {

    //DBHelper db_helper;
    //SQLiteDatabase educ_db;
    LinearLayout visualizer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerireduc);

        //db_helper = new DBHelper(this);
        //educ_db = db_helper.getWritableDatabase();

        displayEduc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        displayEduc();
        //db_helper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayEduc();
        //educ_db = db_helper.getWritableDatabase();
    }

    public void displayEduc() {
        visualizer = (LinearLayout) findViewById(R.id.visualizar);

        String x = "false";

        SharedPreferences oSP = getSharedPreferences("important_variables", 0);

        int id = oSP.getInt("id", 0);

        Log.v("DEBUG", "this is the id obtained in GerirEduc: " + id);

        try{
            x = new Sender(Activity_GerirEducadores.this, "200", "id=" + id, null).execute().get();
            Log.v("DEBUG", "obtido sender no GerirEduc: " + x + ". That was it");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject reader;
        Boolean s = false;

        try {
            if (x == null) {
                Log.v("DEBUG", "we got nothing");
                return;
            }
            Log.v("DEBUG", "gonna try to get the reader");
            reader = new JSONObject(x);
            Log.v("DEBUG", "error happened here");
            s = reader.getBoolean("success");
            Log.v("DEBUG", "no, here");
            Log.v("DEBUG", "obtido reader, boolean s: " + s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(s){
            //receber dados
            try {
                reader = new JSONObject(x);
                receber_dados_educ(x);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            JSONObject erro = null;
            String e = "erro";

            try {
                erro = new JSONObject(x);
            } catch (JSONException json_e) {
                json_e.printStackTrace();
            }

            try {
                e = erro.getString("error");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

            Toast.makeText(Activity_GerirEducadores.this, e, Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        Cursor oCursor = educ_db.query(db_helper.TABLE_NAME7, new String[]{"*"}, null, null, null, null, null, null);
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
        */
    }

    public void receber_dados_educ(String dados) throws JSONException{
        JSONObject reader = new JSONObject(dados);
        Boolean success = reader.getBoolean("success");
        Log.v("DEBUG", "obtido reader, boolean s: " + success);

        String s = reader.getString("table");

        String[] arr = s.split(";");

        for(int i = 0; i < arr.length; i++){
            String[] aux = arr[i].split(",");
            LinearLayout new_educ = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_visualizar_educ, null);
            new_educ.setId(Integer.parseInt(aux[0]) * 10 + 3);

            TextView nome_educ = (TextView) new_educ.findViewById(R.id.nomeAluno);
            nome_educ.setId(Integer.parseInt(aux[0]) * 10 + 2);
            nome_educ.setText(aux[1]);

            ImageButton oB1 = (ImageButton) new_educ.findViewById(R.id.btnVerAluno);
            oB1.setId(Integer.parseInt(aux[0]) * 10 + 1);
            oB1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //qual é o equivalente para isto para os educs?
                    //Intent profile = new Intent(GerirEduc.this, RegisterActivity.class);//espero que isto ajude os testes
                    //startActivity(profile);
                }
            });
            ImageButton oB2 = (ImageButton) new_educ.findViewById(R.id.btnApagar);
            oB2.setId(Integer.parseInt(aux[0])* 10);
            oB2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lógica para apagar este educ
                }
            });
            visualizer.addView(new_educ);
        }

    }
}

