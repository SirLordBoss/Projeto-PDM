package pt.ubi.di.pmd.tfadmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MyDB2";
    protected static final String TABLE_NAME1 = "educando";
    protected static final String TABLE_NAME2 = "atividade";
    protected static final String TABLE_NAME3 = "alergias";
    protected static final String TABLE_NAME4 = "faltas";
    protected static final String TABLE_NAME5 = "relatorio";
    protected static final String TABLE_NAME6 = "contem";
    //aqui foi utilizada uma estratégia sugerida com o Tiago Almeida, de colocar a informação necessária para identificar admins em tabelas locais. 
    //Como os admins só aparecem identificados com uma foreign key para a tabela dos utilizadores, foram necessárias duas tabelas, uma contendo a informação dos utilizadores, 
    //e outra com o id dos administradores, sendo a verificação feita depois
    protected static final String TABLE_NAME7 = "user";
    protected static final String TABLE_NAME8 = "admin";

    protected static final String COL1_T1 = "e_id";
    protected static final String COL2_T1 = "e_nome";
    protected static final String COL3_T1 = "e_idade";
    protected static final String COL4_T1 = "e_morada";
    protected static final String COL5_T1 = "e_sexo";
    protected static final String COL6_T1 = "e_contacto";

    protected static final String COL1_T2 = "a_sumario";
    protected static final String COL2_T2 = "a_id";
    protected static final String COL3_T2 = "a_data";

    protected static final String COL1_T3 = "il_id";
    protected static final String COL2_T3 = "il_nome";

    protected static final String COL1_T4 = "e_id";
    protected static final String COL2_T4 = "a_id";

    protected static final String COL1_T5 = "r_comer";
    protected static final String COL2_T5 = "r_dormir";
    protected static final String COL3_T5 = "r_coment";
    protected static final String COL4_T5 = "r_necessidades";
    protected static final String COL5_T5 = "r_curativos";
    protected static final String COL6_T5 = "e_id";
    protected static final String COL7_T5 = "a_id";

    protected static final String COL1_T6= "al_id";
    protected static final String COL2_T6= "e_id";
    
    protected static final String COL1_T7 = "u_id";
    protected static final String COL2_T7 = "u_nome";
    
    protected static final String COL1_T8 = "adm_id";

    private static final String CREATE_EDUCANDO = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME1+"("+COL1_T1+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+COL2_T1+" VARCHAR(100) NOT NULL,"+COL3_T1+" INTEGER NOT NULL,"+COL4_T1+" VARCHAR(200) NOT NULL,"+COL5_T1+" INTEGER NOT NULL,"+COL6_T1+" VARCHAR(10) NOT NULL)";
    private static final String CREATE_ATIVIDADE = "CREATE TABLE IF NOT EXISTS  "+TABLE_NAME2+"("+COL1_T2+" VARCHAR(300) NOT NULL,"+COL2_T2+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+COL3_T2+" DATE NOT NULL)";
    private static final String CREATE_ALERGIAS = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME3+"("+COL1_T3+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+COL2_T3+" VARCHAR(100) NOT NULL)";
    private static final String CREATE_FALTAS = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME4+"("+COL1_T4+" INTEGER NOT NULL,"+COL2_T4+" INTEGER NOT NULL, PRIMARY KEY ("+COL1_T4+","+COL2_T4+"),FOREIGN KEY ("+COL1_T4+") REFERENCES educando("+COL1_T4+"),FOREIGN KEY ("+COL2_T4+") REFERENCES atividade("+COL2_T4+"))";
    private static final String CREATE_RELATORIO = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME5+"("+COL1_T5+" INTEGER NOT NULL,"+COL2_T5+" INTEGER NOT NULL,"+COL3_T5+" VARCHAR(500) NOT NULL,"+COL4_T5+" INTEGER NOT NULL,"+COL5_T5+" INTEGER NOT NULL,"+COL6_T5+" INTEGER NOT NULL,"+COL7_T5+" INTEGER NOT NULL,PRIMARY KEY ("+COL6_T5+","+COL7_T5+"),FOREIGN KEY ("+COL6_T5+") REFERENCES educando("+COL6_T5+"),FOREIGN KEY ("+COL7_T5+") REFERENCES atividade("+COL7_T5+"))";
    private static final String CREATE_CONTEM = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME6+"("+COL1_T6+" INTEGER NOT NULL,"+COL2_T6+" INTEGER NOT NULL, PRIMARY KEY ("+COL1_T6+","+COL2_T6+"),FOREIGN KEY ("+COL1_T6+") REFERENCES alergias("+COL1_T6+"),FOREIGN KEY ("+COL2_T6+") REFERENCES educando("+COL2_T6+"))";
    private static final String CREATE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME7 + "(" + COL1_T7 + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + COL2_T7 + "VARCHAR(100) NOT NULL)";
    private static final String CREATE_ADMIN = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME8 + "(" + COL1_T8 + " INTEGER NOT NULL PRIMARY KEY, FOREIGN KEY(" + COL1_T8 + ") REFERENCES user("+ COL1_T8 + "))";
    
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EDUCANDO);
        db.execSQL(CREATE_ATIVIDADE);
        db.execSQL(CREATE_ALERGIAS);
        db.execSQL(CREATE_FALTAS);
        db.execSQL(CREATE_RELATORIO);
        db.execSQL(CREATE_CONTEM);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_ADMIN);

        ContentValues cv = new ContentValues();
        cv.put(COL2_T1,"Diogo");
        cv.put(COL3_T1,1);
        cv.put(COL4_T1,"Diogo");
        cv.put(COL5_T1,3);
        cv.put(COL6_T1,"dIOGO");
        db.insert(TABLE_NAME1,null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+TABLE_NAME1+";");
        db.execSQL("DROP TABLE "+TABLE_NAME2+";");
        db.execSQL("DROP TABLE "+TABLE_NAME3+";");
        db.execSQL("DROP TABLE "+TABLE_NAME4+";");
        db.execSQL("DROP TABLE "+TABLE_NAME5+";");
        db.execSQL("DROP TABLE "+TABLE_NAME6+";");

        db.execSQL(CREATE_EDUCANDO);
        db.execSQL(CREATE_ATIVIDADE);
        db.execSQL(CREATE_ALERGIAS);
        db.execSQL(CREATE_FALTAS);
        db.execSQL(CREATE_RELATORIO);
        db.execSQL(CREATE_CONTEM);

    }
}
