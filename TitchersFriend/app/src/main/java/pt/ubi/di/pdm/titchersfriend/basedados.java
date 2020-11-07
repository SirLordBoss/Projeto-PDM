package com.example.sqliteoperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//muitas das coisas que aqui estao foram feitas baseadas na tabela do exemplo em que me basei(links do q usei no fim do codigo)
//Falta fazer várias adaptações para as nossas tabelas e deixei várias adaptações para a tabela do exemplo que depois é so trocar os nomes e assim já ficam ai como exemplos de como tem de ser o codigo depois para cada tabela

public class basedados {
    myDbHelper sqlhelper;
    public basedados(Context context)
    {
        sqlhelper = new myDbHelper(context);
    }

    //aqui assumo que haja so uma tabela, se houver mais que uma tabela, adiciono um parametro strinng e onde diz TABLE_NAME meto o nome da variavel
    public long insertData(String nome_coluna, String insert, String tabela)
    {
        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.nome_coluna, insert);
        long id = db.insert(myDbHelper.tabela, null , contentValues);
        return id;
    }

    public String getData()
    {
        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.NAME,myDbHelper.MyPASSWORD};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            String  password =cursor.getString(cursor.getColumnIndex(myDbHelper.MyPASSWORD));
            buffer.append(cid+ "   " + name + "   " + password +" \n");
        }
        return buffer.toString();
    }
     public String[] getdados(String tabela)
    {
        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.NAME,myDbHelper.MyPASSWORD};
        Cursor cursor =db.query(myDbHelper.tabela,columns,null,null,null,null,null);
        List<String>  tudo =  new ArrayList<String>();
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            
            //aqui percorre as colunas todas da tabela, depois tenho de mudar para as colunas das tabelas com vários ifs
            int cid = cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            tudo.add(String.valueOf(cid));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            tudo.add(name);
            String  password =cursor.getString(cursor.getColumnIndex(myDbHelper.MyPASSWORD));
            tudo.add(password);
            buffer.append(cid+ "   " + name + "   " + password +" \n"); 
        }
         String[] simpleArray = new String[ where.size() ];
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
        contentValues.put(myDbHelper.coluna,insert[n]);
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",insert[n] );
        n++;
        }
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;.    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String NAME = "Name";    //Column II
        private static final String MyPASSWORD= "Password";    // Column III
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"+ MyPASSWORD+" VARCHAR(225));";
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
                Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                Message.message(context,""+e);
            }
        }
    }
}


//https://developer.android.com/reference/android/content/ContentValues
//https://abhiandroid.com/database/sqlite
//https://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application
