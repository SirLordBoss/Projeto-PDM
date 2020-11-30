package com.example.diogo;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/*
1.OUR LAUNCHER ACTIVITY
2.INITIALIZE SOME UI STUFF
3.WE START SENDER ON BUTTON CLICK
 */
public class MainActivity extends AppCompatActivity {

    int h = -1000;
    String urlAddress="http://teachersfriend.atwebpages.com/";
    EditText nameTxt,posTxt, res;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //INITIALIZE UI FIELDS
        nameTxt= (EditText) findViewById(R.id.Query);
        posTxt= (EditText) findViewById(R.id.Extra);
        res= (EditText) findViewById(R.id.r);
        saveBtn= (Button) findViewById(R.id.button)    ;
        Context x =  this;

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = null;
                try {
                   x = new Sender(MainActivity.this,urlAddress,nameTxt,posTxt).execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                res.setText(x);

            }
        });

    }

}
