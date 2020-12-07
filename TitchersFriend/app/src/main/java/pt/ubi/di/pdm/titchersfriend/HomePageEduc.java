package pt.ubi.di.pdm.titchersfriend;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class HomePageEduc extends AppCompatActivity {

   ImageButton gerirAlunos,gerirAulas,MudarPassword,faltas;
    Button exit;
DBHelper dbHelper;
SQLiteDatabase base;
    private void fechando(){
        String x ="false";
        String total ="";
        String user="";
        int count=0;
        Cursor cursor =base.query(dbHelper.TABLE_NAME7,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){
        user = cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T7));
        }
       cursor =base.query(dbHelper.TABLE_NAME1,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){

            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T1));
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T1));
            String c3 =cursor.getString(cursor.getColumnIndex(dbHelper.COL3_T1));
            String c4 =cursor.getString(cursor.getColumnIndex(dbHelper.COL4_T1));
            String c5 =cursor.getString(cursor.getColumnIndex(dbHelper.COL5_T1));
            String c6 =cursor.getString(cursor.getColumnIndex(dbHelper.COL6_T1));
            if(count==0)
                total="user="+user+"&"+"ed"+"="+c1+","+c2+","+c3+","+c4+","+c5+","+c6;
            if(count!=0)
                total=total+";"+c1+","+c2+","+c3+","+c4+","+c5+","+c6;
            count++;
        }
        total = total+"&";
        count =0;
        cursor =base.query(dbHelper.TABLE_NAME2,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){

            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T2));
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T2));
            String c3 =cursor.getString(cursor.getColumnIndex(dbHelper.COL3_T2));
            if(count==0)
                total=total+"at"+"="+c1+","+c2+","+c3;
            if(count!=0)
                total=total+";"+c1+","+c2+","+c3;
            count++;
        }
        total = total+"&";
        count =0;
        cursor =base.query(dbHelper.TABLE_NAME3,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){

            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T3));
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T3));

            if(count==0)
                total=total+"al"+"="+c1+","+c2;
            if(count!=0)
                total=total+";"+c1+","+c2;
            count++;
        }
        total = total+"&";
        count =0;
        cursor =base.query(dbHelper.TABLE_NAME4,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){

            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T4));
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T4));

            if(count==0)
                total=total+"fa"+"="+c1+","+c2;
            if(count!=0)
                total=total+";"+c1+","+c2;
            count++;
        }
        total = total+"&";
        count =0;
        cursor =base.query(dbHelper.TABLE_NAME5,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){

            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T5));
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T5));
            String c3 =cursor.getString(cursor.getColumnIndex(dbHelper.COL3_T5));
            String c4 =cursor.getString(cursor.getColumnIndex(dbHelper.COL4_T5));
            String c5 =cursor.getString(cursor.getColumnIndex(dbHelper.COL5_T5));
            String c6 =cursor.getString(cursor.getColumnIndex(dbHelper.COL6_T5));
            String c7 =cursor.getString(cursor.getColumnIndex(dbHelper.COL7_T5));
            if(count==0)
                total=total+"rel"+"="+c1+","+c2+","+c3+","+c4+","+c5+","+c6+","+c7;
            if(count!=0)
                total=total+";"+c1+","+c2+","+c3+","+c4+","+c5+","+c6+","+c7;
            count++;
        }
        total = total+"&";
        count =0;
        cursor =base.query(dbHelper.TABLE_NAME6,new String[]{"*"},null,null,null,null,null);
        while (cursor.moveToNext()){

            String c1 =cursor.getString(cursor.getColumnIndex(dbHelper.COL1_T6));
            String c2 =cursor.getString(cursor.getColumnIndex(dbHelper.COL2_T6));

            if(count==0)
                total=total+"cont"+"="+c1+","+c2;
            if(count!=0)
                total=total+";"+c1+","+c2;
            count++;
        }
        try {
            x = new Sender(HomePageEduc.this,"104",total,null).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        dbHelper.delete();

    }
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepageeduc);

        dbHelper = new DBHelper(this);
        base = dbHelper.getWritableDatabase();
       gerirAlunos = (ImageButton)findViewById(R.id.gerirAlunos) ;
       gerirAulas = (ImageButton)findViewById(R.id.gerirAulas) ;
       MudarPassword = (ImageButton)findViewById(R.id.mudarPass) ;
       faltas = (ImageButton)findViewById(R.id.gerirEduc) ;
        exit =(Button)findViewById(R.id.btnSair);
       gerirAlunos.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent A2 = new Intent(HomePageEduc.this,GerirAlunos.class);
               startActivity(A2) ;
           }
       });

        gerirAulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A2 = new Intent(HomePageEduc.this,GerirAulas.class);
                startActivity(A2) ;
            }
        });

        MudarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A2 = new Intent(HomePageEduc.this,MudarPassword.class);
                startActivity(A2) ;
            }
        });

        faltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent A2 = new Intent(HomePageEduc.this,Relatorio.class);
                startActivity(A2) ;


            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             fechando();
               // System.exit(0);
            }

        });

    }
    @Override
    public void onPause() {

        super.onPause();


        base.close();

    }
    @Override
    public void onResume() {

        super.onResume();
        base = dbHelper.getWritableDatabase();
    }

}
