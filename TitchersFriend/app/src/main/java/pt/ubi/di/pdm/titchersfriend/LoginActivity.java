package pt.ubi.di.pdm.titchersfriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText inputpass,inputUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.btnLogin) ;
        inputpass = (EditText) findViewById(R.id.inputPass);
        inputUser = (EditText) findViewById(R.id.inputUser);

        Intent iCameFromActivity1 = getIntent() ;
    }

    //metodo para confirmar login
    public void confirmLogin(View v){

    }
}
