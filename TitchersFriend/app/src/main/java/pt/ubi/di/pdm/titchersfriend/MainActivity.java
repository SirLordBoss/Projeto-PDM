package pt.ubi.di.pdm.titchersfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

public class MainActivity extends AppCompatActivity {

    TextView oTV;
    Button btnCriaConta;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oTV = (TextView)findViewById(R.id.Título);
        btnCriaConta = (Button)findViewById(R.id.btnCriarConta);
        btnLogin = (Button)findViewById(R.id.btnLogin);

    }

    public void startLogin(View v){
        Intent A1 = new Intent(this,LoginActivity.class);
        startActivity(A1) ;
    }

    public void startRegister(View v){
        Intent A2 = new Intent(this,RegisterActivity.class);
        startActivity(A2) ;
    }
}