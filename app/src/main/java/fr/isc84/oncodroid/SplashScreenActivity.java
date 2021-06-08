package fr.isc84.oncodroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    private final  int SPLASH_SCREEN_TIMEOUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Redirection vers la page principale apres 2 secondes
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
            // demarrer une page
                Intent intent = new Intent(getApplicationContext(), AudioRecorderActivity.class);
                startActivity(intent);
                finish();
            }
        };
        // Gestion du temps d'envoi
        new Handler().postDelayed(runnable, SPLASH_SCREEN_TIMEOUT);
    }
}