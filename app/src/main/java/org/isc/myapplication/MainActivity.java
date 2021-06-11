
 package org.isc.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity {

     private Button recordingButton;
    private Button QrCodeButton;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         this.recordingButton = (Button) findViewById(R.id.recordingButton);

         this.QrCodeButton = (Button) findViewById(R.id.qrCodeButton);

         this.recordingButton.setOnClickListener((e)->{
             this.onClickRecordingButton();
         });

         this.QrCodeButton.setOnClickListener((e)->{
             this.onClickQRCodeButton();
         });
     }
    public void onClickRecordingButton(){
         String text = "I'm scanning !!";
        Toast.makeText( this.getApplicationContext(), text, 2* 1000  )
                .show();
        System.out.println(text);
    }

    public void onClickQRCodeButton(){
        String text ="I'm recording !!";

        Toast.makeText( this.getApplicationContext() , "I'm recording !!", 2* 1000  )
                .show();
        System.out.println(text);
        Intent QRCodeActivity = new Intent(getApplicationContext(), org.isc.myapplication.QRCodeActivity.class);
        startActivity(QRCodeActivity);
        //finish();

    }




}