package pt.ubi.di.pdm.tfadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_WaitingRoomEduc extends AppCompatActivity {

    DBHelper db_helper;
    SQLiteDatabase inscritos_db;
    LinearLayout visualizer;

    int id, aux;

    Button btn_submeter, btn_cancelar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espera);

        db_helper = new DBHelper(this);
        inscritos_db = db_helper.getWritableDatabase();

        SharedPreferences oSP = getApplicationContext().getSharedPreferences("important_variables", 0);
        id = oSP.getInt("id", 999);

    }

    @Override
    protected void onResume() {
        super.onResume();

        aux = db_helper.updateInscritos(inscritos_db, id);
        if(aux == 0 || aux == -1){
            Toast.makeText(Activity_WaitingRoomEduc.this, "Erro", Toast.LENGTH_SHORT).show();
        }

        displayInscritos();
    }

    public void displayInscritos(){
        visualizer = (LinearLayout) findViewById(R.id.visualizar);
        visualizer.removeAllViewsInLayout();

        Cursor oCursor = inscritos_db.query(DBHelper.TINSCRITO, new String[]{"*"}, null, null, null, null, null, null);

        Boolean bCarryOn = oCursor.moveToFirst();
        while(bCarryOn){
            LinearLayout new_inscrito = (LinearLayout) getLayoutInflater().inflate(R.layout.linha_visualizar_educ, null);
            new_inscrito.setId(oCursor.getInt(0) * 10 + 4);

            TextView nome_inscrito = (TextView) new_inscrito.findViewById(R.id.nomeAluno);
            nome_inscrito.setId(oCursor.getInt(0) * 10 + 3);
            nome_inscrito.setText(oCursor.getString(1));

            TextView id_inscrito = (TextView) new_inscrito.findViewById(R.id.idAluno);
            id_inscrito.setId(oCursor.getInt(0) * 10 + 2);
            id_inscrito.setText(oCursor.getString(0));

            ImageButton btn_aprovar = (ImageButton) new_inscrito.findViewById(R.id.btnVerAluno);
            btn_aprovar.setId(oCursor.getInt(0) * 10 + 1);
            btn_aprovar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db_helper.addInscritoToEducador(inscritos_db, id, ((v.getId() - 1)/10));
                    Log.v("DEBUG", "id admin (5): " + id + ", id_inscrito (7): " + ((v.getId() - 1)/10));
                    displayInscritos();
                    //i.putExtra("id", String.valueOf((v.getId() - 1)/10));
                    //startActivity(i);
                }
            });

            ImageButton btn_recusar = (ImageButton) new_inscrito.findViewById(R.id.btnApagar);
            btn_recusar.setId(oCursor.getInt(0) * 10);
            btn_recusar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //logica de apagar educ aqui
                }
            });

            visualizer.addView(new_inscrito);
            bCarryOn = oCursor.moveToNext();
        }
    }

}
