package pt.ubi.di.pdm.tfadmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.concurrent.ExecutionException;

public class DBHelper extends SQLiteOpenHelper {

    private static Context c;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "AdminDB";
    protected static final String TADMIN = "admin";
    protected static final String COL1_TADMIN = "u_id";
    protected static final String COL2_TADMIN = "u_nome";
    protected static final String COL3_TADMIN = "u_idade";
    protected static final String COL4_TADMIN = "u_morada";
    protected static final String COL5_TADMIN = "u_sexo";
    protected static final String COL6_TADMIN = "u_email";
    protected static final String CREATE_TADMIN = "CREATE TABLE IF NOT EXISTS "+TADMIN+ "("+COL1_TADMIN+" INTEGER NOT NULL,"+COL2_TADMIN+" VARCHAR(200) NOT NULL,"+COL3_TADMIN+" INTEGER NOT NULL,"+COL4_TADMIN+" VARCHAR(200) NOT NULL,"+COL5_TADMIN+" INTEGER NOT NULL,"+COL6_TADMIN+" VARCHAR(200) NOT NULL);";

    protected static final String TEDUCADOR = "educador";
    protected static final String COL1_TEDUCADOR = "u_id";
    protected static final String COL2_TEDUCADOR = "u_nome";
    protected static final String COL3_TEDUCADOR = "u_idade";
    protected static final String COL4_TEDUCADOR = "u_morada";
    protected static final String COL5_TEDUCADOR = "u_sexo";
    protected static final String COL6_TEDUCADOR = "u_email";
    protected static final String COL7_TEDUCADOR = "t_token";
    protected static final String COL8_TEDUCADOR = "t_utilizada";
    protected static final String CREATE_TEDUCADOR = "CREATE TABLE IF NOT EXISTS "+TEDUCADOR+ "("+COL1_TEDUCADOR+" INTEGER NOT NULL,"+COL2_TEDUCADOR+" VARCHAR(200) NOT NULL,"+COL3_TEDUCADOR+" INTEGER NOT NULL,"+COL4_TEDUCADOR+" VARCHAR(200) NOT NULL,"+COL5_TEDUCADOR+" INTEGER NOT NULL,"+COL6_TEDUCADOR+" VARCHAR(200) NOT NULL,"+COL7_TEDUCADOR+" VARCHAR(300) NOT NULL,"+COL8_TEDUCADOR+" INTEGER NOT NULL);";

    protected static final String TEDUCANDO = "educando";
    protected static final String COL1_TEDUCANDO = "e_id";
    protected static final String COL2_TEDUCANDO = "e_nome";
    protected static final String COL3_TEDUCANDO = "e_idade";
    protected static final String COL4_TEDUCANDO = "e_morada";
    protected static final String COL5_TEDUCANDO = "e_sexo";
    protected static final String COL6_TEDUCANDO = "e_contacto";
    protected static final String CREATE_TEDUCANDO = "CREATE TABLE IF NOT EXISTS "+TEDUCANDO+ "("+COL1_TEDUCANDO+" INTEGER NOT NULL,"+COL2_TEDUCANDO+" VARCHAR(200) NOT NULL,"+COL3_TEDUCANDO+" INTEGER NOT NULL,"+COL4_TEDUCANDO+" VARCHAR(200) NOT NULL,"+COL5_TEDUCANDO+" INTEGER NOT NULL,"+COL6_TEDUCANDO+" VARCHAR(200) NOT NULL);";

    protected static final String TALERGIA = "alergia";
    protected static final String COL1_TALERGIA = "e_id";
    protected static final String COL2_TALERGIA = "al_id";
    protected static final String COL3_TALERGIA = "al_nome";
    protected static final String CREATE_TALERGIA = "CREATE TABLE IF NOT EXISTS "+TALERGIA+ "("+COL1_TALERGIA+" INTEGER NOT NULL,"+COL2_TALERGIA+" INTEGER NOT NULL,"+COL3_TALERGIA+" VARCHAR(200) NOT NULL);";

    protected static final String TATIVIDADE = "atividade";
    protected static final String COL1_TATIVIDADE = "a_id";
    protected static final String COL2_TATIVIDADE = "a_sumario";
    protected static final String COL3_TATIVIDADE = "a_data";
    protected static final String CREATE_TATIVIDADE = "CREATE TABLE IF NOT EXISTS "+TATIVIDADE+ "("+COL1_TATIVIDADE+" INTEGER NOT NULL,"+COL2_TATIVIDADE+" VARCHAR(300) NOT NULL,"+COL3_TATIVIDADE+" VARCHAR(20) NOT NULL);";

    protected static final String TFALTA = "falta";
    protected static final String COL1_TFALTA = "e_nome";
    protected static final String COL2_TFALTA = "is_falta";
    protected static final String CREATE_TFALTA = "CREATE TABLE IF NOT EXISTS "+TFALTA+ "("+COL1_TFALTA+" VARCHAR(200) NOT NULL,"+COL2_TFALTA+" INTEGER NOT NULL);";

