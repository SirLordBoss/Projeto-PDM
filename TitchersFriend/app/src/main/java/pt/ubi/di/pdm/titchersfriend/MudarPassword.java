package pt.ubi.di.pdm.titchersfriend;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class MudarPassword extends AppCompatActivity {

    EditText PassAnt, PassNova, RepPassNova;
    Button Submeter,Cancelar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudarpass);

        PassAnt = (EditText) findViewById(R.id.inputPassOld);
        PassNova = (EditText) findViewById(R.id.inputPass);
        RepPassNova = (EditText) findViewById(R.id.inputRepPass);
        Submeter = (Button) findViewById(R.id.btnSubmeterRel);
        Cancelar = (Button) findViewById(R.id.btnCancelarRel);

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
