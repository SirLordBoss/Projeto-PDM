package pt.ubi.di.pdm.titchersfriend;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAcess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DataBaseAcess instance;
    Cursor c = null;

    private DataBaseAcess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DataBaseAcess getInstance(Context context){
        if(instance == null){
            instance = new DataBaseAcess(context);
        }
        return instance;
    }

    public void open(){
        this.db =   openHelper.getWritableDatabase();
    }

    public void close(){
        this.db.close();
    }
}

//https://www.youtube.com/watch?v=rziyVBKEU50  --> link do video onde me baseei