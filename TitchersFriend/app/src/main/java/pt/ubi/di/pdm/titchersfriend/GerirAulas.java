package pt.ubi.di.pdm.titchersfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GerirAulas extends AppCompatActivity {

    Button sumario;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geriraulas);

        sumario = (Button)findViewById(R.id.btnAddAula);
        sumario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GerirAulas.this,Sumarios.class);
                startActivity(i);
            }
        });

    }
}