    protected static final String TRELATORIO = "relatorio";
    protected static final String COL1_TRELATORIO = "e_nome";
    protected static final String COL2_TRELATORIO = "r_comer";
    protected static final String COL3_TRELATORIO = "r_dormir";
    protected static final String COL4_TRELATORIO = "r_coment";
    protected static final String COL5_TRELATORIO = "r_necessidades";
    protected static final String COL6_TRELATORIO = "r_curativos";
    protected static final String CREATE_TRELATORIO = "CREATE TABLE IF NOT EXISTS "+TATIVIDADE+ "("+COL1_TRELATORIO+" VARCHAR(200) NOT NULL,"+COL2_TRELATORIO+" INTEGER NOT NULL,"+COL3_TRELATORIO+" INTEGER NOT NULL,"+COL4_TRELATORIO+" VARCHAR(300) NOT NULL,"+COL5_TRELATORIO+" VARCHAR(20) NOT NULL"+COL6_TRELATORIO+" INTEGER NOT NULL);";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TADMIN);
        db.execSQL(CREATE_TEDUCADOR);
        db.execSQL(CREATE_TEDUCANDO);
        db.execSQL(CREATE_TALERGIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+TADMIN);
        db.execSQL("DROP TABLE "+TEDUCADOR);
    }

    //#200
    /*
    * função updateEducador para meter no método onResume
    *
    * @param id  id do administrador que pede os dados
    */
    public static int updateEducador(SQLiteDatabase db, int id){//-1 : Sem comunicação   0 : Erro da base de dados   1 : Tudo ok
        String s = null;
        try {
            s = new Sender(c,"200", "id="+id,null).execute().get();
            if(s == null){
                return -1;
            }
            JSONObject o = new JSONObject(s);
            if(!o.getBoolean("success")){
                Toast.makeText(c,o.getString("error"),Toast.LENGTH_SHORT).show();
                return 1;
            }
            db.execSQL("DROP TABLE "+TEDUCADOR);
            db.execSQL(CREATE_TEDUCADOR);
            String table = o.getString("table");
            String[] lines = table.split(";");
            for (String line : lines) {
                String[] col = line.split(",");
                ContentValues cv = new ContentValues();
                cv.put(COL1_TEDUCADOR, col[0]);
                cv.put(COL2_TEDUCADOR, col[1]);
                cv.put(COL3_TEDUCADOR, col[2]);
                cv.put(COL4_TEDUCADOR, col[3]);
                cv.put(COL5_TEDUCADOR, col[4]);
                cv.put(COL6_TEDUCADOR, col[5]);
                cv.put(COL7_TEDUCADOR, col[6]);
                cv.put(COL8_TEDUCADOR, col[7]);
                if (db.insert(TEDUCADOR, null, cv) == -1)
                    return 0;
                cv.clear();
            }
            return 1;
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //#201
    /*
     * função updateAdmin para meter no método onResume
     *
     * @param id  id do administrador que pede os dados
     */
    public static int updateAdmin(SQLiteDatabase db, int id){ //-1 : Sem comunicação   0 : Erro da base de dados   1 : Tudo ok
        String s = null;
        try {
            s = new Sender(c,"201", "id="+id,null).execute().get();
            if(s == null){
                return -1;
            }
            JSONObject o = new JSONObject(s);
            if(!o.getBoolean("success")){
                Toast.makeText(c,o.getString("error"),Toast.LENGTH_SHORT).show();
                return 1;
            }

            db.execSQL("DROP TABLE "+TADMIN);
            db.execSQL(CREATE_TADMIN);
            String table = o.getString("table");
            String[] lines = table.split(";");
            for (String line : lines) {
                String[] col = line.split(",");
                ContentValues cv = new ContentValues();
                cv.put(COL1_TADMIN, col[0]);
                cv.put(COL2_TADMIN, col[1]);
                cv.put(COL3_TADMIN, col[2]);
                cv.put(COL4_TADMIN, col[3]);
                cv.put(COL5_TADMIN, col[4]);
                cv.put(COL6_TADMIN, col[5]);
                if (db.insert(TADMIN, null, cv) == -1)
                    return 0;
                cv.clear();
            }
            return 1;
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }
    //#202
    /*
     * função updateEducando para meter no método onResume
     *
     * @param id  id do administrador que pede os dados
     * @param e_id id do educador a que a turma pertence
     * @param t_token token da turma a que o educando pertence
     *
     */
    public static int updateEducando(SQLiteDatabase db, int id,int e_id,int t_token){ //-1 : Sem comunicação   0 : Erro da base de dados   1 : Tudo ok
        String s = null;
        try {
            s = new Sender(c,"202", "id="+id+"&e_id="+e_id+"&t="+t_token,null).execute().get();
            if(s == null){
                return -1;
            }
            JSONObject o = new JSONObject(s);
            if(!o.getBoolean("success")){
                Toast.makeText(c,o.getString("error"),Toast.LENGTH_SHORT).show();
                return 1;
            }

            db.execSQL("DROP TABLE "+TEDUCANDO);
            db.execSQL(CREATE_TEDUCANDO);
            String table = o.getString("table");
            String[] lines = table.split(";");
            for (String line : lines) {
                String[] col = line.split(",");
                ContentValues cv = new ContentValues();
                cv.put(COL1_TEDUCANDO, col[0]);
                cv.put(COL2_TEDUCANDO, col[1]);
                cv.put(COL3_TEDUCANDO, col[2]);
                cv.put(COL4_TEDUCANDO, col[3]);
                cv.put(COL5_TEDUCANDO, col[4]);
                cv.put(COL6_TEDUCANDO, col[5]);
                if (db.insert(TEDUCANDO, null, cv) == -1)
                    return 0;
                cv.clear();
            }
            return 1;
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

}