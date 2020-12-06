package pt.ubi.di.pdm.titchersfriend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Sumarios extends AppCompatActivity {

    EditText Esumario,Enotas;
    Button btnSubmeter,btnCancelar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sumarios);

        Esumario = (EditText)findViewById(R.id.inputSumario);
        Enotas = (EditText)findViewById(R.id.inputNotas);

        btnCancelar = (Button)findViewById(R.id.btnCancelarRel);
        btnSubmeter = (Button)findViewById(R.id.btnSubmeterRel);


    }
}
