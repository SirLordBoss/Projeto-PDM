package pt.ubi.di.pdm.titchersfriend;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

//muitas das coisas que aqui estao foram feitas baseadas na tabela do exemplo em que me basei(links do q usei no fim do codigo)
//FALTA ADAPTAR PARA AS NOSSAS TABELAS MAS FUNCIONA

public class basedados {
    myDbHelper sqlhelper;

    public basedados(Context context)
    {
        sqlhelper = new myDbHelper(context);
    }

    //aqui assumo que haja so uma tabela, se houver mais que uma tabela, adiciono um parametro strinng e onde diz TABLE_NAME meto o nome da variavel
    public long insertDataSauda(String s)
    {

        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((myDbHelper.Saudacao), s);
        long id = db.insert(myDbHelper.Saudacao, null , contentValues);

        return id;
    }
    public long insertDataCorpo(String s)
    {

        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((myDbHelper.Corpo), s);

        long id = db.insert(myDbHelper.TABLE_NAME, null , contentValues);

        return id;
    }
    public long insertDataAssinatura(String s)
    {

        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((myDbHelper.Assinatura), s);
        long id = db.insert(myDbHelper.Assinatura, null , contentValues);

        return id;
    }
    public long insertDataDespedida(String s)
    {

        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((myDbHelper.Despedida), s);
        long id = db.insert(myDbHelper.Despedida, null , contentValues);

        return id;
    }
    public long insertDataAssi(String s)
    {

        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((myDbHelper.Assinatura), s);
        long id = db.insert(myDbHelper.Assinatura, null , contentValues);

        return id;
    }
    public long insertDataSau(String s)
    {

        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put((myDbHelper.Saudacao), s);
        long id = db.insert(myDbHelper.Saudacao, null , contentValues);

        return id;
    }

    public  ArrayList<String> getDataCorpo()
    {
        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ArrayList<String> sb = new ArrayList<>();
        String[] columns = {myDbHelper.Corpo};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {

            String corp =cursor.getString(cursor.getColumnIndex(myDbHelper.Corpo));

            sb.add(corp);
        }
        return sb;
    }
    public  ArrayList<String> getDataAssinatura()
    {
        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ArrayList<String> sb = new ArrayList<>();
        String[] columns = {myDbHelper.Assinatura};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {

            String corp =cursor.getString(cursor.getColumnIndex(myDbHelper.Assinatura));

            sb.add(corp);
        }
        return sb;
    }
    /*  public String[] getdados(int tabela)
      {
          SQLiteDatabase db = sqlhelper.getWritableDatabase();
          String[] columns = {myDbHelper.UID,myDbHelper.NAME,myDbHelper.MyPASSWORD};
          Cursor cursor =db.query(myDbHelper.tabela,columns,null,null,null,null,null);
          List<String> tudo =  new ArrayList<String>();

          StringBuffer buffer= new StringBuffer();
          while (cursor.moveToNext()) {

              //aqui percorre as colunas todas da tabela, depois tenho de mudar para as colunas das tabelas com vários ifs
              int cid = cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
              tudo.add(String.valueOf(cid));
              String name = cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
              tudo.add(name);
              String password = cursor.getString(cursor.getColumnIndex(myDbHelper.MyPASSWORD));
              tudo.add(password);
              buffer.append(cid + "   " + name + "   " + password + " \n");
          }
          String[] simpleArray = new String[ buffer.size() ];
          tudo.toArray( simpleArray );
          return simpleArray;
      }

      public  int delete(String uname)
      {
          SQLiteDatabase db = sqlhelper.getWritableDatabase();
          String[] whereArgs ={uname};

          int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.NAME+" = ?",whereArgs);
          return  count;
      }

      public int update(String[] insert)
      {
          SQLiteDatabase db = sqlhelper.getWritableDatabase();
          int n=0;
          ContentValues contentValues = new ContentValues();
          while (n<insert.length )
          {
              contentValues.put(myDbHelper.Assinatura,insert[n]);
              int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",insert[n] );
              n++;
          }
          return 1;
      }
  */
    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String Assinatura="Assinatura";     // Column I (Primary Key)
        private static final String Saudacao = "Saudacao";    //Column II
        private static final String Corpo= "Corpo";    // Column III
        private static final String Despedida= "Despedida";    // Column III
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+Assinatura+" VARCHAR(255) PRIMARY KEY, "+Saudacao+" VARCHAR(255) ,"+Corpo+" VARCHAR(255) ,"+ Despedida+" VARCHAR(225));";

        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
}


//https://developer.android.com/reference/android/content/ContentValues
//https://abhiandroid.com/database/sqlite
//https://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application