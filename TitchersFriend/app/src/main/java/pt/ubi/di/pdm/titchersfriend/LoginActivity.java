package pt.ubi.di.pdm.titchersfriend;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.*;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText inputpass,inputUser;
    DBHelper dbHelper;
    SQLiteDatabase oSQLDB;
        public static String getM5(String input) {
            try {

                // Static getInstance method is called with hashing MD5
                MessageDigest md = MessageDigest.getInstance("MD5");

                // digest() method is called to calculate message digest
                //  of an input digest() return array of byte
                byte[] messageDigest = md.digest(input.getBytes());

                // Convert byte array into signum representation
                BigInteger no = new BigInteger(1, messageDigest);

                // Convert message digest into hex value
                String hashtext = no.toString(16);
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                return hashtext;
            }

            // For specifying wrong message digest algorithms
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

    protected void onCreate(Bundle savedInstanceState) {
     
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);
        oSQLDB = dbHelper.getWritableDatabase();

        btnLogin = (Button)findViewById(R.id.btnLogin) ;
        inputpass = (EditText) findViewById(R.id.inputPass);
        inputUser = (EditText) findViewById(R.id.inputUser);

       // Intent iCameFromActivity1 = getIntent() ;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = null;
                String us = inputUser.getText().toString();
                String pass = inputpass.getText().toString();
                String enc = getM5(pass);

                try {
                    x = new Sender(LoginActivity.this,"100","u="+us+"&p="+enc,null).execute().get();

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                JSONObject reader;
                Boolean s = false;
                try {
                    if(x==null){
                        return;
                    }
                    reader = new JSONObject(x);
                    s = reader.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(s){
                    //receber dados
                    try {
                        RecebeDados(x, us);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent A2 = new Intent(LoginActivity.this,HomePageEduc.class);

                    startActivity(A2) ;
                }else{
                    Toast.makeText(LoginActivity.this,"Falha no Login",Toast.LENGTH_SHORT).show();
                    inputpass.setText("");
                    inputUser.setText("");
                    return;
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }

    public void RecebeDados(String x, String us) throws JSONException {
        JSONObject reader = new JSONObject(x);
        ContentValues oCV = new ContentValues();
        String s = reader.getString("educando");

        String[] arr = s.split(";");

        for(int i=0;i<arr.length;i++){
            String[] aux = arr[i].split(",");
            oCV.put(dbHelper.COL1_T1,aux[0]);
            oCV.put(dbHelper.COL2_T1,aux[1]);
            oCV.put(dbHelper.COL3_T1,aux[2]);
            oCV.put(dbHelper.COL4_T1,aux[3]);
            oCV.put(dbHelper.COL5_T1,aux[4]);
            oCV.put(dbHelper.COL6_T1,aux[5]);
            oSQLDB.insert(dbHelper.TABLE_NAME1,null,oCV);
        }

         s = reader.getString("atividade");
        arr = s.split(";");
         oCV.clear();

        for(int i=0;i<arr.length;i++){
            String[] aux = arr[i].split(",");
            oCV.put(dbHelper.COL1_T2,aux[0]);
            oCV.put(dbHelper.COL2_T2,aux[1]);
            oCV.put(dbHelper.COL3_T2,aux[2]);
            oSQLDB.insert(dbHelper.TABLE_NAME2,null,oCV);
        }

        s = reader.getString("alergias");
        arr = s.split(";");
        oCV.clear();

        for(int i=0;i<arr.length;i++){
            String[] aux = arr[i].split(",");
            oCV.put(dbHelper.COL1_T3,aux[0]);
            oCV.put(dbHelper.COL2_T3,aux[1]);
            oSQLDB.insert(dbHelper.TABLE_NAME3,null,oCV);
        }

        s = reader.getString("faltas");
        arr = s.split(";");
        oCV.clear();

        for(int i=0;i<arr.length;i++){
            String[] aux = arr[i].split(",");
            oCV.put(dbHelper.COL1_T4,aux[0]);
            oCV.put(dbHelper.COL2_T4,aux[1]);
            oSQLDB.insert(dbHelper.TABLE_NAME4,null,oCV);
        }

        s = reader.getString("relatorio");
        arr = s.split(";");
        oCV.clear();

        for(int i=0;i<arr.length;i++){
            String[] aux = arr[i].split(",");
            Log.d("aux",aux[0]+aux[1]+aux[2]+aux[3]+aux[4]+aux[5]+aux[6]);
            oCV.put(dbHelper.COL1_T5,aux[0]);
            oCV.put(dbHelper.COL2_T5,aux[1]);
            oCV.put(dbHelper.COL3_T5,aux[2]);
            oCV.put(dbHelper.COL4_T5,aux[3]);
            oCV.put(dbHelper.COL5_T5,aux[4]);
            oCV.put(dbHelper.COL6_T5,aux[5]);
            oCV.put(dbHelper.COL7_T5,aux[6]);
            oSQLDB.insert(dbHelper.TABLE_NAME5,null,oCV);
        }

        s = reader.getString("contem");
        arr = s.split(";");
        oCV.clear();

        for(int i=0;i<arr.length;i++){
            String[] aux = arr[i].split(",");
            oCV.put(dbHelper.COL1_T6,aux[0]);
            oCV.put(dbHelper.COL2_T6,aux[1]);
            oSQLDB.insert(dbHelper.TABLE_NAME6,null,oCV);
        }
        oCV.clear();
        oCV.put(dbHelper.COL1_T7,"111");
        oCV.put(dbHelper.COL2_T7,us);
        oSQLDB.insert(dbHelper.TABLE_NAME7,null,oCV);
    }
    
}
