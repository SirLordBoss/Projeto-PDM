package pt.ubi.di.pdm.titchersfriend;


import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;



public class RegisterActivity extends AppCompatActivity {
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
    }





}
