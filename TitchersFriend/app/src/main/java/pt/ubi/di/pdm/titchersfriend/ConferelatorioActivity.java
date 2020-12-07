package pt.ubi.di.pdm.titchersfriend;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConferelatorioActivity extends Activity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrelatorio);

        btn = (Button)findViewById(R.id.btnSubmeterRel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndSendPDF();
            }
        });
    }

    private void createAndSendPDF(){
        PdfDocument myDocument = new PdfDocument();
        Paint myPaint = new Paint();

        PdfDocument.PageInfo myDocumentInfo = new PdfDocument.PageInfo.Builder(250,400,1).create();
        PdfDocument.Page myPage1 = myDocument.startPage(myDocumentInfo);
        Canvas cnvs = myPage1.getCanvas();
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.fundo);
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap,250,400,false);
        cnvs.drawBitmap(scaled,0,0,myPaint);
        cnvs.drawText(getString(R.string.app_name),40,50,myPaint);

        myDocument.finishPage(myPage1);

        File f = new File(getApplicationContext().getExternalFilesDir(null),"/lastReport.pdf");
        try{
            myDocument.writeTo(new FileOutputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myDocument.close();

    }
}
