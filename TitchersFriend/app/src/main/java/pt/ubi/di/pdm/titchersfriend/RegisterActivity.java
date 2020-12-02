package pt.ubi.di.pdm.titchersfriend;


import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {
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

    Button btnCriar;
    EditText inputpass,inputEmail,inputRepPass,inputUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criarconta);

        btnCriar = (Button)findViewById(R.id.btnCriar) ;
        inputpass = (EditText) findViewById(R.id.inputPass);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputRepPass = (EditText) findViewById(R.id.inputRepPass);
        inputUser = (EditText) findViewById(R.id.inputUser);

        Intent iCameFromActivity1 = getIntent() ;


        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x ="false";
                String us = inputUser.getText().toString();
                String pass = inputpass.getText().toString();
                String passr = inputRepPass.getText().toString();
                String email = inputEmail.getText().toString();
                if(!passr.equals(pass)){
                    Intent A2 = new Intent(RegisterActivity.this,RegisterActivity.class);
                    finish();
                    startActivity(A2) ;
                    Toast.makeText(RegisterActivity.this,"Palavras-passe diferem",Toast.LENGTH_LONG).show();
                }
                String enc = getM5(pass);

                try {
                    x = new Sender(RegisterActivity.this,"http://teachersfriend.ddns.net/service.php","50","u="+us+"&p="+enc+"&e="+email).execute().get();
                } catch (ExecutionException e) {

                    e.printStackTrace();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
                if(x.contains("true")){
                    Intent A2 = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(A2) ;
                    Toast.makeText(RegisterActivity.this,"Sucesso na criação da conta",Toast.LENGTH_LONG).show();
                }
                if(x.contains("false")){
                    Intent A2 = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(A2) ;
                    Toast.makeText(RegisterActivity.this,"Erro na criação da conta",Toast.LENGTH_LONG).show();
                }

            }
        });
    }





}
