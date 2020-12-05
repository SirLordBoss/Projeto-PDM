package pt.ubi.di.pdm.titchersfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomePageEduc extends AppCompatActivity {

   ImageButton gerirAlunos,gerirAulas,sumario,faltas;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepageeduc);

       gerirAlunos = (ImageButton)findViewById(R.id.gerirAlunos) ;
       gerirAulas = (ImageButton)findViewById(R.id.gerirAulas) ;
       sumario = (ImageButton)findViewById(R.id.gerirSumarios) ;
       faltas = (ImageButton)findViewById(R.id.gerirEduc) ;

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

        sumario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A2 = new Intent(HomePageEduc.this,Sumarios.class);
                startActivity(A2) ;
            }
        });

        faltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent A2 = new Intent(HomePageEduc.this,MarcarFaltas.class);
                startActivity(A2) ;


            }
        });

    }

}

