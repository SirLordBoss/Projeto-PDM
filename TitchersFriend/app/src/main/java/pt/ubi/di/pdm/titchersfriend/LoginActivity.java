package pt.ubi.di.pdm.titchersfriend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText inputpass,inputUser;
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

        btnLogin = (Button)findViewById(R.id.btnLogin) ;
        inputpass = (EditText) findViewById(R.id.inputPass);
        inputUser = (EditText) findViewById(R.id.inputUser);

       // Intent iCameFromActivity1 = getIntent() ;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x ="false";
                String us = inputUser.getText().toString();
                String pass = inputpass.getText().toString();
                String enc = getM5(pass);

               try {
                    x = new Sender(LoginActivity.this,"http://teachersfriend.ddns.net/service.php","100","u="+us+"&p="+enc).execute().get();
                } catch (ExecutionException e) {

                    e.printStackTrace();
                } catch (InterruptedException e) {

                 e.printStackTrace();
                }
                if(x.contains("true")){
                    //Intent A2 = new Intent(LoginActivity.this,HomePageEduc.class);
                    //startActivity(A2) ;
                }
                if(x.contains("false")){
                    Intent A2 = new Intent(LoginActivity.this,HomePageEduc.class);//metemos aqui pq enquanto testamos n temos la o login da smp false
                    startActivity(A2);
                }

            }
        });
    }


    //metodo para confirmar login
}
